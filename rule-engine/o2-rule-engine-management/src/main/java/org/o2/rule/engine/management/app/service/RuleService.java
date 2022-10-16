package org.o2.rule.engine.management.app.service;

import org.o2.rule.engine.management.domain.entity.Rule;


/**
 * 规则应用服务
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
public interface RuleService {


    /**
     * 查询规则明细
     *
     * @param organizationId 租户id
     * @param ruleId 规则id
     * @return 规则对象
     */
    Rule detail(Long organizationId, Long ruleId);

    /**
     * 创建规则
     *
     * @param organizationId 租户id
     * @param rule 规则对象
     * @return 规则对象
     */
    Rule createRule(Long organizationId, Rule rule);
}
