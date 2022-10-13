package org.o2.rule.engine.client.domain;

import lombok.Data;

/**
 * 规则条件结果
 *
 * @author wei.cai@hand-china.com
 * @date 2022/10/12
 */
@Data
public class RuleConditionResult {

    /**
     * 是否成功, true表示条件满足, false表示条件不满足
     */
    private final boolean conditionMatch;

    /**
     * 构造函数
     * @param conditionMatch conditionMatch
     */
    public RuleConditionResult(boolean conditionMatch) {
        this.conditionMatch = conditionMatch;
    }

}
