package org.o2.metadata.client.domain.co;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FreightTemplateDetailCO that = (FreightTemplateDetailCO) o;
        return templateDetailId.equals(that.templateDetailId) &&
                transportTypeCode.equals(that.transportTypeCode) &&
                regionId.equals(that.regionId) &&
                firstPieceWeight.equals(that.firstPieceWeight) &&
                firstPrice.equals(that.firstPrice) &&
                nextPieceWeight.equals(that.nextPieceWeight) &&
                nextPrice.equals(that.nextPrice) &&
                defaultFlag.equals(that.defaultFlag) &&
                templateId.equals(that.templateId) &&
                tenantId.equals(that.tenantId) &&
                regionName.equals(that.regionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(templateDetailId, transportTypeCode, regionId, firstPieceWeight, firstPrice, nextPieceWeight, nextPrice, defaultFlag, templateId, tenantId, regionName);
    }
}
