package org.o2.metadata.console.infra.constant;

/**
 *
 * 初始化数据常量
 * @author yipeng.zhu@hand-china.com 2022-02-09
 **/
public interface TenantInitConstants {

    interface InitBaseParam {

        String BASE_ONLINE_SHOP = "base_online_shop";
        String BASE_WAREHOUSES = "base_warehouses";
        String BASE_POS = "base_pos";
        String BASE_CATALOG = "base_catalog";
        String BASE_PLATFORM_MAPPING = "base_platform_mapping";
        String BASE_WAREHOUSE = "base_warehouse";
        String BASE_PLATFORM = "base_platform";
    }

    interface InitBusinessParam {
        String BUSINESS_WAREHOUSE = "business_warehouse";
        String BUSINESS_ONLINE_SHOP = "business_online_shop";
        String BUSINESS_ONLINE_SHOPS = "business_online_shops";
        String BUSINESS_SHOP_REL_HOUSE = "business_shop_rel_house";
        String BUSINESS_CARRIER = "business_carrier";
        String BUSINESS_CARRIER_MAPPING = "business_carrier_mapping";
        String BUSINESS_CATALOG = "business_catalog";
        String BUSINESS_FREIGHT = "business_freight";
        String BUSINESS_PLATFORM = "business_platform";
        String BUSINESS_PLATFORM_MAPPING = "business_platform_mapping";
        String BUSINESS_POS = "business_pos";
    }

    /**
     * 仓库基础数据
     */
    interface WarehouseBasis {
        long  POS_ID = 1L;
    }
}
