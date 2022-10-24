package org.o2.rule.engine.client.domain.repository;

import org.o2.rule.engine.client.domain.entity.Rule;

/**
 * 规则仓库
 *
 * @author wei.cai@hand-china.com
 * @date 2022/10/13
 */
public interface RuleRepository {

    /**
     * 查找规则，默认查询指定租户，没有的话查询0租户
     * @param tenantId 租户ID
     * @param code 编码
     * @return 返回规则
     */
    Rule findRuleByCode(Long tenantId, String code);

}
