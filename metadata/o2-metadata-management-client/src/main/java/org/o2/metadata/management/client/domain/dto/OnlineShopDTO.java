package org.o2.metadata.management.client.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 网店
 * @author yuncheng.ma@hand-china.com
 * @since 2022-06-27 11:23:17
 */
@Data
public class OnlineShopDTO {

    @ApiModelProperty(value = "表ID", hidden = true)
    private Long onlineShopId;
    @ApiModelProperty(value = "平台编码")
    private String platformCode;
    @ApiModelProperty(value = "网点名称")
    private String onlineShopName;
    @ApiModelProperty(value = "网点编码")
    private String onlineShopCode;
    @ApiModelProperty(value = "平台店铺 code")
    private String platformShopCode;
    @ApiModelProperty(value = "是否支持寻源", hidden = true)
    private Integer sourcedFlag;
    @ApiModelProperty(value = "是否支持自提", hidden = true)
    private Integer pickedUpFlag;
    @ApiModelProperty(value = "是否支持到店退", hidden = true)
    private Integer returnedFlag;
    @ApiModelProperty(value = "是否有换货权限", hidden = true)
    private Integer exchangedFlag;
    @ApiModelProperty(value = "是否有效")
    private Integer activeFlag;
    @ApiModelProperty(value = "是否拆分平台订单", hidden = true)
    private Integer enableSplitFlag;
    @ApiModelProperty(value = "组织ID")
    private Long tenantId;
    @ApiModelProperty(value = "是否默认网店", hidden = true)
    private Integer isDefault;
    @ApiModelProperty(value = "默认货币", hidden = true)
    private String defaultCurrency;
    @ApiModelProperty(value = "目录编码")
    private String catalogCode;

    @ApiModelProperty(value = "版本目录编码")
    private String catalogVersionCode;

    private String onlineShopType;
}
