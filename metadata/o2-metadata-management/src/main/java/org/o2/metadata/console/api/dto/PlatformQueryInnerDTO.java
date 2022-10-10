package org.o2.metadata.console.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * description 平台匹配查询条件
 *
 * @author zhilin.ren@hand-china.com 2021/08/02 21:40
 */
@Data
public class PlatformQueryInnerDTO {

    @ApiModelProperty(value = "平台编码")
    @NotNull
    private List<String> platformCodes;

    @ApiModelProperty(value = "租户ID")
    @NotNull
    private Long tenantId;

    @ApiModelProperty(value = "平台类型编码")
    private String infTypeCode;
}