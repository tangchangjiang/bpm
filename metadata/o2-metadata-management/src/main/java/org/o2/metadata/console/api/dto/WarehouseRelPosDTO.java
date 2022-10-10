package org.o2.metadata.console.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 仓库关联服务点
 * @author zhilin.ren@hand-china.com 2021/11/26 15:10
 */
@Data
public class WarehouseRelPosDTO {

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
     * 租户ID
     */
    @ApiModelProperty(value = "租户ID")
    private Long tenantId;


}