package org.o2.metadata.console.app.bo;

import lombok.Data;

/**
 * 多租户job参数
 * @author peng.xu@hand-china.com 2022-03-15
 */
@Data
public class TenantInitBO {
    /**
     * 仓库编码
      */
    private String warehouseCode;
    /**
     * 网店编码
     */
    private String onlineShopCode;
    /**
     * 承运商编码
     */
    private String carrierCode;
    /**
     * 来源租户ID
     */
    private Long sourceTenantId;
    /**
     * 目标租户ID
     */
    private Long targetTenantId;
}
