package org.o2.metadata.console.app.service;

import org.o2.initialize.domain.context.TenantInitContext;

/**
 * 静态资源租户初始化
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/17 14:12
 */
public interface StaticResourceTenantInitService {
    /**
     * 租户初始化
     *
     * @param context 租户
     */
    void tenantInitialize(TenantInitContext context);
}
