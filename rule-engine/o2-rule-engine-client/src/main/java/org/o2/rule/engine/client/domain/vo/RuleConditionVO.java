package org.o2.rule.engine.client.domain.vo;

import lombok.Data;

/**
 * 规则条件
 *
 * @author wei.cai@hand-china.com
 * @date 2022/10/17
 */
@Data
public class RuleConditionVO {
    /**
     * 条件编码
     */
    private String conditionCode;
    /**
     * 条件表达式
     */
    private String conditionExpression;
}
