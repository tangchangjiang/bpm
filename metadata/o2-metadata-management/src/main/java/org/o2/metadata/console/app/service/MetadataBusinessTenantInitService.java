package org.o2.metadata.console.app.service;

import org.o2.initialize.domain.context.TenantInitContext;

/**
 * 多租户业务数据初始化Service
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/17 10:44
 */
public interface MetadataBusinessTenantInitService {

    /**
     * 租户业务数据初始化
     * @param context job参数
     */
    void tenantInitializeBusiness(TenantInitContext context);
}
