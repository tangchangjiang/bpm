package org.o2.rule.engine.client.domain.entity;

import lombok.Data;

/**
 * 规则条件对象
 *
 * @author wei.cai@hand-china.com
 * @date 2022/10/12
 */
@Data
public class RuleCondition {

    /**
     * 条件表达式
     */
    private String conditionExpression;

}