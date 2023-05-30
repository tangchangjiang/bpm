package org.o2.metadata.api.co;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 网店
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Data
@ApiModel("网店基")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OnlineShopCO {

    @ApiModelProperty(value = "网点编码")
    private String onlineShopCode;

    @ApiModelProperty(value = "网点名称")
    private String onlineShopName;

    @ApiModelProperty(value = "网店")
    private Long tenantId;

    @ApiModelProperty(value = "平台编码")
    private String platformCode;

    @ApiModelProperty(value = "平台网店编码")
    private String platformShopCode;

    @ApiModelProperty(value = "是否支持自提")
    private Integer pickedUpFlag;

    @ApiModelProperty(value = "是否允许拆分订单")
    private Integer enableSplitFlag;

    @ApiModelProperty(value = "是否到店退")
    private Integer returnedFlag;

    @ApiModelProperty(value = "是否寻源")
    private Integer sourcedFlag;

    @ApiModelProperty(value = "是否有换货权限")
    private Integer exchangedFlag;

    @ApiModelProperty(value = "是否默认网店")
    private Integer isDefault;

    @ApiModelProperty(value = "目录")
    private String catalogCode;

    @ApiModelProperty(value = "目录版本")
    private String catalogVersionCode;
    /**
     * 网店类型
     */
    @ApiModelProperty(value = "网店类型")
    private String onlineShopType;

    /**
     * 业务类型
     */
    private String businessTypeCode;
    /**
     * logo
     */
    private String logoUrl;

    /**
     * 店铺图片
     */
    private String shopMediaUrl;

    /**
     * 是否自营：1-自营，0-非自营
     */
    private Integer selfSalesFlag;
}
