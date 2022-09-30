package org.o2.metadata.console.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 *
 * 地址匹配
 *
 * @author yipeng.zhu@hand-china.com 2021-08-06
 **/
@Data
public class AddressMappingQueryInnerDTO {

    @ApiModelProperty(value = "批量查询地址")
    private List<AddressMappingInnerDTO> addressMappingInnerList;

    @ApiModelProperty(value = "平台编码")
    private String platformCode;

    private Long tenantId;
}
