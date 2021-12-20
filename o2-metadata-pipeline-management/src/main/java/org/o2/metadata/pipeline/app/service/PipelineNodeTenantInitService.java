package org.o2.metadata.pipeline.app.service;

/**
 * description
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/20 11:10
 */
public interface PipelineNodeTenantInitService {
    /**
     * 租户初始化
     *
     * @param sourceTenantId 源租户Id
     * @param tenantId       租户
     */
    void tenantInitialize(long sourceTenantId, Long tenantId);
}
