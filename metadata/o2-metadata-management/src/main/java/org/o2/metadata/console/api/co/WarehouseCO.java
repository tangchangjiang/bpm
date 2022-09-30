package org.o2.metadata.console.api.co;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.o2.metadata.console.infra.constant.WarehouseConstants;

import java.util.Date;

/**
 * 仓库
 *
 * @author yuying.shi@hand-china.com 2020-03-02
 */
@Data
@ApiModel("仓库表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WarehouseCO {

    @ApiModelProperty("表ID，主键，供其他表做外键")
    private Long warehouseId;

    @ApiModelProperty(value = "服务点id，关联到 o2md_pos.pos_id")
    private Long posId;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "仓库状态,值集：O2MD.WAREHOUSE_STATUS")
    @LovValue(lovCode = WarehouseConstants.WarehouseStatus.LOV_CODE)
    private String warehouseStatusCode;
    private String warehouseStatusMeaning;

    @ApiModelProperty(value = "仓库类型,值集: O2MD.WAREHOUSE_TYPE （良品仓/不良品仓/退货仓）")
    @LovValue(lovCode = WarehouseConstants.WarehouseType.LOV_CODE)
    private String warehouseTypeCode;
    private String warehouseTypeMeaning;

    @ApiModelProperty(value = "自提发货接单量")
    private Long pickUpQuantity;

    @ApiModelProperty(value = "配送发货接单量")
    private Long expressedQuantity;

    @ApiModelProperty("仓库自提标识")
    private Integer pickedUpFlag;

    @ApiModelProperty("仓库快递发货")
    private Integer expressedFlag;

    @ApiModelProperty(value = "仓库评分")
    private Long score;

    @ApiModelProperty(value = "有效日期从")
    private Date activedDateFrom;

    @ApiModelProperty(value = "有效日期从")
    private Date activedDateTo;

    @ApiModelProperty(value = "库存组织编码(物权归属)")
    private String invOrganizationCode;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "生效状态")
    private Integer activeFlag;

    @ApiModelProperty(value = "仓库到店退标示")
    private Integer storeReturnFlag;


    private String posCode;

    private String posName;

    private String expressLimitValue;

    private String pickUpLimitValue;

    private String onlineShopCode;

}
