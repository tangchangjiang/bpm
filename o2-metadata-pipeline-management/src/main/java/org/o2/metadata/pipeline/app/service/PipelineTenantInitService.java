package org.o2.metadata.pipeline.app.service;

import org.o2.initialize.domain.context.TenantInitContext;

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
     * @param context 参数
     */
    void tenantInitialize(TenantInitContext context);
}
