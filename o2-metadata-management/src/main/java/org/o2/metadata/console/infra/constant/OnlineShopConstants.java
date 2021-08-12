package org.o2.metadata.console.infra.constant;


import org.springframework.core.io.ClassPathResource;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 *
 * 网店常量
 *
 * @author yipeng.zhu@hand-china.com 2021-07-13
 **/
public interface OnlineShopConstants {

    interface  Redis {
        String UPDATE = "UPDATE";
        String DELETE = "DELETE";
        /**
         *  o2md:onlineShop:[tenantId]:[shopCode]
         */
        String ONLINE_SHOP_KEY = "o2md:onlineShop:{%s}";
        /**
         * Redis OnlineShopRelWarehouse
         * o2md:shopRelwh:[tenantId]:[shopCode]
         * key:value [warehouseCode:businessActiveFlag]
         * 对应数据库表：o2md_online_shop_rel_warehouse
         */
        String KEY_ONLINE_SHOP_REL_WAREHOUSE = "o2md:shopRelWh:%d:{%s}";
        
        /**
         *  获取key
         * @param shopCode 网店编码
         * @return key
         */
        static String getOnlineShopKey(String shopCode) {
            return String.format(ONLINE_SHOP_KEY,shopCode);
        }

        /**
         *  获取key
         * @param shopCode 网店编码
         * @param tenantId 租户ID
         * @return key
         */
        static String getOnlineShopRelWarehouseKey(String shopCode,Long tenantId) {
            return String.format(KEY_ONLINE_SHOP_REL_WAREHOUSE,tenantId,shopCode);
        }
        ResourceScriptSource UPDATE_CACHE_LUA =
                new ResourceScriptSource(new ClassPathResource("script/lua/onlineShop/batch_update_shopRelWh_redis.lua"));
        ResourceScriptSource DELETE_CACHE_LUA =
                new ResourceScriptSource(new ClassPathResource("script/lua/onlineShop/batch_delete_shopRelWh_redis.lua"));
    }
}
