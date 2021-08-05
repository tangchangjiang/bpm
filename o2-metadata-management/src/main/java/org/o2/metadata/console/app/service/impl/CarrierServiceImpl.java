package org.o2.metadata.console.app.service.impl;

import io.choerodon.core.exception.CommonException;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.console.api.dto.CarrierDTO;
import org.o2.metadata.console.api.vo.CarrierVO;
import org.o2.metadata.console.app.service.CarrierService;
import org.o2.metadata.console.infra.constant.CarrierConstants;
import org.o2.metadata.console.infra.entity.Carrier;
import org.o2.metadata.console.infra.entity.CarrierDeliveryRange;
import org.o2.metadata.console.infra.entity.PosRelCarrier;
import org.o2.metadata.console.infra.redis.CarrierRedis;
import org.o2.metadata.console.infra.repository.CarrierDeliveryRangeRepository;
import org.o2.metadata.console.infra.repository.CarrierRepository;
import org.o2.metadata.console.infra.repository.PosRelCarrierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 承运商应用服务默认实现
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Service
public class CarrierServiceImpl implements CarrierService {
    private final CarrierRepository carrierRepository;
    private final CarrierDeliveryRangeRepository carrierDeliveryRangeRepository;
    private final PosRelCarrierRepository posRelCarrierRepository;
    private final CarrierRedis carrierRedis;


    public CarrierServiceImpl(final CarrierRepository carrierRepository,
                              final CarrierDeliveryRangeRepository carrierDeliveryRangeRepository,
                              final PosRelCarrierRepository posRelCarrierRepository,
                              CarrierRedis carrierRedis) {
        this.carrierRepository = carrierRepository;
        this.carrierDeliveryRangeRepository = carrierDeliveryRangeRepository;
        this.posRelCarrierRepository = posRelCarrierRepository;
        this.carrierRedis = carrierRedis;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Carrier> batchUpdate(Long organizationId, final List<Carrier> carrierList) {
        carrierList.forEach(carrier -> {
            carrier.setTenantId(organizationId);
        });
        checkData(carrierList, true);
        List<Carrier> list = carrierRepository.batchUpdateByPrimaryKey(carrierList);
        carrierRedis.batchUpdateRedis(organizationId);
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Carrier> batchMerge(Long organizationId, final List<Carrier> carrierList) {
        List<Carrier> unique = carrierList.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Carrier::getCarrierCode))), ArrayList::new));
        if (unique.size() != carrierList.size()) {
            throw new CommonException(CarrierConstants.ErrorCode.O2MD_ERROR_CARRIER_EXISTS);
        }
        final List<Carrier> updateList = new ArrayList<>();
        final List<Carrier> insertList = new ArrayList<>();
        for (int i = 0; i < carrierList.size(); i++) {
            Carrier carrier = carrierList.get(i);
            carrier.setTenantId(organizationId);
            carrier.validate();
            if (carrier.getCarrierId() != null) {
                SecurityTokenHelper.validToken(carrier);
                updateList.add(carrier);
            } else {
                if (carrier.exist(carrierRepository)) {
                    throw new CommonException(CarrierConstants.ErrorCode.O2MD_ERROR_CARRIER_EXISTS);
                }
                insertList.add(carrier);
            }
        }
        final List<Carrier> resultList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(updateList)) {
            resultList.addAll(carrierRepository.batchUpdateByPrimaryKey(updateList));
        }
        if (CollectionUtils.isNotEmpty(insertList)) {
            resultList.addAll(carrierRepository.batchInsertSelective(insertList));
        }
        carrierRedis.batchUpdateRedis(organizationId);
        return resultList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(Long organizationId, List<Carrier> carrierList) {
        for (final Carrier carrier : carrierList) {
            carrier.setTenantId(organizationId);
            if (carrier.getCarrierId() != null) {
                Long carrierId = carrier.getCarrierId();
                //删除承运商送达范围
                final List<CarrierDeliveryRange> list = carrierDeliveryRangeRepository.selectByCondition(Condition.builder(CarrierDeliveryRange.class)
                        .andWhere(Sqls.custom().andEqualTo(CarrierDeliveryRange.FIELD_TENANT_ID, organizationId)
                                .andEqualTo(CarrierDeliveryRange.FIELD_CARRIER_ID, carrierId)).build());
                carrierDeliveryRangeRepository.batchDeleteByPrimaryKey(list);
                //删除服务点关联承运商
                final List<PosRelCarrier> posRelCarrierList = posRelCarrierRepository.selectByCondition(Condition.builder(PosRelCarrier.class).andWhere(Sqls.custom()
                        .andEqualTo(PosRelCarrier.FIELD_TENANT_ID, organizationId).andEqualTo(PosRelCarrier.FIELD_CARRIER_ID, carrierId)).build());
                posRelCarrierRepository.batchDeleteByPrimaryKey(posRelCarrierList);
            }
        }
        carrierRepository.batchDeleteByPrimaryKey(carrierList);
        carrierRedis.batchUpdateRedis(organizationId);
    }

    @Override
    public Map<String, CarrierVO> listCarriers(CarrierDTO carrierDTO, Long organizationId) {
        Map<String, CarrierVO> map = new HashMap<>(16);
        if (null == carrierDTO.getCarrierCodes() && null == carrierDTO.getCarrierNames()) {
            return map;
        }
        List<Carrier> carriers = carrierRepository.batchSelect(carrierDTO,organizationId);
        if (carriers.isEmpty()) {
            return map;
        }
        CarrierVO carrierVO = new CarrierVO();
        for (Carrier carrier : carriers) {
            carrierVO.setCarrierCode(carrier.getCarrierCode());
            carrierVO.setCarrierName(carrier.getCarrierName());
            map.put(carrier.getCarrierCode(), carrierVO);
        }
        return map;
    }

    /**
     * 校验查重
     *
     * @param carrieies
     * @param isCheckId
     */
    private void checkData(final List<Carrier> carrieies, final boolean isCheckId) {
        final Map<String, Object> map = new HashMap<>(carrieies.size());
        for (int i = 0; i < carrieies.size(); i++) {
            final Carrier carrier = carrieies.get(i);
            if (isCheckId) {
                Assert.notNull(carrier.getCarrierId(), "carrierId must not be null");
            }
            carrier.validate();
            // 数据库查重
            Assert.isTrue(!carrier.exist(carrierRepository), "could not exist same carrier");
            // list查重
            Assert.isTrue(map.get(carrier.getCarrierCode()) == null, "could not exist same carrier");
            map.put(carrier.getCarrierCode(), i);
        }
    }

}
