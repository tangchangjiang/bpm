package org.o2.metadata.console.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * 承运商
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Data
@ApiModel("承运商")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarrierQueryInnerDTO {


    @ApiModelProperty(value = "承运商编码")
    private List<String> carrierCodes;

    @ApiModelProperty(value = "承运商名称")
    private List<String> carrierNames;

}