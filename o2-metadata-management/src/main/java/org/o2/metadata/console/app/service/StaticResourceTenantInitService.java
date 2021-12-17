package org.o2.metadata.console.app.service;

/**
 * 静态资源租户初始化
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/17 14:12
 */
public interface StaticResourceTenantInitService {
    /**
     * 租户初始化
     *
     * @param targetTenantId 租户Id
     */
    void tenantInitialize(Long targetTenantId);
}
