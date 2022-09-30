package org.o2.metadata.console.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.core.O2CoreConstants;
import org.o2.metadata.console.app.service.PlatformDefineTenantInitService;
import org.o2.metadata.console.infra.entity.Platform;
import org.o2.metadata.console.infra.entity.PlatformInfoMapping;
import org.o2.metadata.console.infra.repository.PlatformInfoMappingRepository;
import org.o2.metadata.console.infra.repository.PlatformRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

/**
 * description
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/17 16:12
 */
@Slf4j
@Service
public class PlatformDefineTenantInitServiceImpl implements PlatformDefineTenantInitService {

    private final PlatformRepository platformRepository;

    private final PlatformInfoMappingRepository platformInfoMappingRepository;


    public PlatformDefineTenantInitServiceImpl(PlatformRepository platformRepository, PlatformInfoMappingRepository platformInfoMappingRepository) {
        this.platformRepository = platformRepository;
        this.platformInfoMappingRepository = platformInfoMappingRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void tenantInitialize(Long sourceTenantId, Long targetTenantId) {
        log.info("initializePlatforms start");
        // 1. 查询平台定义租户（所有已启用）
        final List<Platform> sourcePlatforms = platformRepository.selectByCondition(Condition.builder(Platform.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(Platform.FIELD_TENANT_ID, sourceTenantId)
                        .andIn(Platform.FIELD_PLATFORM_CODE, O2CoreConstants.PlatformFrom.PLATFORM_FROM_LIST)
                )
                .build());

        if (CollectionUtils.isEmpty(sourcePlatforms)) {
            log.info("platforms is empty.");
            return;
        }
        // 2. 查询目标租户是否存在数据
        final List<Platform> targetPlatforms = platformRepository.selectByCondition(Condition.builder(Platform.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(Platform.FIELD_TENANT_ID, targetTenantId)
                .andIn(Platform.FIELD_PLATFORM_CODE, O2CoreConstants.PlatformFrom.PLATFORM_FROM_LIST))
                .build());

        // 删除目标租户存在的数据
        platformRepository.batchDeleteByPrimaryKey(targetPlatforms);
        // 新增源租户数据 到目标租户数据
        for (Platform sourcePlatform : sourcePlatforms) {
            sourcePlatform.setTenantId(targetTenantId);
            sourcePlatform.setPlatformId(null);
        }
        platformRepository.batchInsert(sourcePlatforms);

        // 1. 查询源租户平台信息匹配
        final List<PlatformInfoMapping> sourcePlatformInfoMappings = platformInfoMappingRepository.selectByCondition(Condition.builder(PlatformInfoMapping.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(PlatformInfoMapping.FIELD_TENANT_ID, sourceTenantId)
                        .andIn(PlatformInfoMapping.FIELD_PLATFORM_CODE, O2CoreConstants.PlatformFrom.PLATFORM_FROM_LIST)
                )
                .build());

        if (CollectionUtils.isEmpty(sourcePlatformInfoMappings)) {
            log.info("platformInfoMappings is empty.");
            return;
        }

        // 2. 查询目标租户平台信息匹配
        final List<PlatformInfoMapping> targetPlatformInfoMappings = platformInfoMappingRepository.selectByCondition(Condition.builder(PlatformInfoMapping.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(PlatformInfoMapping.FIELD_TENANT_ID, targetTenantId)
                        .andIn(PlatformInfoMapping.FIELD_PLATFORM_CODE, O2CoreConstants.PlatformFrom.PLATFORM_FROM_LIST))
                .build());
        platformInfoMappingRepository.batchDeleteByPrimaryKey(targetPlatformInfoMappings);
        for (PlatformInfoMapping sourcePlatformInfoMapping : sourcePlatformInfoMappings) {
            sourcePlatformInfoMapping.setPlatformInfMappingId(null);
            sourcePlatformInfoMapping.setTenantId(targetTenantId);
        }
        platformInfoMappingRepository.batchInsert(sourcePlatformInfoMappings);
        log.info("initializePlatforms finish, tenantId[{}]", targetTenantId);
    }




}
