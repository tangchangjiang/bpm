package org.o2.metadata.console.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 *
 * 网店关联仓库DTO
 *
 * @author yipeng.zhu@hand-china.com 2021-12-08
 **/
@Data
public class OnlineShopRelWarehouseDTO {
    /**
     * 仓库编码
     */
    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    /**
     * 仓库名称
     */
    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    /**
     * 网店名称
     */
    @ApiModelProperty(value = "网店名称")
    private String onlineShopCode;

    /**
     * 租户ID
     */
    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
}
