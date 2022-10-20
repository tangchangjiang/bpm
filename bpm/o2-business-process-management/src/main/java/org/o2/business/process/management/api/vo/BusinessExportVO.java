package org.o2.business.process.management.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;
import org.o2.process.domain.engine.BpmnModel;

import java.util.List;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/8/23 16:19
 */
@Data
@ExcelSheet(zh = "业务流程", en = "business process")
public class BusinessExportVO {

    @ApiModelProperty(value = "业务流程编码", required = true)
    @ExcelColumn(zh = "业务流程编码", en = "processCode")
    private String processCode;
    @ApiModelProperty(value = "业务流程描述")
    @ExcelColumn(zh = "业务流程描述", en = "description")
    private String description;
    @ApiModelProperty(value = "1-启用/0-禁用", required = true)
    @ExcelColumn(zh = "启用状态", en = "enabledFlag")
    private Integer enabledFlag;
    @ApiModelProperty(value = "业务流程json")
    @ExcelColumn(zh = "业务流程json", en = "processJson")
    private String processJson;
    @ApiModelProperty(value = "画布json")
    @ExcelColumn(zh = "画布json", en = "viewJson")
    private String viewJson;
    @ApiModelProperty(value = "业务类型", required = true)
    @ExcelColumn(zh = "业务类型", en = "businessTypeCode")
    private String businessTypeCode;

    @ExcelColumn(zh = "业务流程节点", en = "nodeExportList", child = true)
    List<BusinessNodeExportVO> nodeExportList;

    private BpmnModel bpmnModel;
}
