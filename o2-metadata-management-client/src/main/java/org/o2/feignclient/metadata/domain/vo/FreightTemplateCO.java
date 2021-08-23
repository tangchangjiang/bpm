package org.o2.feignclient.metadata.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Objects;


/**
 * 运费模板
 *
 * @author peng.xu@hand-china.com 2019/5/15
 */
@ApiModel("运费模板")
@Data
public class FreightTemplateCO {

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


    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        FreightTemplateCO that = (FreightTemplateCO) o;
        return templateId.equals(that.templateId) &&
                templateCode.equals(that.templateCode) &&
                templateName.equals(that.templateName) &&
                deliveryFreeFlag.equals(that.deliveryFreeFlag) &&
                valuationType.equals(that.valuationType) &&
                valuationUom.equals(that.valuationUom) &&
                dafaultFlag.equals(that.dafaultFlag) &&
                tenantId.equals(that.tenantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(templateId, templateCode, templateName, deliveryFreeFlag, valuationType, valuationUom, dafaultFlag, tenantId);
    }
}
