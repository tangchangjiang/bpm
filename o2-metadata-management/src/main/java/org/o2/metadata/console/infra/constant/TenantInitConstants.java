package org.o2.metadata.console.infra.constant;

import java.util.Arrays;
import java.util.List;

/**
 *
 * 初始化数据常量
 * @author yipeng.zhu@hand-china.com 2022-02-09
 **/
public interface TenantInitConstants {
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
