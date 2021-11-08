package org.o2.metadata.console.app.service.impl;

import io.choerodon.core.exception.CommonException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.o2.core.exception.O2CommonException;
import org.o2.metadata.console.api.co.CarrierMappingCO;
import org.o2.metadata.console.api.dto.CarrierMappingQueryInnerDTO;
import org.o2.metadata.console.api.dto.CarrierQueryInnerDTO;
import org.o2.metadata.console.api.co.CarrierCO;
import org.o2.metadata.console.app.service.CarrierService;
import org.o2.metadata.console.infra.constant.CarrierConstants;
import org.o2.metadata.console.infra.convertor.CarrierConverter;
import org.o2.metadata.console.infra.entity.Carrier;
import org.o2.metadata.console.infra.entity.CarrierDeliveryRange;
import org.o2.metadata.console.infra.entity.CarrierMapping;
import org.o2.metadata.console.infra.entity.PosRelCarrier;
import org.o2.metadata.console.infra.redis.CarrierRedis;
import org.o2.metadata.console.infra.repository.CarrierDeliveryRangeRepository;
import org.o2.metadata.console.infra.repository.CarrierMappingRepository;
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
    private final CarrierMappingRepository carrierMappingRepository;


    public CarrierServiceImpl(final CarrierRepository carrierRepository,
                              final CarrierDeliveryRangeRepository carrierDeliveryRangeRepository,
                              final PosRelCarrierRepository posRelCarrierRepository,
                              CarrierRedis carrierRedis, CarrierMappingRepository carrierMappingRepository) {
        this.carrierRepository = carrierRepository;
        this.carrierDeliveryRangeRepository = carrierDeliveryRangeRepository;
        this.posRelCarrierRepository = posRelCarrierRepository;
        this.carrierRedis = carrierRedis;
        this.carrierMappingRepository = carrierMappingRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Carrier> batchUpdate(Long organizationId, final List<Carrier> carrierList) {
        // 中台页面 控制了不能批量新建/更新数据
        for (Carrier carrier : carrierList) {
            validCarrierNameUnique(carrier.getCarrierName(),organizationId);
            carrier.setTenantId(organizationId);
        }
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
        // 中台页面 控制了不能批量新建/更新数据
        for (Carrier carrier : carrierList) {
            validCarrierNameUnique(carrier.getCarrierName(),organizationId);
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

    /**
     * 验证承运商名称唯一性
     * @param name 运费名称
     * @param tenantId 租户ID
     */
    private void validCarrierNameUnique(String name,Long tenantId){
        Carrier query = new Carrier();
        query.setCarrierName(name);
        query.setTenantId(tenantId);
        List<Carrier> list = carrierRepository.select(query);
        if (!list.isEmpty()) {
            throw new O2CommonException(null,CarrierConstants.ErrorCode.ERROR_CARRIER_NAME_DUPLICATE,CarrierConstants.ErrorCode.ERROR_CARRIER_NAME_DUPLICATE);
        }
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(Long organizationId, List<Carrier> carrierList) {
        List<Long> ids = new ArrayList<>(carrierList.size());
        for (final Carrier carrier : carrierList) {
            carrier.setTenantId(organizationId);
            ids.add(carrier.getCarrierId());
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
        List<Carrier> carriers = carrierRepository.selectByIds(StringUtils.join(ids, ","));
        carrierRepository.batchDeleteByPrimaryKey(carrierList);
        carrierRedis.deleteRedis(carriers,organizationId);
    }

    @Override
    public Map<String, CarrierCO> listCarriers(CarrierQueryInnerDTO carrierQueryInnerDTO, Long organizationId) {
        Map<String, CarrierCO> map = new HashMap<>(16);
        List<Carrier> carriers = carrierRepository.batchSelect(carrierQueryInnerDTO,organizationId);
        if (carriers.isEmpty()) {
            return map;
        }
        CarrierCO carrierVO = new CarrierCO();
        for (Carrier carrier : carriers) {
            carrierVO.setCarrierCode(carrier.getCarrierCode());
            carrierVO.setCarrierName(carrier.getCarrierName());
            map.put(carrier.getCarrierCode(), carrierVO);
        }
        return map;
    }

    @Override
    public Map<String, CarrierMappingCO> listCarrierMappings(CarrierMappingQueryInnerDTO queryInnerDTO, Long organizationId) {
        queryInnerDTO.setTenantId(organizationId);
        Map<String, CarrierMappingCO> map = new HashMap<>(16);
        List<CarrierMapping> list = carrierMappingRepository.listCarrierMappings(queryInnerDTO);
        if(list.isEmpty()) {
            return map;
        }
        List<CarrierMappingCO> cos = CarrierConverter.poToCoListObjects(list);
        for (CarrierMappingCO carrier : cos) {
            map.put(carrier.getCarrierCode(),  carrier);
        }
        return map;
    }

    /**
     * 校验查重
     *
     * @param carrieies 承运商
     * @param isCheckId 是否
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
