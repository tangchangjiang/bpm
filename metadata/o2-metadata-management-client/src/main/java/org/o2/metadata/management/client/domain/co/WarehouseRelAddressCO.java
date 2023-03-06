package org.o2.metadata.management.client.domain.co;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 仓库关联地址CO
 *
 * @author miao.chen01@hand-china.com 2021-11-13
 */
@Data
public class WarehouseRelAddressCO {
    @ApiModelProperty("仓库code")
    private String warehouseCode;
    @ApiModelProperty("仓库类型（门店仓、实体仓）")
    private String warehouseType;
    @ApiModelProperty("仓库分数")
    private String score;
    @ApiModelProperty("省")
    private String regionCode;
    @ApiModelProperty("市")
    private String cityCode;
    @ApiModelProperty("区")
    private String districtCode;
    @ApiModelProperty("仓储平台code")
    private String wmsCode;
}
