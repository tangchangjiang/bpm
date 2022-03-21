package org.o2.metadata.pipeline.app.service;

import java.util.List;

/**
 * 流程器 租户初始化
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/18 13:06
 */
public interface PipelineTenantInitService {
    /**
     * 租户初始化
     *
     * @param sourceTenantId 源租户
     * @param targetTenantId 目标租户
     */
    void tenantInitialize(Long sourceTenantId, Long targetTenantId);
}
