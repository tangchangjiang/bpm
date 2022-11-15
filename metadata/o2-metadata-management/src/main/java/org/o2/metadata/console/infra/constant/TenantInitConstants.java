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
     * 服务点
     */
    interface InitPosBusiness {
        List<String> POS_CODE = Arrays.asList("SH001", "BJ001");
    }

    /**
     * 目录版本
     */
    interface InitCatalog {
         String MASTER = "MASTER";
    }

    /**
     * 运费业务数据
     */
    interface FreightBusiness {
        String FREIGHT_CODE = "FREE";
    }

    /**
     * 仓库基础数据
     */
    interface WarehouseBasis {
        String VIRTUAL_WAREHOUSE = "VIRTUAL_POS";
        long  POS_ID = 1L;
    }
}
