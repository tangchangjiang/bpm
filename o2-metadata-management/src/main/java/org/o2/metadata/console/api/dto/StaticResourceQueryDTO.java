package org.o2.metadata.console.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import java.util.List;

/**
 * 静态资源文件查询DTO
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/07/30 11:57
 */
@Data
public class StaticResourceQueryDTO {

    @ApiModelProperty(value = "资源编码列表", required = true)
    private List<String> resourceCodeList;

    @ApiModelProperty(value = "语言")
    private String lang;

    @ApiModelProperty(value = "租户Id")
    private Long tenantId;

}
