package org.o2.business.process.management.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.util.List;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/8/23 17:22
 */
@Data
@ExcelSheet(zh = "业务流程节点", en = "business process node")
public class BusinessNodeExportVO {

    @ApiModelProperty(value = "业务流程节点bean", required = true)
    @ExcelColumn(zh = "业务流程节点bean", en = "beanId")
    private String beanId;
    @ApiModelProperty(value = "业务节点描述")
    @ExcelColumn(zh = "业务节点描述", en = "description")
    private String description;
    @ApiModelProperty(value = "节点类型", required = true)
    @ExcelColumn(zh = "节点类型", en = "nodeType")
    private String nodeType;
    @ApiModelProperty(value = "脚本")
    @ExcelColumn(zh = "脚本", en = "script")
    private String script;
    @ApiModelProperty(value = "1-启用/0-禁用", required = true)
    @ExcelColumn(zh = "启用状态", en = "enabledFlag")
    private Integer enabledFlag;
    @ApiModelProperty(value = "业务类型(O2BPM.BUSINESS_TYPE)", required = true)
    @ExcelColumn(zh = "业务类型", en = "businessTypeCode")
    private String businessTypeCode;
    @ApiModelProperty(value = "二级业务类型(O2BPM.SUB_BUSINESS_TYPE)")
    @ExcelColumn(zh = "二级业务类型", en = "subBusinessTypeCode")
    private String subBusinessTypeCode;

    @ApiModelProperty(value = "节点参数集合")
    @ExcelColumn(zh = "业务流程节点参数", en = "nodeParamExportList", child = true)
    List<NodeParamExportVO> nodeParamExportList;

}
