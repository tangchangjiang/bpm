package org.o2.metadata.console.infra.constant;

import org.springframework.core.io.ClassPathResource;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 *
 * 仓库常量
 *
 * @author yipeng.zhu@hand-china.com 2021-07-13
 **/
public interface WarehouseConstants {

    /**
     * 仓库类型
     */
    interface WarehouseType {
        String LOV_CODE = "O2MD.WAREHOUSE_TYPE";
    }

    /**
     * 仓库状态
     */
    interface WarehouseStatus {

        String LOV_CODE = "O2MD.WAREHOUSE_STATUS";
    }
    /**
     * Redis Warehouse
     * o2md:warehouse:[tenantId]:[warehouseCode]
     * 对应数据库表：o2md_warehouse
     */
    interface WarehouseCache {
        String WAREHOUSE_INFO_KEY = "o2md:warehouse:{%d}:all";
        /**
         * 达到仓库
         */
        String EXPRESS_LIMIT_KEY = "o2md:warehouse:express:{%d}:limit";
        String PICK_UP_LIMIT_KEY = "o2md:warehouse:pick_up:{%d}:limit";
        String FLAG= "limitFlag";
        /**
         * 格式化的字符串
         *
         * @param tenantId      租户ID
         * @return the return
         * @throws RuntimeException exception description
         */
        static String warehouseCacheKey(final long tenantId) {
            return String.format(WAREHOUSE_INFO_KEY, tenantId);
        }

        /**
         * 格式化的字符串
         * @param key           limit
         * @param tenantId      租户ID
         * @return redis key
         */
        static String getLimitCacheKey(final String key, final long tenantId) {
            return String.format(key, tenantId);
        }


         ResourceScriptSource EXPRESS_LIMIT_CACHE_LUA =
                new ResourceScriptSource(new ClassPathResource("script/lua/warehouse/express_limit_cache.lua"));
         ResourceScriptSource PICK_UP_LIMIT_CACHE_LUA =
                new ResourceScriptSource(new ClassPathResource("script/lua/warehouse/pick_up_limit_cache.lua"));

         ResourceScriptSource UPDATE_WAREHOUSE_CACHE_LUA =
                new ResourceScriptSource(new ClassPathResource("script/lua/warehouse/update_warehouse_cache.lua"));

    }
}
