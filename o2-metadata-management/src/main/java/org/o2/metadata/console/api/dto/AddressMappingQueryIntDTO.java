package org.o2.metadata.console.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 *
 * 地址匹配
 *
 * @author yipeng.zhu@hand-china.com 2021-08-06
 **/
@Data
public class AddressMappingQueryIntDTO {
    /**
     * 条件查询code 和 name 二选一
     */
    @ApiModelProperty(value = "外部区域代码")
    private String externalCode;

    @ApiModelProperty(value = "外部区域名称")
    private String externalName;

    /**
     * 属于(省) or  (市)  3(区)
     */
    private String addressTypeCode;
}
