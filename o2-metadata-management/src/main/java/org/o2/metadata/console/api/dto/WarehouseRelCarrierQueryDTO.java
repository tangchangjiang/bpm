package org.o2.metadata.console.api.dto;

import lombok.Data;


/**
 *
 * 仓库
 *
 * @author yipeng.zhu@hand-china.com 2021-08-13
 **/
@Data
public class WarehouseRelCarrierQueryDTO {
    /**
     * 仓库编码
     */
    private String warehouseCode;

    /**
     * 承运商编码
     */
    private String carrierCode;

    /**
     * 承运商名称
     */
    private String carrierName;

    private Long tenantId;
}
