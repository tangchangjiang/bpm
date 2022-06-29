package org.o2.metadata.management.client.domain.dto;

import java.time.LocalDate;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 服务点
 * @author yuncheng.ma@hand-china.com
 * @since 2022-06-27 11:17:39
 */
@Data
public class PosDTO {

    @ApiModelProperty("表ID，主键，供其他表做外键")
    private Long posId;
    @ApiModelProperty(value = "服务点编码")
    private String posCode;
    @ApiModelProperty(value = "服务点名称")
    private String posName;
    @ApiModelProperty(value = "服务点状态")
    private String posStatusCode;
    @ApiModelProperty(value = "服务点类型,值集O2MD.POS_TYPE")
    private String posTypeCode;
    @ApiModelProperty(value = "营业类型")
    private String businessTypeCode;
    @ApiModelProperty(value = "开店日期")
    private LocalDate openDate;
    @ApiModelProperty(value = "详细地址")
    private Long addressId;
    @ApiModelProperty(value = "营业时间")
    private String businessTime;
    @ApiModelProperty(value = "门店自提接单量")
    private Long pickUpLimitQuantity;
    @ApiModelProperty(value = "店铺公告信息")
    private String notice;
    @ApiModelProperty(value = "门店快递发货接单量", hidden = true)
    private Long expressLimitQuantity;
    @ApiModelProperty(value = "门店快递发货接单量", hidden = true)
    private PosAddressDTO address;
    private Long tenantId;


}
