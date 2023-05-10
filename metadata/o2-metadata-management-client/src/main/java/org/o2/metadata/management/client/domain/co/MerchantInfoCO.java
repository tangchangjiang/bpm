package org.o2.metadata.management.client.domain.co;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 商家信息
 *
 * @author chao.yang05@hand-china.com 2023-05-08
 */
@Data
public class MerchantInfoCO {

    /**
     * 网店编码
     */
    @ApiModelProperty("网店编码")
    @NotBlank
    private String onlineShopCode;

    /**
     * 网店名称
     */
    @ApiModelProperty("网店名称")
    private String onlineShopName;

    /**
     * 店铺logo
     */
    @ApiModelProperty("店铺logo")
    private String logoUrl;

    /**
     * 店铺图片
     */
    @ApiModelProperty("店铺图片")
    private String shopMediaUrl;

    /**
     * 自营标识
     */
    @ApiModelProperty("自营标识")
    private Integer selfSalesFlag;

    /**
     * 网店生效标识
     */
    @ApiModelProperty("网店生效标识")
    private Integer activeFlag;
}
