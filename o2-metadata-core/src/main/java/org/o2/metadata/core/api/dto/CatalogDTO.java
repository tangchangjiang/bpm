package org.o2.metadata.core.api.dto;

import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

@Data
@ExcelSheet(zh = "版本数据")
public class CatalogDTO {

    @ExcelColumn(zh = "版本编码")
    private String catalogCode;


    @ExcelColumn(zh = "版本描述")
    private String catalogRemarks;


    @ExcelColumn(zh = "是否有效")
    private Integer activeFlag;


    @ExcelColumn(zh = "版本名称")
    private String catalogName;


}
