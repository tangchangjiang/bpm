package org.o2.metadata.console.app.service;

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
     * @param targetTenantId 租户Id
     */
    void tenantInitialize(Long targetTenantId);
}
