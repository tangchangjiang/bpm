package org.o2.metadata.console.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 查询收货地址是否在承运商的送达范围内
 *
 * @author zhilin.ren@hand-china.com 2022/10/13 9:54
 */
@Data
public class CarrierDeliveryRangeDTO {

    @ApiModelProperty(value = "收货地址")
    @NotNull
    private ReceiveAddressDTO address;
    @ApiModelProperty(value = "备选承运商编码")
    @NotEmpty
    private List<String> alternateCarrierList;
    @ApiModelProperty(value = "租户id")
    private Long tenantId;
}
