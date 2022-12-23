package org.o2.metadata.management.client.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * 地址匹配
 *
 * @author yipeng.zhu@hand-china.com 2021-08-06
 **/
@Data
public class OutAddressMappingInnerDTO {

    @ApiModelProperty(value = "内部区域代码")
    private String regionCode;

    /**
     * 属于(省) or  (市)  3(区)
     */
    private String addressTypeCode;

    @ApiModelProperty(value = "平台编码")
    private String platformCode;

    private Integer activeFlag;
}
