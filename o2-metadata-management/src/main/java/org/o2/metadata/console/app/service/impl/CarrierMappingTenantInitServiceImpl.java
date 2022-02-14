package org.o2.metadata.console.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.o2.metadata.console.app.service.CarrierMappingTenantInitService;
import org.o2.metadata.console.infra.constant.TenantInitConstants;
import org.o2.metadata.console.infra.entity.CarrierMapping;
import org.o2.metadata.console.infra.repository.CarrierMappingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 承运商匹配租户初始化
 *
 * @author yipeng.zhu@hand-china.com 2022-2-11
 */
@Service
@Slf4j
public class CarrierMappingTenantInitServiceImpl implements CarrierMappingTenantInitService {
    private final CarrierMappingRepository carrierMappingRepository;

    public CarrierMappingTenantInitServiceImpl(CarrierMappingRepository carrierMappingRepository) {
        this.carrierMappingRepository = carrierMappingRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void tenantInitializeBusiness(long sourceTenantId, Long targetTenantId) {
        // 1. 查询源租户
        log.info("Business: carrier start");
        CarrierMapping query = new CarrierMapping();
        query.setTenantId(sourceTenantId);
        query.setPlatformCodes(TenantInitConstants.CarrierMappingBusiness.PLATFORM_CODES);
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
        if (CollectionUtils.isNotEmpty(oldList)) {
           carrierMappingRepository.batchDeleteByPrimaryKey(oldList);
        }
        initList.forEach(carrierMapping -> {
            carrierMapping.setCarrierId(null);
            carrierMapping.setTenantId(targetTenantId);
        });
        carrierMappingRepository.batchInsert(initList);
    }
}
