package org.o2.rule.engine.management.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.rule.engine.management.domain.entity.RuleEntity;
import org.o2.rule.engine.management.domain.repository.RuleEntityRepository;
import org.o2.rule.engine.management.infra.mapper.O2reRuleEntityMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 规则实体 资源库实现
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
@Component
public class RuleEntityRepositoryImpl extends BaseRepositoryImpl<RuleEntity> implements RuleEntityRepository {

    private final O2reRuleEntityMapper o2reRuleEntityMapper;

    public RuleEntityRepositoryImpl(O2reRuleEntityMapper o2reRuleEntityMapper) {
        this.o2reRuleEntityMapper = o2reRuleEntityMapper;
    }

    @Override
    public List<RuleEntity> selectList(RuleEntity ruleEntity) {
        return o2reRuleEntityMapper.selectList(ruleEntity);
    }
}
