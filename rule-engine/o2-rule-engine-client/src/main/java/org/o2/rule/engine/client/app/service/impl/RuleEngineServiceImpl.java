package org.o2.rule.engine.client.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.o2.rule.engine.client.app.exception.RuleExecuteException;
import org.o2.rule.engine.client.app.service.RuleEngineService;
import org.o2.rule.engine.client.app.service.RuleObjectService;
import org.o2.rule.engine.client.domain.RuleConditionResult;
import org.o2.rule.engine.client.domain.RuleObject;
import org.o2.rule.engine.client.domain.entity.Rule;
import org.o2.rule.engine.client.domain.repository.RuleRepository;
import org.o2.rule.engine.client.infra.constant.RuleClientConstants;
import org.o2.rule.engine.client.infra.rule.ConditionHelper;
import org.o2.rule.engine.client.infra.rule.RuleExecuteContext;

/**
 * 规则Service 实现类
 *
 * @author wei.cai@hand-china.com
 * @date 2022/10/12
 */
@Slf4j
public class RuleEngineServiceImpl implements RuleEngineService {

    private final RuleRepository ruleRepository;
    private final RuleObjectService ruleObjectService;

    /**
     * 构造方法
     *
     * @param ruleRepository    ruleRepository
     * @param ruleObjectService ruleObjectService
     */
    public RuleEngineServiceImpl(final RuleRepository ruleRepository,
                                 final RuleObjectService ruleObjectService) {
        this.ruleRepository = ruleRepository;
        this.ruleObjectService = ruleObjectService;
    }

    @Override
    public RuleConditionResult fireRuleCondition(Long tenantId, String ruleConditionCode, RuleObject fact) throws RuleExecuteException {
        final Rule rule = ruleRepository.findRuleByCode(tenantId, ruleConditionCode);

        if (null == rule
                || null == rule.getRuleCondition()
                || StringUtils.isEmpty(rule.getRuleCondition().getConditionExpression())) {
            throw new RuleExecuteException(RuleClientConstants.ErrorMessage.RULE_CONDITION_IS_NULL, ruleConditionCode);
        }

        final RuleExecuteContext<String, Object> context = ruleObjectService.generateContext(tenantId, fact);

        try {
            final boolean result = ConditionHelper.matchCondition(rule.getRuleCondition().getConditionExpression(), context);
            return new RuleConditionResult(result);
        } catch (Exception e) {
            throw new RuleExecuteException(RuleClientConstants.ErrorMessage.RULE_EXECUTE_EXCEPTION, e,
                    ruleConditionCode,
                    rule.getRuleCondition().getConditionExpression(),
                    e.getMessage());
        }
    }
}
