package org.o2.metadata.console.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 系统参数复制DTO
 *
 * @author chao.yang05@hand-china.com 2023-06-02
 */
@Data
public class SysParamCopyDTO {

    /**
     * 系统参数Id
     */
    @ApiModelProperty("系统参数Id")
    private Long paramId;

    /**
     * 租户Id
     */
    @ApiModelProperty("租户Id")
    private List<Long> tenantIds;
}
