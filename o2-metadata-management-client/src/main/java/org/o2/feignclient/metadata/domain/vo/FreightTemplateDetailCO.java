package org.o2.feignclient.metadata.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 运费模板
 *
 * @author peng.xu@hand-china.com 2019/5/15
 */
@Data
public class FreightTemplateDetailCO {
    @ApiModelProperty("表ID，主键")
    private Long templateDetailId;
    @ApiModelProperty(value = "运送方式，关联值集O2MD.TRANSPORT_TYPE")
    private String transportTypeCode;
    @ApiModelProperty(value = "目的地")
    private Long regionId;
    @ApiModelProperty(value = "首件/千克设置")
    private BigDecimal firstPieceWeight;
    @ApiModelProperty(value = "首件/千克价格")
    private BigDecimal firstPrice;
    @ApiModelProperty(value = "续件/千克设置")
    private BigDecimal nextPieceWeight;
    @ApiModelProperty(value = "续件/千克价格")
    private BigDecimal nextPrice;
    @ApiModelProperty(value = "是否默认")
    private Integer defaultFlag;
    @ApiModelProperty(value = "关联运费模板ID")
    private Long templateId;
    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
    @ApiModelProperty("目的地描述")
    private String regionName;
    @ApiModelProperty(value = "运送方式含义")
    private String transportTypeMeaning;
}
