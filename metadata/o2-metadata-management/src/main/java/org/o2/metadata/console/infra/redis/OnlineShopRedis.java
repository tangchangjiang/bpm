package org.o2.metadata.console.infra.redis;

import org.o2.metadata.console.infra.entity.OnlineShop;
import org.o2.metadata.console.infra.entity.OnlineShopRelWarehouse;
import org.o2.metadata.console.infra.entity.Warehouse;

import java.util.List;

/**
 * 网店
 *
 * @author yipeng.zhu@hand-china.com 2021-08-06
 **/
public interface OnlineShopRedis {
    /**
     * 更新网店
     *
     * @param onlineShopCode 网店编码
     * @param tenantId       租户ID
     */
    void updateRedis(String onlineShopCode, Long tenantId);

    /**
     * 更新网店关联仓库
     *
     * @param list       网店关联仓库数据
     * @param tenantId   租户ID
     * @param handleType 操作类型
     */
    void batchUpdateShopRelWh(List<OnlineShopRelWarehouse> list, Long tenantId, String handleType);

    /**
     * 批量更新
     *
     * @param list     网店
     * @param tenantId 租户ID
     */
    void batchUpdateRedis(List<OnlineShop> list, Long tenantId);

    /**
     * 同步店铺信息（同步网店、仓库和网店关联仓库；因服务点没有维护地址和经纬度，不需要同步至Redis）
     *
     * @param onlineShop 网店信息
     * @param warehouse  仓库信息
     * @param shopRelWh  网店关联仓库信息
     */
    void syncMerchantMetaInfo(OnlineShop onlineShop, Warehouse warehouse, OnlineShopRelWarehouse shopRelWh);

    void insertMultiShop(OnlineShop onlineShop);
}
