package org.o2.metadata.domain.freight.domain;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * 运费模板明细
 */
public class FreightTemplateDetailDO {
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

    public Long getTemplateDetailId() {
        return templateDetailId;
    }

    public void setTemplateDetailId(Long templateDetailId) {
        this.templateDetailId = templateDetailId;
    }

    public String getTransportTypeCode() {
        return transportTypeCode;
    }

    public void setTransportTypeCode(String transportTypeCode) {
        this.transportTypeCode = transportTypeCode;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public BigDecimal getFirstPieceWeight() {
        return firstPieceWeight;
    }

    public void setFirstPieceWeight(BigDecimal firstPieceWeight) {
        this.firstPieceWeight = firstPieceWeight;
    }

    public BigDecimal getFirstPrice() {
        return firstPrice;
    }

    public void setFirstPrice(BigDecimal firstPrice) {
        this.firstPrice = firstPrice;
    }

    public BigDecimal getNextPieceWeight() {
        return nextPieceWeight;
    }

    public void setNextPieceWeight(BigDecimal nextPieceWeight) {
        this.nextPieceWeight = nextPieceWeight;
    }

    public BigDecimal getNextPrice() {
        return nextPrice;
    }

    public void setNextPrice(BigDecimal nextPrice) {
        this.nextPrice = nextPrice;
    }

    public Integer getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(Integer defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getTransportTypeMeaning() {
        return transportTypeMeaning;
    }

    public void setTransportTypeMeaning(String transportTypeMeaning) {
        this.transportTypeMeaning = transportTypeMeaning;
    }

    @Override
    public String toString() {
        return "FreightTemplateDetailDO{" +
                "templateDetailId=" + templateDetailId +
                ", transportTypeCode='" + transportTypeCode + '\'' +
                ", regionId=" + regionId +
                ", firstPieceWeight=" + firstPieceWeight +
                ", firstPrice=" + firstPrice +
                ", nextPieceWeight=" + nextPieceWeight +
                ", nextPrice=" + nextPrice +
                ", defaultFlag=" + defaultFlag +
                ", templateId=" + templateId +
                ", tenantId=" + tenantId +
                ", regionName='" + regionName + '\'' +
                ", transportTypeMeaning='" + transportTypeMeaning + '\'' +
                '}';
    }
}
