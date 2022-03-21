package org.o2.metadata.console.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.console.app.service.StaticResourceTenantInitService;
import org.o2.metadata.console.infra.entity.StaticResourceConfig;
import org.o2.metadata.console.infra.repository.StaticResourceConfigRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

/**
 * 静态资源租户初始化
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/17 14:13
 */
@Service
@Slf4j
public class StaticResourceTenantInitServiceImpl implements StaticResourceTenantInitService {

    private final StaticResourceConfigRepository staticResourceConfigRepository;

    public StaticResourceTenantInitServiceImpl(StaticResourceConfigRepository staticResourceConfigRepository) {
        this.staticResourceConfigRepository = staticResourceConfigRepository;
    }


    /**
     * 租户初始化
     *
     * @param sourceTenantId 源租户Id
     * @param targetTenantId 租户Id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void tenantInitialize(long sourceTenantId, Long targetTenantId) {
        log.info("initializeStaticResourceConfig start, tenantId[{}]", targetTenantId);
        // 1. 查询平台租户（所有已启用）
        final List<StaticResourceConfig> platformStaticResourceConfigs = staticResourceConfigRepository.selectByCondition(Condition.builder(StaticResourceConfig.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(StaticResourceConfig.FIELD_TENANT_ID, sourceTenantId)
                        .andEqualTo(StaticResourceConfig.FIELD_ACTIVE_FLAG, BaseConstants.Flag.YES))
                .build());

        if (CollectionUtils.isEmpty(platformStaticResourceConfigs)) {
            log.info("staticResourceConfig is empty.");
            return;
        }

        // 2. 查询目标租户是否存在数据
        final List<StaticResourceConfig> targetResourceConfigs = staticResourceConfigRepository.selectByCondition(Condition.builder(StaticResourceConfig.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(StaticResourceConfig.FIELD_TENANT_ID, targetTenantId))
                .build());

        if (CollectionUtils.isNotEmpty(targetResourceConfigs)) {
            // 2.1 删除目标租户数据
            staticResourceConfigRepository.batchDeleteByPrimaryKey(targetResourceConfigs);
        }

        // 3. 插入平台数据到目标租户
        platformStaticResourceConfigs.forEach(staticResourceConfig -> {
            staticResourceConfig.setResourceConfigId(null);
            staticResourceConfig.setTenantId(targetTenantId);
        });
        staticResourceConfigRepository.batchInsert(platformStaticResourceConfigs);

        log.info("initializeStaticResourceConfig finish, tenantId[{}]", targetTenantId);
    }
}
