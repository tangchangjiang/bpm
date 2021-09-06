package org.o2.metadata.console.api.dto;

/**
 * 静态资源配置DTO
 *
 * @author wenjie.liu@hand-china.com 2021-09-06 10:47:44
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("静态资源配置DTO")
@Data
public class StaticResourceConfigDTO {

    @ApiModelProperty(value = "静态资源编码")
    private String resourceCode;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

}
