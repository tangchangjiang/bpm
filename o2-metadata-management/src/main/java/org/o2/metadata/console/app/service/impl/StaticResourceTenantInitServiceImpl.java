package org.o2.metadata.console.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.initialize.domain.context.TenantInitContext;
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
     * @param context 租户
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void tenantInitialize(TenantInitContext context) {
        log.info("initializeStaticResourceConfig start, tenantId[{}]", context.getTargetTenantId());
        // 1. 查询平台租户（所有已启用）
        final List<StaticResourceConfig> platformStaticResourceConfigs = staticResourceConfigRepository.selectByCondition(Condition.builder(StaticResourceConfig.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(StaticResourceConfig.FIELD_TENANT_ID, context.getSourceTenantId())
                        .andEqualTo(StaticResourceConfig.FIELD_ACTIVE_FLAG, BaseConstants.Flag.YES))
                .build());

        if (CollectionUtils.isEmpty(platformStaticResourceConfigs)) {
            log.info("staticResourceConfig is empty.");
            return;
        }

        // 2. 查询目标租户是否存在数据
        final List<StaticResourceConfig> targetResourceConfigs = staticResourceConfigRepository.selectByCondition(Condition.builder(StaticResourceConfig.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(StaticResourceConfig.FIELD_TENANT_ID, context.getTargetTenantId()))
                .build());

        if (CollectionUtils.isNotEmpty(targetResourceConfigs)) {
            // 2.1 删除目标租户数据
            staticResourceConfigRepository.batchDeleteByPrimaryKey(targetResourceConfigs);
        }

        // 3. 插入平台数据到目标租户
        platformStaticResourceConfigs.forEach(staticResourceConfig -> {
            staticResourceConfig.setResourceConfigId(null);
            staticResourceConfig.setTenantId(context.getTargetTenantId());
        });
        staticResourceConfigRepository.batchInsert(platformStaticResourceConfigs);

        log.info("initializeStaticResourceConfig finish, tenantId[{}]", context.getTargetTenantId());
    }
}
