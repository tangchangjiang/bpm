package org.o2.process.domain.engine.process.calculator;

import io.choerodon.core.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.o2.process.domain.infra.ProcessEngineConstants;
import org.o2.rule.engine.client.RuleEngineClient;
import org.o2.rule.engine.client.app.exception.RuleExecuteException;
import org.o2.rule.engine.client.domain.RuleConditionResult;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/9/30 16:40
 */
@Slf4j
@Service
public class QLExpressCalculator implements ExpressionCalculator{

    private final RuleEngineClient ruleEngineClient;

    public QLExpressCalculator(RuleEngineClient ruleEngineClient) {
        this.ruleEngineClient = ruleEngineClient;
    }

    @Override
    public boolean calculate(String ruleConditionCode, Map<String, Object> dataMap, Long tenantId) {

        ProcessObject processObject = new ProcessObject();
        processObject.setDataMap(dataMap);

        RuleConditionResult conditionResult;
        try {
            conditionResult = ruleEngineClient.fireRuleCondition(tenantId, ruleConditionCode, processObject);
        } catch (RuleExecuteException e) {
            throw new CommonException(e);
        }
        return conditionResult.isConditionMatch();
    }

    @Override
    public String getExpressType() {
        return ProcessEngineConstants.ExpressType.QL_EXPRESS;
    }
}
