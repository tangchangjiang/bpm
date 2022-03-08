package org.o2.metadata.console.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.o2.metadata.console.app.service.CarrierMappingTenantInitService;
import org.o2.metadata.console.infra.constant.TenantInitConstants;
import org.o2.metadata.console.infra.entity.Carrier;
import org.o2.metadata.console.infra.entity.CarrierMapping;
import org.o2.metadata.console.infra.repository.CarrierMappingRepository;
import org.o2.metadata.console.infra.repository.CarrierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 承运商匹配租户初始化
 *
 * @author yipeng.zhu@hand-china.com 2022-2-11
 */
@Service
@Slf4j
public class CarrierMappingTenantInitServiceImpl implements CarrierMappingTenantInitService {
    private final CarrierMappingRepository carrierMappingRepository;
    private final CarrierRepository carrierRepository;

    public CarrierMappingTenantInitServiceImpl(CarrierMappingRepository carrierMappingRepository,
                                               CarrierRepository carrierRepository) {
        this.carrierMappingRepository = carrierMappingRepository;
        this.carrierRepository = carrierRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void tenantInitializeBusiness(long sourceTenantId, Long targetTenantId) {
        // 1. 查询源租户
        log.info("Business: carrier start");
        CarrierMapping query = new CarrierMapping();
        query.setTenantId(sourceTenantId);
        query.setPlatformCodes(TenantInitConstants.CarrierMappingBusiness.PLATFORM_CODES);
        query.setCarrierCodes(TenantInitConstants.CarrierBusiness.CARRIERS);
        List<CarrierMapping> sourceCarrierMapping = carrierMappingRepository.listByCondition(query);
        if (CollectionUtils.isEmpty(sourceCarrierMapping)) {
            log.info("Business: carrier is empty.");
            return;
        }
        // 2. 查询目标租户
        query.setTenantId(targetTenantId);
        List<CarrierMapping> targetCarrierMapping = carrierMappingRepository.listByCondition(query);
        handleData(targetCarrierMapping,sourceCarrierMapping,targetTenantId);
        log.info("Business: carrier finish");
    }

    /**
     * 删除已存在的&重新初始化
     * @param oldList 目标租户已存在
     * @param initList 目标租户需要重新初始化
     * @param targetTenantId 目标租户
     */
    private void handleData( List<CarrierMapping> oldList, List<CarrierMapping> initList,Long targetTenantId) {
        // 承运商编码
        List<String> targetCarrierCode = new ArrayList<>(initList.size());
        for (CarrierMapping carrierMapping : initList ) {
            targetCarrierCode.add(carrierMapping.getCarrierCode());
        }
        Carrier query = new Carrier();
        query.setTenantId(targetTenantId);
        query.setCarrierCodes(targetCarrierCode);
        List<Carrier> targetCarriers = carrierRepository.listCarrier(query);
        Map<String,Long> targetCarrierMap = new HashMap<>(targetCarriers.size());
        for (Carrier targetCarrier : targetCarriers) {
            targetCarrierMap.put(targetCarrier.getCarrierCode(),targetCarrier.getCarrierId());
        }

        if (CollectionUtils.isNotEmpty(oldList)) {
           carrierMappingRepository.batchDeleteByPrimaryKey(oldList);
        }
        for (CarrierMapping carrierMapping : initList) {
            carrierMapping.setCarrierMappingId(null);
            carrierMapping.setCarrierId(targetCarrierMap.get(carrierMapping.getCarrierCode()));
            carrierMapping.setTenantId(targetTenantId);
        }
        carrierMappingRepository.batchInsert(initList);
    }
}
