package org.o2.business.process.management.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/8/23 17:26
 */
@Data
@ExcelSheet(zh = "业务流程节点参数", en = "business process node")
public class NodeParamExportVO {

    @ApiModelProperty("流程节点参数主键id")
    private Long bizNodeParameterId;
    @ApiModelProperty(value = "参数编码", required = true)
    @ExcelColumn(zh = "参数编码", en = "paramCode")
    private String paramCode;
    @ApiModelProperty(value = "参数名称", required = true)
    @ExcelColumn(zh = "参数名称", en = "paramName")
    private String paramName;
    @ApiModelProperty(value = "BeanId", required = true)
    @ExcelColumn(zh = "业务流程节点bean", en = "beanId")
    private String beanId;
    @ApiModelProperty(value = "参数格式，HSDR.PARAM_FORMAT", required = true)
    @ExcelColumn(zh = "参数格式", en = "paramFormatCode")
    private String paramFormatCode;
    @ApiModelProperty(value = "编辑类型，HSDR.PARAM_EDIT_TYPE", required = true)
    @ExcelColumn(zh = "编辑类型", en = "paramEditTypeCode")
    private String paramEditTypeCode;
    @ApiModelProperty(value = "是否必须")
    @ExcelColumn(zh = "是否必须", en = "notnullFlag")
    private Integer notnullFlag;
    @ApiModelProperty(value = "业务模型")
    @ExcelColumn(zh = "业务模型", en = "businessModel")
    private String businessModel;
    @ApiModelProperty(value = "字段值从")
    @ExcelColumn(zh = "字段值从", en = "valueFiledFrom")
    private String valueFiledFrom;
    @ApiModelProperty(value = "字段值至")
    @ExcelColumn(zh = "字段值至", en = "valueFiledTo")
    private String valueFiledTo;
    @ApiModelProperty(value = "是否展示", required = true)
    @ExcelColumn(zh = "是否展示", en = "showFlag")
    private Integer showFlag;
    @ApiModelProperty(value = "启用标识", required = true)
    @ExcelColumn(zh = "启用标识", en = "enabledFlag")
    private Integer enabledFlag;
    @ApiModelProperty(value = "默认值")
    @ExcelColumn(zh = "默认值", en = "defaultValue")
    private String defaultValue;
    @ApiModelProperty(value = "默认展示值")
    @ExcelColumn(zh = "默认展示值", en = "defaultMeaning")
    private String defaultMeaning;
    @ApiModelProperty(value = "级联父级字段")
    @ExcelColumn(zh = "级联父级字段", en = "parentField")
    private String parentField;
    @ApiModelProperty(value = "默认值类型")
    @ExcelColumn(zh = "默认值类型", en = "defaultValueType")
    private String defaultValueType;
}
