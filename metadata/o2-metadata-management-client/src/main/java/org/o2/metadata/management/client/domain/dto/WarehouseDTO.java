package org.o2.metadata.management.client.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Map;

/**
 * 库存DTO
 *
 * @author miao.chen01@hand-china.com 2022-12-16
 */
@Data
public class WarehouseDTO {
    @ApiModelProperty("表ID，主键，供其他表做外键")
    private Long warehouseId;

    @ApiModelProperty(value = "服务点id，关联到 o2md_pos.pos_id")
    @NotNull
    private Long posId;

    @ApiModelProperty(value = "仓库编码")
    @NotBlank
    @Size(max = 255)
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    @NotBlank
    @Size(max = 255)
    private String warehouseName;

    @ApiModelProperty(value = "仓库状态,值集：O2MD.WAREHOUSE_STATUS")
    @NotBlank
    @Size(max = 255)
    private String warehouseStatusCode;

    @ApiModelProperty(value = "仓库类型,值集: O2MD.WAREHOUSE_TYPE （良品仓/不良品仓/退货仓）")
    @NotBlank
    @Size(max = 255)
    private String warehouseTypeCode;

    @ApiModelProperty(value = "自提发货接单量")
    private Long pickUpQuantity;

    @ApiModelProperty(value = "配送发货接单量")
    private Long expressedQuantity;

    @ApiModelProperty("仓库自提标识")
    @NotNull
    @Max(1)
    @Min(0)
    private Integer pickedUpFlag;

    @ApiModelProperty("仓库快递发货")
    @NotNull
    @Max(1)
    @Min(0)
    private Integer expressedFlag;

    @ApiModelProperty(value = "仓库评分")
    private Long score;

    @ApiModelProperty(value = "有效日期从")
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date activedDateFrom;

    @ApiModelProperty(value = "有效日期从")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date activedDateTo;

    @ApiModelProperty(value = "库存组织编码(物权归属)")
    @Size(max = 18)
    private String invOrganizationCode;

    @ApiModelProperty(value = "租户ID")
    @NotNull
    private Long tenantId;

    @ApiModelProperty(value = "生效状态")
    @NotNull
    private Integer activeFlag;

    @ApiModelProperty(value = "仓库到店退标示")
    private Integer storeReturnFlag;

    @ApiModelProperty(value = "外部仓储编码")
    private String wmsWarehouseCode;
    @ApiModelProperty(value = "仓库多语言")
    private Map<String,String> warehouseNameTls;
}
