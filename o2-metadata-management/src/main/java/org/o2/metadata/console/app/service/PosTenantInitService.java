package org.o2.metadata.console.app.service;

/**
 * 服务点数据初始化
 *
 * @author tingting.wang@hand-china.com 2022-02-10
 */
public interface PosTenantInitService {

    /**
     * 租户业务数据初始化
     * @param sourceTenantId 源租户
     * @param targetTenantId  目标库租户
     */
    void tenantInitializeBusiness(long sourceTenantId, Long targetTenantId);
}
