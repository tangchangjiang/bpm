package org.o2.metadata.console.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 国家信息刷新DTO
 */
@Data
public class CountryRefreshDTO {
    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
    @ApiModelProperty(value = "语言")
    private String lang;
    @ApiModelProperty(value = "业务类型")
    private String businessTypeCode;
    @ApiModelProperty(value = "文件桶前缀")
    private String bucketPrefix;
}
