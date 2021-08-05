package org.o2.metadata.console.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;


/**
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Data
public class CountryDTO{

    @ApiModelProperty("国家ID")
    private Long countryId;

    @ApiModelProperty("国家编码")
    private String countryCode;

    @ApiModelProperty("国家名称")
    private String countryName;

    @ApiModelProperty("是否启用")
    private Integer enabledFlag;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
}
