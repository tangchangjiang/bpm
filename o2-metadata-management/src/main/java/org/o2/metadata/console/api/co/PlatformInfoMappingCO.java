package org.o2.metadata.console.api.co;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *
 * 平台信息匹配
 *
 * @author yipeng.zhu@hand-china.com 2021-08-16
 **/
@Data
public class PlatformInfoMappingCO {
    @ApiModelProperty("表主键")
    private Long platformInfMappingId;
    @ApiModelProperty(value = "信息类型，值集：O2MD.INF_TYPE", required = true)
    private String infTypeCode;
    @ApiModelProperty(value = "平台编码", required = true)
    private String infCode;
    @ApiModelProperty(value = "信息名称", required = true)
    private String infName;
    private String platformInfCode;
    private String platformInfName;
    @ApiModelProperty(value = "租户ID", required = true)
    private Long tenantId;
}
