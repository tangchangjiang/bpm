package org.o2.metadata.console.app.service;

/**
 *
 * job
 *
 * @author yipeng.zhu@hand-china.com 2021-08-12
 **/
public interface CacheJobService {
    /**
     * 仓库
     *
     * @param tenantId 租户ID
     */
    void refreshWarehouse(Long tenantId);

    /**
     * 网点关联仓库
     *
     * @param tenantId 租户ID
     */
    void  refreshOnlineShopRelWarehouse(Long tenantId);

    /**
     * 系统参数
     *
     * @param tenantId 租户ID
     */
     void refreshSysParameter(Long tenantId);


    /**
     * 承运商
     * @param tenantId 租户ID
     */
     void refreshCarrier(Long tenantId);

    /**
     * 运费模版
     * @param tenantId 租户ID
     */
     void refreshFreight(Long tenantId);
}
