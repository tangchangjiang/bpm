package org.o2.metadata.console.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@ApiModel("承运商匹配表视图")
@Data
public class CarrierMappingQueryDTO{

    private Long carrierMappingId;


    @ApiModelProperty(value = "平台id")
    private Long platformId;

    @ApiModelProperty(value = "承运商")
    private String carrierCode;

    @ApiModelProperty(value = "平台承运商编码")
    private String platformCarrierCode;

    @ApiModelProperty(value = "平台承运商名称")
    private String platformCarrierName;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "版本编码")
    private String platformCode;

    @ApiModelProperty(value = "版本名称")
    private String platformName;
}
