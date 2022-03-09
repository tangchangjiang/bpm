package org.o2.metadata.console.app.service;

import org.o2.initialize.domain.context.TenantInitContext;

/**
 * description
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/17 18:04
 */
public interface PlatformInfoMapTenantInitService {
    /**
     * 租户初始化
     *
     * @param context job参数
     */
    void tenantInitialize(TenantInitContext context);

    /**
     * 租户业务数据初始化
     * @param context job参数
     */
    void tenantInitializeBusiness(TenantInitContext context);
}
