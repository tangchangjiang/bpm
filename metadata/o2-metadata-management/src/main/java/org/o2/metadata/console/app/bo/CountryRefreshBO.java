package org.o2.metadata.console.app.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 国家刷新BO
 * @author rui.ling@hand-china.com 2023/02/15
 */
@Data
public class CountryRefreshBO {
    @ApiModelProperty(value = "国家编码")
    private String countryCode;
    @ApiModelProperty(value = "国家名称")
    private String countryName;
    @ApiModelProperty(value = "地区静态资源URL")
    private String regionResourceUrl;
}
