package org.o2.metadata.console.app.service;

import org.o2.metadata.console.app.bo.TenantInitBO;

/**
 * 网店多租户初始化
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/17 15:44
 */
public interface ShopTenantInitService {


    /**
     * 租户业务数据初始化
     * @param bo job参数
     */
    void tenantInitializeBusiness(TenantInitBO bo);

}
