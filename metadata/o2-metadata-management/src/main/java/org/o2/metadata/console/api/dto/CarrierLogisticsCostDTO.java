package org.o2.metadata.console.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 承运商物流成本计算
 *
 * @author zhilin.ren@hand-china.com 2022/10/12 11:14
 */
@Data
public class CarrierLogisticsCostDTO {

    @ApiModelProperty(value = "sku总重量,重量单位为千克")
    @NotNull
    private BigDecimal skuTotalWeight;
    @ApiModelProperty(value = "sku总体积,重量单位为立方厘米")
    @NotNull
    private BigDecimal skuTotalVolume;
    @ApiModelProperty(value = "收货地址")
    @NotNull
    private ReceiveAddressDTO address;
    @ApiModelProperty(value = "备选承运商编码")
    @NotEmpty
    private List<String> alternateCarrierList;
    @ApiModelProperty(value = "租户id")
    private Long tenantId;
}
