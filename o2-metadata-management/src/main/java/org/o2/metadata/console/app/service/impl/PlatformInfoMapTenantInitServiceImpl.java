package org.o2.metadata.console.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.console.app.service.PlatformInfoMapTenantInitService;
import org.o2.metadata.console.infra.constant.TenantInitConstants;
import org.o2.metadata.console.infra.entity.PlatformInfoMapping;
import org.o2.metadata.console.infra.repository.PlatformInfoMappingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;

/**
 * 平台信息匹配租户初始化
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/17 18:04
 */
@Slf4j
@Service
public class PlatformInfoMapTenantInitServiceImpl implements PlatformInfoMapTenantInitService {

    private final PlatformInfoMappingRepository platformInfoMappingRepository;

    public PlatformInfoMapTenantInitServiceImpl(PlatformInfoMappingRepository platformInfoMappingRepository) {
        this.platformInfoMappingRepository = platformInfoMappingRepository;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void tenantInitialize(Long sourceTenantId, Long targetTenantId) {
        log.info("initializePlatformInfoMapping start, tenantId[{}]", targetTenantId);
        // 1. 查询平台租户（默认OW-1）
        final List<PlatformInfoMapping> platformInfoMappings = platformInfoMappingRepository.selectByCondition(Condition.builder(PlatformInfoMapping.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(PlatformInfoMapping.FIELD_TENANT_ID, sourceTenantId)
                        .andEqualTo(PlatformInfoMapping.FIELD_PLATFORM_CODE, TenantInitConstants.PlatformInfoMappingBasis.PLATFORM_CODE)
                )
                .build());

        if (CollectionUtils.isEmpty(platformInfoMappings)) {
            log.info("platformInfoMappings is empty.");
            return;
        }

        // 2. 查询目标租户是否存在数据
        final List<PlatformInfoMapping> targetPlatformInfoMappings = platformInfoMappingRepository.selectByCondition(Condition.builder(PlatformInfoMapping.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(PlatformInfoMapping.FIELD_TENANT_ID, targetTenantId)
                        .andEqualTo(PlatformInfoMapping.FIELD_PLATFORM_CODE, TenantInitConstants.PlatformInfoMappingBasis.PLATFORM_CODE))
                .build());
        handleData(targetPlatformInfoMappings,platformInfoMappings,targetTenantId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void tenantInitializeBusiness(Long sourceTenantId, Long targetTenantId) {
        log.info("Business ：initializePlatformInfoMapping start, tenantId[{}]", targetTenantId);
        // 1. 查询平台租户（默认OW-1）
        final List<PlatformInfoMapping> platformInfoMappings = platformInfoMappingRepository.selectByCondition(Condition.builder(PlatformInfoMapping.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(PlatformInfoMapping.FIELD_TENANT_ID, sourceTenantId)
                        .andIn(PlatformInfoMapping.FIELD_PLATFORM_CODE, TenantInitConstants.PlatformInfoMappingBusiness.PLATFORM_CODE)
                )
                .build());

        if (CollectionUtils.isEmpty(platformInfoMappings)) {
            log.info("platformInfoMappings is empty.");
            return;
        }

        // 2. 查询目标租户是否存在数据
        final List<PlatformInfoMapping> targetPlatformInfoMappings = platformInfoMappingRepository.selectByCondition(Condition.builder(PlatformInfoMapping.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(PlatformInfoMapping.FIELD_TENANT_ID, targetTenantId)
                        .andIn(PlatformInfoMapping.FIELD_PLATFORM_CODE, TenantInitConstants.PlatformInfoMappingBusiness.PLATFORM_CODE))
                .build());
        handleData(targetPlatformInfoMappings,platformInfoMappings,targetTenantId);
        log.info("Business ：initializePlatformInfoMapping finish, tenantId[{}]", targetTenantId);
    }

    private void handleData(List<PlatformInfoMapping> oldList,List<PlatformInfoMapping> initList,Long targetTenantId) {
        List<PlatformInfoMapping> addList = new ArrayList<>(16);
        List<PlatformInfoMapping> updateList = new ArrayList<>(16);
        for (PlatformInfoMapping init : initList) {
            String initCode = init.getPlatformCode() + "-"+ init.getPlatformInfCode() +"-"+init.getInfCode() +"-" +init.getInfTypeCode();
            boolean addFlag = true;
            if (CollectionUtils.isEmpty(oldList)) {
               addList.add(init);
               continue;
            }
            for (PlatformInfoMapping old : oldList) {
                String oldCode = old.getPlatformCode() + "-"+ old.getPlatformInfCode() +"-"+old.getInfCode() +"-" +old.getInfTypeCode();;
                if (initCode.equals(oldCode)) {
                    init.setPlatformInfMappingId(old.getPlatformInfMappingId());
                    init.setTenantId(old.getTenantId());
                    init.setObjectVersionNumber(old.getObjectVersionNumber());
                    addFlag = false;
                    updateList.add(init);
                    break;
                }
            }
            if (addFlag) {
                addList.add(init);
            }
        }
        addList.forEach(platformInfoMapping -> {
            platformInfoMapping.setTenantId(targetTenantId);
            platformInfoMapping.setPlatformInfMappingId(null);
        });
        platformInfoMappingRepository.batchInsert(addList);
        platformInfoMappingRepository.batchUpdateByPrimaryKey(updateList);

    }
}
