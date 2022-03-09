package org.o2.metadata.console.app.service;

import org.o2.initialize.domain.context.TenantInitContext;

/**
 * 系统参数多租户初始化
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/17 13:52
 */
public interface SysParamTenantInitService {
    /**
     * 租户初始化
     *
     * @param context 租户
     */
    void tenantInitialize(TenantInitContext context);
}
