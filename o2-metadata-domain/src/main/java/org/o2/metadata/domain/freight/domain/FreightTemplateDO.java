package org.o2.metadata.domain.freight.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 运费模板
 *
 * @author peng.xu@hand-china.com 2019/5/15
 */
@ApiModel("运费模板")
public class FreightTemplateDO {

    @ApiModelProperty("表ID，主键")
    private Long templateId;
    @ApiModelProperty(value = "运费模板编码")
    private String templateCode;
    @ApiModelProperty(value = "运费模板名称")
    private String templateName;
    @ApiModelProperty(value = "是否包邮")
    private Integer deliveryFreeFlag;
    @ApiModelProperty(value = "计价方式，值集O2MD.VALUATION_TYPE")
    private String valuationType;
    @ApiModelProperty(value = "计价单位，值集O2MD.UOM")
    private String valuationUom;
    @ApiModelProperty(value = "默认运费模板标记，新建的时候默认为0")
    private Integer dafaultFlag;
    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
    @ApiModelProperty(value = "计价方式描述")
    private String valuationTypeMeaning;
    @ApiModelProperty(value = "计价单位描述")
    private String valuationUomMeaning;

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Integer getDeliveryFreeFlag() {
        return deliveryFreeFlag;
    }

    public void setDeliveryFreeFlag(Integer deliveryFreeFlag) {
        this.deliveryFreeFlag = deliveryFreeFlag;
    }

    public String getValuationType() {
        return valuationType;
    }

    public void setValuationType(String valuationType) {
        this.valuationType = valuationType;
    }

    public String getValuationUom() {
        return valuationUom;
    }

    public void setValuationUom(String valuationUom) {
        this.valuationUom = valuationUom;
    }

    public Integer getDafaultFlag() {
        return dafaultFlag;
    }

    public void setDafaultFlag(Integer dafaultFlag) {
        this.dafaultFlag = dafaultFlag;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getValuationTypeMeaning() {
        return valuationTypeMeaning;
    }

    public void setValuationTypeMeaning(String valuationTypeMeaning) {
        this.valuationTypeMeaning = valuationTypeMeaning;
    }

    public String getValuationUomMeaning() {
        return valuationUomMeaning;
    }

    public void setValuationUomMeaning(String valuationUomMeaning) {
        this.valuationUomMeaning = valuationUomMeaning;
    }

    @Override
    public String toString() {
        return "FreightTemplateDO{" +
                "templateId=" + templateId +
                ", templateCode='" + templateCode + '\'' +
                ", templateName='" + templateName + '\'' +
                ", deliveryFreeFlag=" + deliveryFreeFlag +
                ", valuationType='" + valuationType + '\'' +
                ", valuationUom='" + valuationUom + '\'' +
                ", dafaultFlag=" + dafaultFlag +
                ", tenantId=" + tenantId +
                ", valuationTypeMeaning='" + valuationTypeMeaning + '\'' +
                ", valuationUomMeaning='" + valuationUomMeaning + '\'' +
                '}';
    }
}
