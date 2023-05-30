package org.o2.rule.engine.management.infra.repository.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.core.helper.QueryFallbackHelper;
import org.o2.rule.engine.management.domain.entity.RuleEntityCondition;
import org.o2.rule.engine.management.domain.entity.RuleParam;
import org.o2.rule.engine.management.domain.repository.RuleEntityConditionRepository;
import org.o2.rule.engine.management.infra.mapper.O2reRuleEntityConditionMapper;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 规则实体条件 资源库实现
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
@Component
public class RuleEntityConditionRepositoryImpl extends BaseRepositoryImpl<RuleEntityCondition> implements RuleEntityConditionRepository {
    private final O2reRuleEntityConditionMapper o2reRuleEntityConditionMapper;

    public RuleEntityConditionRepositoryImpl(O2reRuleEntityConditionMapper o2reRuleEntityConditionMapper) {
        this.o2reRuleEntityConditionMapper = o2reRuleEntityConditionMapper;
    }

    @Override
    public List<RuleEntityCondition> selectList(RuleEntityCondition ruleEntityCondition) {
        return o2reRuleEntityConditionMapper.selectList(ruleEntityCondition);
    }

    @Override
    public List<RuleEntityCondition> selectListByRuleEntityCode(Long tenantId, String ruleEntityCode, RuleEntityCondition ruleEntityCondition) {
        List<RuleEntityCondition> ruleEntityConditionList = QueryFallbackHelper.siteFallback(tenantId, tenant -> o2reRuleEntityConditionMapper.selectListByRuleEntityCode(tenant, ruleEntityCode, ruleEntityCondition), Boolean.TRUE);
        for (RuleEntityCondition entityCondition : ruleEntityConditionList) {
            List<RuleParam> paramList = entityCondition.getRuleParams();
            if (CollectionUtils.isNotEmpty(paramList)) {
                entityCondition.setRuleParams(paramList.stream().sorted(Comparator.comparing(RuleParam::getOrderSeq)).collect(Collectors.toList()));
            }
        }
        return ruleEntityConditionList;
    }
}
