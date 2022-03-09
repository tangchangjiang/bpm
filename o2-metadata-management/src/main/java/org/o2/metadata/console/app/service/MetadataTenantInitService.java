package org.o2.metadata.console.app.service;

import org.o2.initialize.domain.context.TenantInitContext;

/**
 * 多租户初始化Service
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/17 10:44
 */
public interface MetadataTenantInitService {
    /**
     * 租户初始化
     *
     * @param context 参数
     */
    void tenantInitialize(TenantInitContext context);

    /**
     * 租户业务数据初始化
     * @param context 参数
     */
    void tenantInitializeBusiness(TenantInitContext context);
}
