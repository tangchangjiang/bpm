package org.o2.metadata.console.infra.constant;

import java.util.Collection;
import java.util.HashSet;

/**
 *
 * 网店常量
 *
 * @author yipeng.zhu@hand-china.com 2021-07-13
 **/
public interface OnlineShopConstants {
    /**
     * Redis OnlineShopRelWarehouse
     * o2md:shopRelwh:[tenantId]:[shopCode]
     * key:value [warehouseCode:businessActiveFlag]
     * 对应数据库表：o2md_online_shop_rel_warehouse
     */
    interface OnlineShopRelWarehouse {
        String KEY_ONLINE_SHOP_REL_WAREHOUSE = "o2md:shopRelwh:%d:{%s}";
        String FIELD_WAREHOUSE_CODE = "warehouseCode";
        Collection<String> HASH_KEYS = new HashSet<String>() {{
            add(FIELD_WAREHOUSE_CODE);
        }};
    }

    interface  Redis {
        /**
         *  o2md:onlineShop:[tenantId]:[shopCode]
         */
        String ONLINE_SHOP_KEY = "o2md:onlineShop:{%s}";
        
        /**
         *  获取key
         * @param shopCode 网店编码
         * @return key
         */
        static String getOnlineShopKey(String shopCode) {
            return String.format(ONLINE_SHOP_KEY,shopCode);
        }
    }
}
