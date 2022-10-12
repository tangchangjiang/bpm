package org.o2.rule.engine.client;

import lombok.extern.slf4j.Slf4j;
import org.o2.rule.engine.client.app.exception.RuleExecuteException;
import org.o2.rule.engine.client.domain.RuleConditionResult;
import org.o2.rule.engine.client.domain.RuleObject;

/**
 * 规则引擎客户端实现类, 外部使用的唯一入口
 *
 * @author wei.cai@hand-china.com
 * @date 2022/10/11
 */
@Slf4j
public class RuleEngineClientImpl implements RuleEngineClient {

    @Override
    public RuleConditionResult fireRuleCondition(Long tenantId, String ruleConditionCode, RuleObject fact) throws RuleExecuteException {
        //todo 默认全部校验构通过
        return new RuleConditionResult(true);
    }

}
