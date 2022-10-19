package org.o2.rule.engine.client.domain.entity;

import lombok.Data;

/**
 * 规则
 *
 * @author wei.cai@hand-china.com
 * @date 2022/10/12
 */
@Data
public class Rule {
    /**
     * 规则编码
     */
    private String ruleCode;
    /**
     * 规则实体编码
     */
    private String entityCode;
    /**
     * 规则实体别名
     */
    private String entityAlias;
    /**
     * 规则条件
     */
    private RuleCondition ruleCondition;
}
