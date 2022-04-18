package org.o2.metadata.api.co;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chao.yang05@hand-china.com 2022/4/18
 */
@Data
public class WarehousePickupLimitCO {

    /**
     * 已下单的自提量
     */
    @ApiModelProperty("已下单自提量")
    private Long pickUpQuantity;

    /**
     * 是否达到自提量上限
     */
    @ApiModelProperty("自提上限标识")
    private Boolean limitFlag;
}
