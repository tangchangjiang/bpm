package org.o2.metadata.console.api.co;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 承运商可配送范围
 *
 * @author zhilin.ren@hand-china.com 2022/10/13 9:49
 */
@Data
public class CarrierDeliveryRangeCO {

    @ApiModelProperty(value = "承运商编码")
    private String carrierCode;

    @ApiModelProperty(value = "是否可送达,1为可送达;为不可送达")
    private Integer deliveryFlag;

}
