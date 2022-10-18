package org.o2.rule.engine.management.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.rule.engine.management.domain.entity.RuleEntityCondition;

import java.util.List;

/**
 * 规则实体条件资源库
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
public interface RuleEntityConditionRepository extends BaseRepository<RuleEntityCondition> {
    /**
     * 查询规则实体条件列表
     *
     * @param ruleEntityCondition 查询条件
     * @return 规则实体条件列表
     */
    List<RuleEntityCondition> selectList(RuleEntityCondition ruleEntityCondition);

    /**
     * 通过规则实体查询规则实体条件列表
     *
     * @param tenantId 租户id
     * @param ruleEntityCode 规则实体id
     * @return 规则实体条件列表
     */
    List<RuleEntityCondition> selectListByRuleEntityCode(Long tenantId, String ruleEntityCode);
}
