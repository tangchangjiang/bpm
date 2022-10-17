package org.o2.rule.engine.management.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.rule.engine.management.domain.entity.Rule;
import org.o2.rule.engine.management.domain.repository.RuleRepository;
import org.o2.rule.engine.management.domain.vo.RuleVO;
import org.o2.rule.engine.management.infra.mapper.O2reRuleMapper;
import org.springframework.stereotype.Component;

/**
 * 规则 资源库实现
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
@Component
public class RuleRepositoryImpl extends BaseRepositoryImpl<Rule> implements RuleRepository {
    private final O2reRuleMapper o2reRuleMapper;

    public RuleRepositoryImpl(final O2reRuleMapper o2reRuleMapper) {
        this.o2reRuleMapper = o2reRuleMapper;
    }

    @Override
    public RuleVO getRuleByCode(Long tenantId, String ruleCode) {
        return o2reRuleMapper.getRuleByCode(tenantId, ruleCode);
    }
}
