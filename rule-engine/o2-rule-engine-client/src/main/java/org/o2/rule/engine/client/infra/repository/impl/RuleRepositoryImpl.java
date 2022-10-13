package org.o2.rule.engine.client.infra.repository.impl;

import org.o2.rule.engine.client.domain.entity.Rule;
import org.o2.rule.engine.client.domain.entity.RuleCondition;
import org.o2.rule.engine.client.domain.repository.RuleRepository;

/**
 * 规则仓库实现类
 *
 * @author wei.cai@hand-china.com
 * @date 2022/10/13
 */
public class RuleRepositoryImpl implements RuleRepository {

    @Override
    public Rule findRuleByCode(Long tenantId, String code) {

        //todo 添加实际逻辑，删除Mock 数据
        final Rule rule = new Rule();
        final RuleCondition ruleCondition = new RuleCondition();
        ruleCondition.setConditionExpression("1 == 1");
        rule.setRuleCode(code);
        rule.setRuleCondition(ruleCondition);

        return rule;
    }

}
