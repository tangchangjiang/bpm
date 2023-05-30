package org.o2.metadata.console.infra.constant;

import org.springframework.core.io.ClassPathResource;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * 网店常量
 *
 * @author yipeng.zhu@hand-china.com 2021-07-13
 **/
public interface OnlineShopConstants {

    interface ErrorCode {
        String ERROR_ONLINE_SHOP_CODE_NULL = "error.o2md.online_shop_code.is.null";
        String ERROR_ONLINE_SHOP_CODE_UNIQUE = "o2md.error.online_shop_code.is.not.unique";
        String ERROR_PLATFORM_ONLINE_SHOP_CODE_UNIQUE = "o2md.error.platform_online_shop_code.is.not.unique";
        String ERROR_ONLINE_SHOP_NAME_UNIQUE = "o2md.error.online_shop_name.is.not.unique";
        String ERROR_ONLINE_SHOP_CODE_UPDATE = "o2md.error.online_shop_code.is.forbidden.update";
        String ERROR_ONLINE_SHOP_DATA_VERSION_ERROR = "o2md.error.online_shop_data_version_error";
    }

    interface Language {
        String ZH_CN = "zh_CN";
        String EN_US = "en_US";
        String CATALOG_NAME = "catalogName";
        String CATALOG_VERSION_NAME = "catalogVersionName";
    }

    interface Redis {
        String UPDATE = "UPDATE";
        String DELETE = "DELETE";
        /**
         * 租户关联网店key set o2md:onlineShop:[tenantId]:index
         */
        String ONLINE_SHOP_KEY = "o2md:onlineShop:{%d}:index";
        /**
         * 租户关联网店key set o2md:onlineShop:[tenantId]:onlineShopCode
         */
        String ONLINE_SHOP_MULTI_KEY = "o2md:onlineShop:{%d}:%s";

        /**
         * 网店详情 hash o2md:onlineShop:detail
         */
        String ONLINE_SHOP_DETAIL_KEY = "o2md:onlineShop:detail";

        /**
         * Redis OnlineShopRelWarehouse
         * o2md:shopRelwh:[tenantId]:[shopCode]
         * key:value [warehouseCode:businessActiveFlag]
         * 对应数据库表：o2md_online_shop_rel_warehouse
         */
        String KEY_ONLINE_SHOP_REL_WAREHOUSE = "o2md:shopRelWh:{%d}:%s";

        /**
         * 获取key
         *
         * @param tenantId 租户id
         * @return key
         */
        static String getOnlineShopKey(Long tenantId) {
            return String.format(ONLINE_SHOP_KEY, tenantId);
        }

        /**
         * 获取key
         *
         * @param tenantId 租户id
         * @return key
         */
        static String getOnlineShopMutliKey(Long tenantId,String onlineShopCode) {
            return String.format(ONLINE_SHOP_MULTI_KEY, tenantId, onlineShopCode);
        }

        /**
         * 获取网店详情key
         *
         * @return 全部网店详情key
         */
        static String getOnlineShopDetailKey() {
            return ONLINE_SHOP_DETAIL_KEY;
        }

        /**
         * 获取key
         *
         * @param shopCode 网店编码
         * @param tenantId 租户ID
         * @return key
         */
        static String getOnlineShopRelWarehouseKey(String shopCode, Long tenantId) {
            return String.format(KEY_ONLINE_SHOP_REL_WAREHOUSE, tenantId, shopCode);
        }

        ResourceScriptSource UPDATE_CACHE_LUA =
                new ResourceScriptSource(new ClassPathResource("script/lua/onlineShop/batch_update_shopRelWh_redis.lua"));
        ResourceScriptSource DELETE_CACHE_LUA =
                new ResourceScriptSource(new ClassPathResource("script/lua/onlineShop/batch_delete_shopRelWh_redis.lua"));
    }
}
