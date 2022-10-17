package org.o2.rule.engine.management.app.service;

import org.o2.rule.engine.management.domain.entity.Rule;
import org.o2.rule.engine.management.domain.vo.RuleVO;
import java.util.List;

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
     * 查询规则明细
     *
     * @param organizationId 租户id
     * @param ruleCode 规则编码
     * @return 规则对象
     */
    RuleVO detailByCode(Long organizationId, String ruleCode);

    /**
     * 创建规则
     *
     * @param organizationId 租户id
     * @param rule 规则对象
     * @return 规则对象
     */
    Rule createRule(Long organizationId, Rule rule);

    /**
     * 更新规则
     *
     * @param organizationId 租户id
     * @param rule 规则对象
     * @return 规则对象
     */
    Rule updateRule(Long organizationId, Rule rule);

    /**
     * 启用规则
     * @param tenantId 租户ID
     * @param ruleIds 规则id
     */
    void enable(Long tenantId, List<Long> ruleIds);

    /**
     * 禁用规则场景
     * @param tenantId 租户ID
     * @param ruleIds 规则Id
     */
    void disable(Long tenantId, List<Long> ruleIds);
}
