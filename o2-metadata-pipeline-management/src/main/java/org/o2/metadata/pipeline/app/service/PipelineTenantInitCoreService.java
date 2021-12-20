package org.o2.metadata.pipeline.app.service;

/**
 * 流程器租户初始化
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/18 13:25
 */
public interface PipelineTenantInitCoreService {
    /**
     * 租户初始化
     *
     * @param tenantId 租户
     */
    void tenantInitialize(Long tenantId);
}
