package org.o2.metadata.console.api.co;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 服务点CO
 *
 * @author zhanpeng.jiang@hand-china.com 2022/12/14
 */
@Data
public class PosRedisCO {
    @ApiModelProperty(value = "服务点编码")
    private String posCode;

    @ApiModelProperty(value = "服务点名称")
    private String posName;

    @ApiModelProperty(value = "服务点状态")
    private String posStatusCode;

    @ApiModelProperty(value = "服务点类型")
    private String posTypeCode;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;
}
