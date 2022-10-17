package org.o2.rule.engine.management.infra.mapper;

import org.o2.rule.engine.management.domain.entity.RuleEntityCondition;
import io.choerodon.mybatis.common.BaseMapper;

import java.util.List;

/**
 * 规则实体条件Mapper
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
public interface O2reRuleEntityConditionMapper extends BaseMapper<RuleEntityCondition> {
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
     * @param ruleEntityId 规则实体id
     * @param ruleEntityCondition 规则实体
     * @return 规则实体条件列表
     */
    List<RuleEntityCondition> selectListByRuleEntityCode(Long tenantId, Long ruleEntityId, RuleEntityCondition ruleEntityCondition);
}
