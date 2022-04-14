package org.o2.metadata.client.domain.co;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 服务店门店信息
 *
 * @author chao.yang05@hand-china.com 2022/4/14
 */
@Data
public class PosStoreInfoCO {

    @ApiModelProperty(value = "服务点编码")
    private String posCode;

    @ApiModelProperty(value = "服务点名称")
    private String posName;

    @ApiModelProperty(value = "服务点状态")
    private String posStatusCode;

    @ApiModelProperty(value = "服务点类型")
    private String posTypeCode;

    @ApiModelProperty(value = "营业类型")
    private String businessTypeCode;

    @ApiModelProperty(value = "营业时间")
    private String businessTime;

    @ApiModelProperty(value = "街道,门牌号")
    private String streetName;

    @ApiModelProperty("经度")
    private BigDecimal longitude;

    @ApiModelProperty("纬度")
    private BigDecimal latitude;

    @ApiModelProperty(value = "电话")
    private String phoneNumber;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "自提发货接单量")
    private Long pickUpQuantity;

    @ApiModelProperty("仓库自提标识")
    private Integer pickedUpFlag;
}
