package org.o2.metadata.console.infra.redis;

import org.o2.metadata.console.api.co.OnlineShopRelWarehouseCO;
import org.o2.metadata.console.infra.entity.OnlineShopRelWarehouse;

import java.util.List;

/**
 * 网店关联仓库
 *
 * @author yipeng.zhu@hand-china.com 2021-07-13
 **/
public interface OnlineShopRelWarehouseRedis {
    /**
     * 查询网店关联库存
     *
     * @param onlineShopCode 网店编码
     * @param tenantId       租户ID
     * @return list
     */
    List<OnlineShopRelWarehouse> listOnlineShopRelWarehouses(String onlineShopCode, Long tenantId);

    /**
     * 批量查询网店关联库存
     *
     * @param onlineShopCode 网店编码
     * @param tenantId       租户ID
     * @return list
     */
    List<OnlineShopRelWarehouseCO> listOnlineShopRelWarehouses(List<String> onlineShopCode, Long tenantId);
}
