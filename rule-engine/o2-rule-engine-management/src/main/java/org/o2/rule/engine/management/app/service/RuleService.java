package org.o2.rule.engine.management.app.service;

import org.o2.rule.engine.management.domain.entity.Rule;
import java.util.List;


/**
 * 规则应用服务
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
public interface RuleService {

    
    /**
     * 批量保存规则
     *
     * @param ruleList 规则对象列表
     * @return 规则对象列表
     */
    List<Rule> batchSave(List<Rule> ruleList);


    /**
     * 保存规则
     *
     * @param rule 规则对象
     * @return 规则对象
     */
    Rule save(Rule rule);
}
