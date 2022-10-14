package org.o2.rule.engine.management.app.service;

import org.o2.rule.engine.management.domain.entity.RuleEntityCondition;
import java.util.List;


/**
 * 规则实体条件应用服务
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
public interface RuleEntityConditionService {

    
    /**
     * 批量保存规则实体条件
     *
     * @param ruleEntityConditionList 规则实体条件对象列表
     * @return 规则实体条件对象列表
     */
    List<RuleEntityCondition> batchSave(List<RuleEntityCondition> ruleEntityConditionList);


    /**
     * 保存规则实体条件
     *
     * @param ruleEntityCondition 规则实体条件对象
     * @return 规则实体条件对象
     */
    RuleEntityCondition save(RuleEntityCondition ruleEntityCondition);
}
