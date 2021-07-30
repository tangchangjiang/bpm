package org.o2.metadata.console.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 静态资源 SaveDTO
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/07/30 12:04
 */
@Data
public class StaticResourceSaveDTO {

    @ApiModelProperty(value = "静态资源编码[模块名_资源内容名]", required = true)
    @NotBlank
    private String resourceCode;

    @ApiModelProperty(value = "来源模块编码", required = true)
    @NotBlank
    private String sourceModuleCode;

    @ApiModelProperty(value = "静态资源相对路径", required = true)
    @NotBlank
    private String resourceUrl;

    @ApiModelProperty(value = "静态资源描述")
    private String description;

    @ApiModelProperty(value = "租户Id", required = true)
    @NotNull
    private Long tenantId;

}
