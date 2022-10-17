package org.o2.rule.engine.management.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xiang.zhao@hand-chian.com 2022/10/17
 */
@Data
public class RuleConditionVO {
    @ApiModelProperty(value = "规则实体条件编码")
    private String conditionCode;
    @ApiModelProperty(value = "规则条件表达式")
    private String conditionExpression;
}
