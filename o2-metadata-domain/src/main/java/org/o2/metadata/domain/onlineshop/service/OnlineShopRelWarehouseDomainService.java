package org.o2.metadata.domain.onlineshop.service;

import org.o2.metadata.domain.onlineshop.domain.OnlineShopRelWarehouseDO;

import java.util.List;

/**
 *
 * 网店关联仓库
 *
 * @author yipeng.zhu@hand-china.com 2021-07-13
 **/
public interface OnlineShopRelWarehouseDomainService {
    /**
     * 查询网店关联库存
     * @param onlineShopCode 网店编码
     * @param tenantId 租户ID
     * @return list
     */
    List<OnlineShopRelWarehouseDO> listOnlineShopRelWarehouses(String onlineShopCode, Long tenantId);
}
