package org.o2.metadata.console.app.service;

import org.o2.initialize.domain.context.TenantInitContext;

/**
 * 网店关联服务点数据初始化
 *
 * @author tingting.wang@hand-china.com 2022-02-10
 */
public interface OnlineShopRelHouseTenantInitService {

    /**
     * 租户业务数据初始化
     * @param context job参数
     */
    void tenantInitializeBusiness(TenantInitContext context);
}
