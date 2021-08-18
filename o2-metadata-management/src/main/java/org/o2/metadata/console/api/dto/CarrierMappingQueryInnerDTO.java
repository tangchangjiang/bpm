package org.o2.metadata.console.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 承运商
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Data
@ApiModel("承运商匹配")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarrierMappingQueryInnerDTO {


    @ApiModelProperty("平台编码")
    private String platformCode;

    private Long tenantId;
}
