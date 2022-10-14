package org.o2.metadata.console.app.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhilin.ren@hand-china.com 2022/10/12 14:06
 */
@Data
public class CarrierLogisticsCostDetailBO {

    private Long templateDetailId;
    @ApiModelProperty(value = "目的地")
    private String regionCode;
    @ApiModelProperty(value = "首件/千克设置")
    private BigDecimal firstPieceWeight;
    @ApiModelProperty(value = "首件/千克价格")
    private BigDecimal firstPrice;
    @ApiModelProperty(value = "续件/千克设置")
    private BigDecimal nextPieceWeight;
    @ApiModelProperty(value = "续件/千克价格")
    private BigDecimal nextPrice;

}
