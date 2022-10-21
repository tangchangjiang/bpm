package org.o2.rule.engine.management.infra.converts;

import org.o2.rule.engine.management.domain.bo.RuleEntityBO;
import org.o2.rule.engine.management.domain.entity.RuleEntity;

/**
 * 转换类
 *
 * @author yuncheng.ma@hand-china.com
 * @since 2022/10/17 15:01
 */
public class RuleEntityConverts {
    public static RuleEntityBO toRuleEntityBO(RuleEntity ruleEntity){

        if (ruleEntity == null) {
            return null;
        }
        RuleEntityBO ruleEntityBO = new RuleEntityBO();
        ruleEntityBO.setRuleEntityCode(ruleEntity.getRuleEntityCode());
        ruleEntityBO.setRuleEntityName(ruleEntity.getRuleEntityName());
        ruleEntityBO.setRuleEntityAlias(ruleEntity.getRuleEntityAlias());
        ruleEntityBO.setEnableFlag(ruleEntity.getEnableFlag());

        return ruleEntityBO;
    }

    private RuleEntityConverts(){}
}
