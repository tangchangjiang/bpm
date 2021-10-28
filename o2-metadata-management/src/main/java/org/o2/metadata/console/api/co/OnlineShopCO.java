package org.o2.metadata.console.api.co;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

/**
 * 网店
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Data
@ApiModel("网店基础设置")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OnlineShopCO {

    @ApiModelProperty(value = "表ID")
    private Long onlineShopId;

    @ApiModelProperty(value = "网点编码")
    private String onlineShopCode;

    @ApiModelProperty(value = "目录编码")
    private String catalogCode;
    @ApiModelProperty(value = "目录名称")
    private String catalogName;

    @ApiModelProperty(value = "目录版本编码")
    private String catalogVersionCode;
    @ApiModelProperty(value = "目录版本名称")
    private String catalogVersionName;

    @ApiModelProperty(value = "网点名称")
    private String onlineShopName;

    @ApiModelProperty(value = "是否有效")
    private Integer activeFlag;

    @ApiModelProperty("平台编码")
    private String platformCode;

    @ApiModelProperty("平台网店编码")
    private String platformShopCode;

    @ApiModelProperty("平台名称")
    private String platformName;

    @ApiModelProperty(value = "是否支持寻源")
    private Integer sourcedFlag;

    @ApiModelProperty(value = "是否支持自提")
    private Integer pickedUpFlag;

    @ApiModelProperty(value = "是否支持到店退")
    private Integer returnedFlag;

    @ApiModelProperty(value = "是否有换货权限")
    private Integer exchangedFlag;

    @ApiModelProperty(value = "是否拆分平台订单")
    private Integer enableSplitFlag;

}
