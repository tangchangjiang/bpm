package org.o2.metadata.console.app.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 *
 * 仓库缓存
 *
 * @author yipeng.zhu@hand-china.com 2021-08-13
 **/
@Data
public class WarehouseCacheBO {





    @ApiModelProperty(value = "仓库编码")

    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")

    private String warehouseName;



    @ApiModelProperty(value = "仓库类型,值集: O2MD.WAREHOUSE_TYPE （良品仓/不良品仓/退货仓）")
    private String warehouseTypeCode;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date activedDateFrom;

    @ApiModelProperty(value = "有效日期从")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date activedDateTo;

    @ApiModelProperty(value = "生效状态")
    private Integer activeFlag;

    private String posCode;
    private String expressLimitValue;
    private String pickUpLimitValue;
    private String warehouseStatusCode;

}
