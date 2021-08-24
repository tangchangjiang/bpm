package org.o2.feignclient.metadata.domain.co;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import java.math.BigDecimal;

/**
 * 详细地址信息
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Data
@ApiModel("详细地址")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PosAddressCO {

    @ApiModelProperty("表ID，主键，供其他表做外键")

    private Long posAddressId;

    @ApiModelProperty(value = "国家")
    private String countryCode;

    @ApiModelProperty(value = "省")
    private String regionCode;

    @ApiModelProperty(value = "市")
    private String cityCode;

    @ApiModelProperty(value = "区")
    private String districtCode;

    @ApiModelProperty(value = "街道,门牌号")
    private String streetName;

    @ApiModelProperty(value = "电话")
    private String phoneNumber;

    @ApiModelProperty(value = "邮编")
    private String postcode;

    @ApiModelProperty(value = "联系人")
    private String contact;

    @ApiModelProperty(value = "手机号")
    private String mobilePhone;

    @ApiModelProperty("经度")
    private BigDecimal longitude;

    @ApiModelProperty("纬度")
    private BigDecimal latitude;


    @ApiModelProperty(value = "国家")

    private String country;

    @ApiModelProperty(value = "省")
    private String region;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区")
    private String district;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
}
