package org.o2.metadata.client.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 *
 * 网店管理仓库
 *
 * @author yipeng.zhu@hand-china.com 2021-11-18
 **/
@Data
public class OnlineShopRelWarehouseInnerDTO {
    @ApiModelProperty(value = "网店编码")
    private List<String> onlineShopCodes;
}
