package org.o2.metadata.console.infra.constant;

import org.o2.data.redis.helper.ScriptHelper;
import org.springframework.data.redis.core.script.RedisScript;

/**
 *
 * 仓库常量
 *
 * @author yipeng.zhu@hand-china.com 2021-07-13
 **/
public interface WarehouseConstants {

    interface ErrorCode {
        /**
         * 仓库名称重复
         */
        String ERROR_WAREHOUSE_NAME_DUPLICATE = "error.warehouse_name.duplicate";
        /**
         * 一个门店服务点只能关联一个仓库，不能重复
         */
        String ERROR_WAREHOUSE_REL_POS_NOT_UNIQUE = "error.warehouse_rel_pos_not_unique";
        /**
         * 仓库编码重复
         */
        String ERROR_WAREHOUSE_CODE_DUPLICATE = "error.warehouse_code.duplicate";
    }

    /**
     * 仓库类型
     */
    interface WarehouseType {
        String LOV_CODE = "O2MD.WAREHOUSE_TYPE";

        /**
         * 良品仓
         */
        String GOOD = "GOOD";
    }

    /**
     * 仓库状态
     */
    interface WarehouseStatus {

        String LOV_CODE = "O2MD.WAREHOUSE_STATUS";

        /**
         * 正常
         */
        String NORMAL = "NORMAL";
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
        String FLAG = "limitFlag";

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

        RedisScript<Long> EXPRESS_LIMIT_CACHE_LUA =
                ScriptHelper.of("script/lua/warehouse/express_limit_cache.lua", Long.class);
        RedisScript<Long> PICK_UP_LIMIT_CACHE_LUA =
                ScriptHelper.of("script/lua/warehouse/pick_up_limit_cache.lua", Long.class);
        RedisScript<Long> UPDATE_WAREHOUSE_CACHE_LUA =
                ScriptHelper.of("script/lua/warehouse/update_warehouse_cache.lua", Long.class);

        /**
         * 格式化的字符串
         *
         * @param key      limit
         * @param tenantId 租户ID
         * @return redis key
         */
        static String getLimitCacheKey(final String key, final long tenantId) {
            return String.format(key, tenantId);
        }

    }

    interface WarehouseEventManagement {
        String O2SE_CONFIG_CACHE_UPDATE_EVT = "O2SE_CONFIG_CACHE_UPDATE_EVT";
        String WAREHOUSE_CACHE_NAME = "warehouseCacheImpl";
        String SHOP_REL_WAREHOUSE_CACHE_NAME = "shopRelWarehouseCacheImpl";
        String NEAR_REGION_CACHE_NAME = "nearRegionCacheImpl";
    }
}
