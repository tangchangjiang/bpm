package org.o2.rule.engine.management.domain.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 规则实体
 *
 * @author yuncheng.ma@hand-china.com
 * @since 2022-10-17 15:05:21
 */
@Data
public class RuleEntityBO {

    @ApiModelProperty(value = "规则实体编码，编码规则生成", required = true)
    private String ruleEntityCode;
    @ApiModelProperty(value = "规则实体名称", required = true)
    private String ruleEntityName;
    @ApiModelProperty(value = "规则实体别名")
    private String ruleEntityAlias;
    @ApiModelProperty(value = "是否启用", required = true)
    private Integer enableFlag;

}
