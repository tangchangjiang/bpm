package org.o2.metadata.management.client.domain.co;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * 承运商匹配
 *
 * @author yipeng.zhu@hand-china.com 2021-08-18
 **/
@Data
public class CarrierMappingCO {


    @ApiModelProperty(value = "平台承运商编码")
    private String platformCarrierCode;

    @ApiModelProperty(value = "平台承运商名称")
    private String platformCarrierName;

    @ApiModelProperty(value = "版本编码")
    private String platformCode;

    @ApiModelProperty(value = "版本名称")
    private String platformName;

    @ApiModelProperty(value = "承运商编码")
    private String carrierCode;

    @ApiModelProperty(value = "承运商名称")
    private String carrierName;


}
