package org.o2.metadata.console.api.co;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 临近省
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Data
@ApiModel("临近省")
public class NeighboringRegionCO {

    @ApiModelProperty(value = "服务点类型,值集: O2MD.POS_TYPE")

    private String posTypeCode;

    @ApiModelProperty(value = "发货国家")
    private String sourceCountryCode;

    @ApiModelProperty(value = "发货省")
    private String sourceRegionCode;

    @ApiModelProperty(value = "收货国家")
    private String targetCountryCode;

    @ApiModelProperty(value = "收货省")
    private String targetRegionCode;

    @ApiModelProperty(value = "发货省", hidden = true)

    private String sourceRegionName;

    @ApiModelProperty(value = "收货省", hidden = true)
    private String targetRegionName;

    @ApiModelProperty(hidden = true)
    private String sourceCountryName;

    @ApiModelProperty(hidden = true)

    private String targetCountryName;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
}
