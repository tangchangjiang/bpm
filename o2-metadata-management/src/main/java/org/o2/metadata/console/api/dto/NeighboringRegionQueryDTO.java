package org.o2.metadata.console.api.dto;

import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 临近省
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("临近省")
public class NeighboringRegionQueryDTO extends AuditDomain {

    @ApiModelProperty("表ID，主键，供其他表做外键")
    private Long neighboringRegionId;

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

    @ApiModelProperty(value = "pos类型含义", hidden = true)
    private String posTypeMeaning;

    @ApiModelProperty(hidden = true)
    private String sourceCountryName;

    @ApiModelProperty(hidden = true)
    private String targetCountryName;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
}
