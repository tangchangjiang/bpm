package org.o2.metadata.console.app.service;

import org.o2.initialize.domain.context.TenantInitContext;

/**
 * 平台定义租户初始化
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/17 16:11
 */
public interface PlatformDefineTenantInitService {
    /**
     * 租户初始化
     *
     * @param context job参数
     */
    void tenantInitialize(TenantInitContext context);


    /**
     * 租户初始化
     *
     * @param context job参数
     */
    void tenantInitializeBusiness(TenantInitContext context);
}
