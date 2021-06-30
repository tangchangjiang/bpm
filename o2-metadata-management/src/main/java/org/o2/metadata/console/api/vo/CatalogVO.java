package org.o2.metadata.console.api.vo;

import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

/**
 * 目录数据实体
 * @author jiu.yang@hand-china.com
 * @date 2019/12/25 18:36
 */
@Data
@ExcelSheet(zh = "目录数据")
public class CatalogVO {

    @ExcelColumn(zh = "目录编码")
    private String catalogCode;


    @ExcelColumn(zh = "目录描述")
    private String catalogRemarks;


    @ExcelColumn(zh = "是否有效")
    private Integer activeFlag;


    @ExcelColumn(zh = "目录名称")
    private String catalogName;


}
