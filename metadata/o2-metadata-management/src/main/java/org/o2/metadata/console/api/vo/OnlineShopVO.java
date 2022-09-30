package org.o2.metadata.console.api.vo;

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
@ApiModel("网店基础设置")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OnlineShopVO {

    @ApiModelProperty(value = "表ID", hidden = true)
    private Long onlineShopId;

    @ApiModelProperty(value = "网点编码")
    private String onlineShopCode;

    @ApiModelProperty(value = "网点名称")
    private String onlineShopName;

    @ApiModelProperty(value = "目录编码")
    private String catalogCode;

    @ApiModelProperty(value = "目录编码")
    private String catalogVersionCode;

    @ApiModelProperty(value = "是否有效")
    private Integer activeFlag;

    @ApiModelProperty("平台编码")
    private String  platformCode;

    @ApiModelProperty("平台网店编码")
    private String platformShopCode;


}
