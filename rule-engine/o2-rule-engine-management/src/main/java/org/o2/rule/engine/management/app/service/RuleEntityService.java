package org.o2.rule.engine.management.app.service;

import org.o2.rule.engine.management.domain.entity.RuleEntity;


/**
 * 规则实体应用服务
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
public interface RuleEntityService {

    /**
     * 保存规则实体
     *
     * @param ruleEntity 规则实体对象
     * @return 规则实体对象
     */
    RuleEntity save(RuleEntity ruleEntity);
}
