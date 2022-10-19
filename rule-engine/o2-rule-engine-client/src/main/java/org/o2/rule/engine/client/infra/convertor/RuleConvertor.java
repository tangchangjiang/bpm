package org.o2.rule.engine.client.infra.convertor;

import org.o2.rule.engine.client.domain.entity.Rule;
import org.o2.rule.engine.client.domain.entity.RuleCondition;
import org.o2.rule.engine.client.domain.vo.RuleVO;

/**
 * 规则转换
 *
 * @author wei.cai@hand-china.com
 * @date 2022/10/17
 */
public class RuleConvertor {

    private RuleConvertor() {

    }

    /**
     * 转为为Rule
     * @param ruleVO ruleVO
     * @return 返回Rule
     */
    public static Rule convertToRule(RuleVO ruleVO) {
        if (ruleVO == null || ruleVO.getCondition() == null) {
            return null;
        }

        final Rule rule = new Rule();
        final RuleCondition ruleCondition = new RuleCondition();
        ruleCondition.setConditionExpression(ruleVO.getCondition().getConditionExpression());
        rule.setRuleCode(ruleVO.getRuleCode());
        rule.setEntityCode(ruleVO.getRuleEntityCode());
        rule.setEntityAlias(ruleVO.getRuleEntityAlias());
        rule.setRuleCondition(ruleCondition);
        return rule;
    }

}
