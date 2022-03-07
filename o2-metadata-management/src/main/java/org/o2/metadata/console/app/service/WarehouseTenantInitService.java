package org.o2.metadata.console.app.service;

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
     * @param sourceTenantId 源租户Id
     * @param targetTenantId 租户Id
     */
    void tenantInitialize(long sourceTenantId, Long targetTenantId);

    /**
     * 租户初始化业务数据
     * @param sourceTenantId 源租户Id
     * @param targetTenantId 租户Id
     */
    void tenantInitializeBusiness(long sourceTenantId, Long targetTenantId);
}
