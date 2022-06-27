package org.o2.metadata.management.client.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 地址
 * @author yuncheng.ma@hand-china.com
 * @since 2022-06-27 11:46:18
 */
@Data
public class PosAddressDTO {

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
    @ApiModelProperty(value = "16位地址码: (国家编码,省编码,市编码,区编码,街道或楼牌,收货人姓名,收货人手机号)转成16位")
    private String codeDec16;
    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
    @ApiModelProperty(value = "关联o2md_pos.pos_code 服务点编码")
    private String posCode;

}
