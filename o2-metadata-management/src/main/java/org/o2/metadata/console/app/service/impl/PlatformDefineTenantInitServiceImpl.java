package org.o2.metadata.console.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.console.app.service.PlatformDefineTenantInitService;
import org.o2.metadata.console.infra.entity.Platform;
import org.o2.metadata.console.infra.repository.PlatformRepository;
import org.springframework.stereotype.Service;


import java.util.Arrays;
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

    public PlatformDefineTenantInitServiceImpl(PlatformRepository platformRepository) {
        this.platformRepository = platformRepository;
    }

    @Override
    public void tenantInitialize(long sourceTenantId, Long targetTenantId) {
        log.info("initializePlatforms start");
        // 1. 查询平台租户（所有已启用）
        final List<Platform> platforms = platformRepository.selectByCondition(Condition.builder(Platform.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(Platform.FIELD_TENANT_ID, BaseConstants.DEFAULT_TENANT_ID)
                        .andIn(Platform.FIELD_PLATFORM_CODE, Arrays.asList("OW", "TM", "JD"))
                )
                .build());

        if (CollectionUtils.isEmpty(platforms)) {
            log.info("platforms is empty.");
            return;
        }

        // 2. 查询目标租户是否存在数据
        final List<Platform> targetPlatforms = platformRepository.selectByCondition(Condition.builder(Platform.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(Platform.FIELD_TENANT_ID, targetTenantId))
                .build());

        if (CollectionUtils.isNotEmpty(targetPlatforms)) {
            // 2.1 删除目标租户数据
            platformRepository.batchDeleteByPrimaryKey(targetPlatforms);
        }

        // 3. 插入平台数据到目标租户
        platforms.forEach(platform -> {
            platform.setPlatformId(null);
            platform.setTenantId(targetTenantId);
        });
        platformRepository.batchInsert(targetPlatforms);

        log.info("initializeMallLangPrompt finish, tenantId[{}]", targetTenantId);
    }
}
