package org.o2.metadata.console.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.console.app.service.PlatformInfoMapTenantInitService;
import org.o2.metadata.console.infra.entity.PlatformInfoMapping;
import org.o2.metadata.console.infra.repository.PlatformInfoMappingRepository;
import org.springframework.stereotype.Service;


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
    public void tenantInitialize(Long targetTenantId) {
        log.info("initializePlatformInfoMapping start, tenantId[{}]", targetTenantId);
        // 1. 查询平台租户（默认OW-1）
        final List<PlatformInfoMapping> platformInfoMappings = platformInfoMappingRepository.selectByCondition(Condition.builder(PlatformInfoMapping.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(PlatformInfoMapping.FIELD_TENANT_ID, BaseConstants.DEFAULT_TENANT_ID)
                        .andEqualTo(PlatformInfoMapping.FIELD_PLATFORM_CODE, "TM")
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
                        .andEqualTo(PlatformInfoMapping.FIELD_PLATFORM_CODE, "TM"))
                .build());

        if (CollectionUtils.isNotEmpty(targetPlatformInfoMappings)) {
            // 2.1 删除目标租户数据
            platformInfoMappingRepository.batchDeleteByPrimaryKey(targetPlatformInfoMappings);
        }

        // 3. 插入平台数据到目标租户
        platformInfoMappings.forEach(platformInfoMapping -> {
            platformInfoMapping.setPlatformInfMappingId(null);
            platformInfoMapping.setTenantId(targetTenantId);
        });
        platformInfoMappingRepository.batchInsert(platformInfoMappings);

        log.info("initializePlatformInfoMapping finish, tenantId[{}]", targetTenantId);
    }
}
