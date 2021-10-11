package org.o2.metadata.infra.constants;

/**
 *
 * 仓库常量
 *
 * @author yipeng.zhu@hand-china.com 2021-07-13
 **/
public interface WarehouseConstants {
    /**
     * Redis Warehouse
     * o2md:warehouse:[tenantId]:[warehouseCode]
     * 对应数据库表：o2md_warehouse
     */
    interface WarehouseCache {
        String WAREHOUSE_INFO_KEY = "o2md:warehouse:%d:{%s}";
        String EXPRESS_LIMIT_COLLECTION = "o2md:warehouse:express:{%d}:limit";
        String PICK_UP_LIMIT_COLLECTION = "o2md:warehouse:pick_up:{%d}:limit";
        String POS_CODE = "posCode";
        String WAREHOUSE_STATUS_CODE= "warehouseStatusCode";
        String WAREHOUSE_TYPE_CODE = "warehouseTypeCode";
        String PICKUP_FLAG = "pickedUpFlag";
        String EXPRESSED_FLAG = "expressedFlag";
        String SCORE = "score";
        String ACTIVE_DATE_FROM = "activedDateFrom";
        String ACTIVE_DATE_TO = "activedDateTo";
        String INV_ORGANIZATION_CODE = "invOrganizationCode";
        String EXPRESS_LIMIT_QUANTITY= "express_limit_quantity";
        String EXPRESS_LIMIT_VALUE = "express_limit_value";
        String PICK_UP_LIMIT_QUANTITY = "pick_up_limit_quantity";
        String PICK_UP_LIMIT_VALUE = "pick_up_limit_value";

        /**
         * 格式化的字符串
         *
         * @param tenantId      租户ID
         * @param warehouseCode 仓库编码
         * @return the return
         * @throws RuntimeException exception description
         */
        static String warehouseCacheKey(final long tenantId, final String warehouseCode) {
            return String.format(WAREHOUSE_INFO_KEY, tenantId, warehouseCode);
        }

        /**
         * 格式化的字符串
         * @param limit           limit
         * @param tenantId      租户ID
         * @return
         */
        static String warehouseLimitCacheKey(final String limit, final long tenantId) {
            return String.format(limit, tenantId);
        }
    }
}
