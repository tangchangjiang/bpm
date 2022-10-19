package org.o2.rule.engine.management.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.rule.engine.management.domain.entity.RuleParam;
import org.o2.rule.engine.management.domain.repository.RuleParamRepository;
import org.o2.rule.engine.management.infra.mapper.O2reRuleParamMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 规则参数 资源库实现
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
@Component
public class RuleParamRepositoryImpl extends BaseRepositoryImpl<RuleParam> implements RuleParamRepository {
    private final O2reRuleParamMapper o2reRuleParamMapper;

    public RuleParamRepositoryImpl(O2reRuleParamMapper o2reRuleParamMapper) {
        this.o2reRuleParamMapper = o2reRuleParamMapper;
    }

    @Override
    public List<RuleParam> selectList(RuleParam ruleParam) {
        return o2reRuleParamMapper.selectList(ruleParam);
    }
}
