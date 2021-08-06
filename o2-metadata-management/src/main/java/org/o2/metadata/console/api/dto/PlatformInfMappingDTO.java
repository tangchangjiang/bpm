package org.o2.metadata.console.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * description 平台匹配查询条件
 *
 * @author zhilin.ren@hand-china.com 2021/08/02 21:40
 */
@Data
public class PlatformInfMappingDTO {
    @ApiModelProperty(value = "信息类型，值集：O2MD.INF_TYPE", required = true)
    @NotBlank
    private String infTypeCode;
    @ApiModelProperty(value = "平台编码", required = true)
    @NotBlank
    private String platformCode;
    @ApiModelProperty(value = "平台信息编码", required = true)
    @NotBlank
    private String platformInfCode;
    @ApiModelProperty(value = "租户ID", required = true)
    private Long tenantId;
}
