package org.o2.metadata.console.app.service;

/**
 * 运费租户初始化
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/17 16:43
 */
public interface FreightTenantInitService {

    /**
     * 租户业务数据初始化
     * @param sourceTenantId 源租户
     * @param targetTenantId  目标库租户
     */
    void tenantInitializeBusiness(long sourceTenantId, Long targetTenantId);
}
