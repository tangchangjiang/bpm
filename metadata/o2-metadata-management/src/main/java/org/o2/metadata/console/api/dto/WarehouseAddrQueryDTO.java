package org.o2.metadata.console.api.dto;

import lombok.Data;

/**
 *
 * 仓库地址
 *
 * @author yipeng.zhu@hand-china.com 2021-08-24
 **/
@Data
public class WarehouseAddrQueryDTO {

    /**
     * 仓库编码
     */
    private String warehouseCode;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 仓库状态
     */
    private String warehouseStatusCode;

    /**
     * 仓库类型
     */
    private String warehouseTypeCode;

    private Long tenantId;
}
