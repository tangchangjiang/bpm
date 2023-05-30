package org.o2.metadata.infra.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.o2.multi.language.core.annotation.O2RedisMultiLanguageField;

import java.math.BigDecimal;

/**
 * @author chao.yang05@hand-china.com 2022/4/14
 */
@Data
public class Pos {

    @ApiModelProperty(value = "服务点编码")
    private String posCode;

    @ApiModelProperty(value = "服务点名称")
    @O2RedisMultiLanguageField
    private String posName;

    @ApiModelProperty(value = "服务点状态")
    private String posStatusCode;

    @ApiModelProperty(value = "服务点类型")
    private String posTypeCode;

    @ApiModelProperty(value = "营业类型")
    private String businessTypeCode;

    @ApiModelProperty(value = "营业时间")
    private String businessTime;

    @ApiModelProperty(value = "国家")
    private String countryCode;

    @ApiModelProperty(value = "国家name")
    private String countryName;

    @ApiModelProperty(value = "省")
    private String regionCode;

    @ApiModelProperty(value = "市")
    private String cityCode;

    @ApiModelProperty(value = "区")
    private String districtCode;

    @ApiModelProperty(value = "省name")
    private String regionName;

    @ApiModelProperty(value = "市name")
    private String cityName;

    @ApiModelProperty(value = "区name")
    private String districtName;

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

    @ApiModelProperty("仓库自提标识")
    private Integer pickedUpFlag;

    @ApiModelProperty(value = "自提接单量")
    private Long pickUpQuantity;
}
