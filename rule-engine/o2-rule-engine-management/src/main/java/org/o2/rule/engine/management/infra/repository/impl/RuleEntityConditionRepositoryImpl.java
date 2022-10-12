package org.o2.rule.engine.management.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.rule.engine.management.domain.entity.RuleEntityCondition;
import org.o2.rule.engine.management.domain.repository.RuleEntityConditionRepository;
import org.springframework.stereotype.Component;

/**
 * 规则实体条件 资源库实现
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
@Component
public class RuleEntityConditionRepositoryImpl extends BaseRepositoryImpl<RuleEntityCondition> implements RuleEntityConditionRepository {
}
