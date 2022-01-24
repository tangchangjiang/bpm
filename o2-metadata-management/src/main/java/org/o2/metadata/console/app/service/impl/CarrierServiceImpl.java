package org.o2.metadata.console.app.service.impl;

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
    public List<Carrier> batchMerge(Long organizationId, final List<Carrier> carrierList) {
        List<Carrier> updateList = new ArrayList<>();
        List<Carrier> insertList = new ArrayList<>();
        // 中台页面 控制了不能批量新建/更新数据 后续前端改成批量在优化
        List<Carrier> resultList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(carrierList)) {
            for (Carrier carrier : carrierList) {
                carrier.setTenantId(organizationId);
                // 新增
                if (null == carrier.getCarrierId()) {
                    validCarrierNameUnique(carrier);
                    validCarrierCodeUnique(carrier);
                    insertList.add(carrier);
                } else {
                    SecurityTokenHelper.validToken(carrier);
                    // 更新
                    Carrier original = carrierRepository.selectByPrimaryKey(carrier);
                    if (!original.getCarrierName().equals(carrier.getCarrierName())) {
                        validCarrierNameUnique(carrier);
                    }
                    if (!original.getCarrierCode().equals(carrier.getCarrierCode())) {
                        throw new O2CommonException(null, CarrierConstants.ErrorCode.ERROR_CARRIER_CODE_NOT_UPDATE, CarrierConstants.ErrorCode.ERROR_CARRIER_CODE_NOT_UPDATE);
                    }
                    updateList.add(carrier);
                }
            }
            carrierRepository.batchUpdateByPrimaryKey(updateList);
            carrierRepository.batchInsertSelective(insertList);
            carrierRedis.batchUpdateRedis(organizationId);
            resultList.addAll(updateList);
            resultList.addAll(insertList);
        }
        return resultList;
    }

    /**
     * 验证承运商名称唯一性
     * @param carrier 承运商
     */
    private void validCarrierNameUnique(Carrier carrier){
        Carrier query = new Carrier();
        query.setCarrierName(carrier.getCarrierName());
        query.setTenantId(carrier.getTenantId());
        List<Carrier> list = carrierRepository.select(query);
        if (!list.isEmpty()) {
            throw new O2CommonException(null,CarrierConstants.ErrorCode.ERROR_CARRIER_NAME_DUPLICATE,CarrierConstants.ErrorCode.ERROR_CARRIER_NAME_DUPLICATE);
        }
    }
    /**
     * 验证承运商编码唯一性
     * @param carrier 承运商
     */
    private void validCarrierCodeUnique(Carrier carrier){
        Carrier query = new Carrier();
        query.setCarrierCode(carrier.getCarrierCode());
        query.setTenantId(carrier.getTenantId());
        List<Carrier> list = carrierRepository.select(query);
        if (!list.isEmpty()) {
            throw new O2CommonException(null,CarrierConstants.ErrorCode.ERROR_CARRIER_CODE_DUPLICATE,CarrierConstants.ErrorCode.ERROR_CARRIER_CODE_DUPLICATE);
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
        for (Carrier carrier : carriers) {
            CarrierCO carrierVO = new CarrierCO();
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

    @Override
    public Map<String, CarrierCO> importListCarriers(Long organizationId) {
        Map<String, CarrierCO> map = new HashMap<>(16);
        Carrier carrier = new Carrier();
        carrier.setTenantId(organizationId);
        List<Carrier> carriers = carrierRepository.listCarrier(carrier);
        if (carriers.isEmpty()) {
            return map;
        }
        for (Carrier carri : carriers) {
            CarrierCO carrierVO = new CarrierCO();
            carrierVO.setCarrierCode(carri.getCarrierCode());
            carrierVO.setCarrierName(carri.getCarrierName());
            carrierVO.setActiveFlag(carri.getActiveFlag());
            map.put(carri.getCarrierName(), carrierVO);
        }
        return map;
    }
}
