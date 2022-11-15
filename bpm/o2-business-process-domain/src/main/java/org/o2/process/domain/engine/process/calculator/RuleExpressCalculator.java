package org.o2.process.domain.engine.process.calculator;

import io.choerodon.core.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.o2.rule.engine.client.RuleEngineClient;
import org.o2.rule.engine.client.app.exception.RuleExecuteException;
import org.o2.rule.engine.client.domain.RuleConditionResult;
import org.o2.rule.engine.client.domain.RuleObject;
import org.springframework.stereotype.Service;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/9/30 16:40
 */
@Slf4j
@Service
public class RuleExpressCalculator implements ExpressionCalculator {

    private final RuleEngineClient ruleEngineClient;

    public RuleExpressCalculator(RuleEngineClient ruleEngineClient) {
        this.ruleEngineClient = ruleEngineClient;
    }

    @Override
    public boolean calculate(String ruleCode, RuleObject ruleObject, Long tenantId) {

        RuleConditionResult conditionResult;
        try {
            conditionResult = ruleEngineClient.fireRuleCondition(tenantId, ruleCode, ruleObject);
        } catch (RuleExecuteException e) {
            throw new CommonException(e);
        }
        return conditionResult.isConditionMatch();
    }

}
