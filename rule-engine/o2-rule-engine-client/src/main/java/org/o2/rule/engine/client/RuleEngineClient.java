package org.o2.rule.engine.client;

import org.o2.rule.engine.client.app.exception.RuleExecuteException;
import org.o2.rule.engine.client.domain.RuleConditionResult;
import org.o2.rule.engine.client.domain.RuleObject;

/**
 * 规则引擎客户端, 外部使用的唯一入口
 *
 * @author wei.cai@hand-china.com
 * @date 2022/10/11
 */
public interface RuleEngineClient {

    /**
     * 执行规则条件
     *
     * @param tenantId 租户ID
     * @param ruleConditionCode 规则条件编码
     * @param fact fact实体
     * @return 规则条件结果
     */
    RuleConditionResult fireRuleCondition(Long tenantId, String ruleConditionCode, RuleObject fact) throws RuleExecuteException;


}
