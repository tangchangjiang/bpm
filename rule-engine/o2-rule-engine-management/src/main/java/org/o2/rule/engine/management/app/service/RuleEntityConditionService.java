package org.o2.rule.engine.management.app.service;

import org.o2.rule.engine.management.domain.entity.RuleEntityCondition;


/**
 * 规则实体条件应用服务
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
public interface RuleEntityConditionService {

    /**
     * 保存规则实体条件
     *
     * @param ruleEntityCondition 规则实体条件对象
     * @return 规则实体条件对象
     */
    RuleEntityCondition save(RuleEntityCondition ruleEntityCondition);
}
