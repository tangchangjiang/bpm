package org.o2.metadata.console.infra.constant;

import java.util.Arrays;
import java.util.List;

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
     * 网店业务数据
     */
    interface InitOnlineShopBusiness {
        List<String> ONLINE_SHOP_CODE = Arrays.asList("TM-1","JD-1","OW-2","OW-1");
    }
    /**
     * 目录基础数据
     */
    interface InitOnlineShopBasis {
        List<String> ONLINE_SHOP_CODE = Arrays.asList("OW-1");
    }

    /**
     * 网店业务数据
     */
    interface InitWarehouseBusiness {
        List<String> warehouses = Arrays.asList("VIRTUAL_POS","SH001","SH002","BJ001");
    }

    /**
     * 网店业务数据
     */
    interface InitPosBusiness {
        List<String> POS_CODE= Arrays.asList("SH001","BJ001");
    }
    /**
     * 网店关联仓库业务数据
     */
    interface OnlineShopRelHouseBusiness {
        List<String> onlineShops = Arrays.asList("TM-1","JD-1","OW-1","OW-2");
    }
    /**
     * 承运商业务数据
     */
    interface CarrierBusiness {
        List<String> CARRIERS = Arrays.asList("STO","YTO","SF","EMS","JD");
    }

    /**
     * 承运商匹配业务数据
     */
    interface CarrierMappingBusiness {
        List<String> PLATFORM_CODES = Arrays.asList("OW","JD","TM");
    }

    /**
     * 目录基础数据
     */
    interface CatalogBasis {
        List<String> CATALOG_CODE = Arrays.asList("MASTER", "OW");
    }
    /**
     * 目录基础数据
     */
    interface CatalogBusiness {
        List<String> CATALOG_CODE = Arrays.asList("JD","OW","TM");
    }

    /**
     * 运费业务数据
     */
    interface FreightBusiness {
        String FREIGHT_CODE = "FREE";
    }

    /**
     * 平台定义数据
     */
    interface PlatformBasis {
        List<String> PLATFORM_CODE = Arrays.asList("JD","OW","TM");
    }
    /**
     * 平台定义业务数据
     */
    interface PlatformBusiness {
        List<String> PLATFORM_CODE = Arrays.asList("JD","OW","TM","KDN");
    }

    /**
     * 平台定义匹配数据
     */
    interface PlatformInfoMappingBasis {
        List<String> PLATFORM_CODE = Arrays.asList("JD", "OW", "TM");
    }

    /**
     * 平台定义匹配业务数据
     */
    interface PlatformInfoMappingBusiness {
        List<String> PLATFORM_CODE = Arrays.asList("JD", "OW", "TM", "KDN");
    }

    /**
     * 仓库基础数据
     */
    interface WarehouseBasis {
        String VIRTUAL_WAREHOUSE = "VIRTUAL_POS";
        long  POS_ID = 1L;
    }
}
