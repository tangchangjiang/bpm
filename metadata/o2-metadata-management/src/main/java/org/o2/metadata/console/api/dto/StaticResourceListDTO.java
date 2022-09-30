package org.o2.metadata.console.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * description 关联查询静态资源和配置条件
 *
 * @author zhilin.ren@hand-china.com 2021/09/08 16:38
 */
@Data
public class StaticResourceListDTO {

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "语言")
    @NotBlank
    private String lang;

    @ApiModelProperty(value = "站点编码")
    private String siteCode;

    @ApiModelProperty(value = "目录版本编码")
    private String catalogVersionCode;

}
