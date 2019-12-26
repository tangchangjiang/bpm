package org.o2.metadata.core.domain.vo;

import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

/**
 * 版本数据实体
 * @author jiu.yang@hand-china.com
 * @date 2019/12/25 18:36
 */
@Data
@ExcelSheet(zh = "版本数据")
public class CatalogVO {

    @ExcelColumn(zh = "版本编码")
    private String catalogCode;


    @ExcelColumn(zh = "版本描述")
    private String catalogRemarks;


    @ExcelColumn(zh = "是否有效")
    private Integer activeFlag;


    @ExcelColumn(zh = "版本名称")
    private String catalogName;


}
