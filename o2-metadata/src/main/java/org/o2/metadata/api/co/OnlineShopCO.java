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

    @ApiModelProperty(value = "是否支持自提")
    private Integer pickedUpFlag;

}
