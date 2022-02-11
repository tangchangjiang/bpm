package org.o2.metadata.console.app.service;

/**
 * 网店多租户初始化
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/17 15:44
 */
public interface ShopTenantInitService {

    /**
     * 租户初始化
     *
     * @param sourceTenantId 源租户Id
     * @param targetTenantId 租户Id
     */
    void tenantInitialize(long sourceTenantId, Long targetTenantId);

    /**
     * 租户业务数据初始化
     * @param sourceTenantId 源租户Id
     * @param targetTenantId 租户Id
     */
    void tenantInitializeBusiness(long sourceTenantId, Long targetTenantId);
}
