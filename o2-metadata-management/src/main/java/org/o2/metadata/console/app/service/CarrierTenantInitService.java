package org.o2.metadata.console.app.service;

/**
 * 承运商租户初始化
 *
 * @author yipeng.zhu@hand-china.com 2022-2-11
 */
public interface CarrierTenantInitService {
    /**
     * 租户业务数据初始化
     * @param sourceTenantId 源租户
     * @param targetTenantId  目标库租户
     */
    void tenantInitializeBusiness(long sourceTenantId, Long targetTenantId);
}
