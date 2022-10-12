package org.o2.rule.engine.management.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.rule.engine.management.domain.entity.RuleParam;
import org.o2.rule.engine.management.domain.repository.RuleParamRepository;
import org.springframework.stereotype.Component;

/**
 * 规则参数 资源库实现
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
@Component
public class RuleParamRepositoryImpl extends BaseRepositoryImpl<RuleParam> implements RuleParamRepository {
}
