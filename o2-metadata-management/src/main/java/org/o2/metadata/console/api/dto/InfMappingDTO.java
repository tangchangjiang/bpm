package org.o2.metadata.console.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * description 平台数据匹配
 *
 * @author zhilin.ren@hand-china.com 2021/08/03 22:55
 */
@Data
public class InfMappingDTO {

    @ApiModelProperty(value = "信息类型，值集：O2MD.INF_TYPE", required = true)
    private String infTypeCode;
    @ApiModelProperty(value = "平台编码", required = true)
    private String platformCode;
    @ApiModelProperty(value = "信息名称", required = true)
    private String infName;
    @ApiModelProperty(value = "租户ID", required = true)
    private Long tenantId;
    @ApiModelProperty(value = "平台信息名称")
    private String platformInfName;
    @ApiModelProperty(value = "平台名称")
    private String platformName;

}
