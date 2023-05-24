package org.o2.metadata.console.app.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.o2.multi.language.core.annotation.O2RedisMultiLanguage;
import org.o2.multi.language.core.annotation.O2RedisMultiLanguageField;

/**
 * 服务点redis
 *
 * @author yipeng.zhu@hand-china.com 20283-05-24
 **/
@Data
@O2RedisMultiLanguage(tlsTable = "o2md_pos_tl")
public class PosMultiRedisBO {
    @ApiModelProperty(value = "服务点编码")
    private String posCode;
    @O2RedisMultiLanguageField
    @ApiModelProperty(value = "服务点名称")
    private String posName;
    @O2RedisMultiLanguageField(tableUniqueKey=true)
    @ApiModelProperty(value = "服务点主键")
    private Long posId;
}
