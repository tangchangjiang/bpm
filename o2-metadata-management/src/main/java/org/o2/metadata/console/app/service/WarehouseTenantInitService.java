package org.o2.metadata.console.app.service;

import org.o2.initialize.domain.context.TenantInitContext;

/**
 * description
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2022/01/23 19:52
 */
public interface WarehouseTenantInitService {
    /**
     * 租户初始化
     *
     * @param context 参数
     */
    void tenantInitialize(TenantInitContext context);

    /**
     * 租户初始化业务数据
     * @param context 参数
     */
    void tenantInitializeBusiness(TenantInitContext context);
}
