package org.o2.metadata.console.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * description 平台dto
 *
 * @author zhilin.ren@hand-china.com 2021/08/03 22:50
 */
@Data
@ApiModel("平台定义")
public class PlatformDTO {
    @ApiModelProperty(value = "平台编码")
    private String platformCode;
    @ApiModelProperty(value = "平台名称")
    private String platformName;
    @ApiModelProperty(value = "租户id", required = true)
    private Long tenantId;
    @ApiModelProperty(value = "启用标志")
    private Integer activeFlag;

}
