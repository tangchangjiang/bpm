package org.o2.metadata.management.client.domain.co;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 承运商物流成本计算
 *
 * @author zhilin.ren@hand-china.com 2022/10/12 11:36
 */
@Data
public class CarrierLogisticsCostCO {

    @ApiModelProperty(value = "承运商编码")
    private String carrierCode;

    @ApiModelProperty(value = "物流成本")
    private BigDecimal logisticsCost;

}
