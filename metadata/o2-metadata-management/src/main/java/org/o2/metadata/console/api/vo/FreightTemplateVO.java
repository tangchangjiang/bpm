package org.o2.metadata.console.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 运费模板
 *
 * @author peng.xu@hand-china.com 2019/5/15
 */
@ApiModel("运费模板")
@Data
public class FreightTemplateVO {

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
    
}
