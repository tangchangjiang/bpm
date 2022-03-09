package org.o2.metadata.console.app.service;

import org.o2.initialize.domain.context.TenantInitContext;

/**
 * 运费租户初始化
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/17 16:43
 */
public interface FreightTenantInitService {

    /**
     * 租户业务数据初始化
     * @param context job参数
     */
    void tenantInitializeBusiness(TenantInitContext context);
}
