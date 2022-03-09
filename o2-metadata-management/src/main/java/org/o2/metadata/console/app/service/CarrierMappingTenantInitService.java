package org.o2.metadata.console.app.service;

import org.o2.initialize.domain.context.TenantInitContext;

/**
 * 承运商匹配租户初始化
 *
 * @author yipeng.zhu@hand-china.com 2022-2-11
 */
public interface CarrierMappingTenantInitService {

    /**
     * 租户业务数据初始化
     * @param context job参数
     */
    void tenantInitializeBusiness(TenantInitContext context);
}
