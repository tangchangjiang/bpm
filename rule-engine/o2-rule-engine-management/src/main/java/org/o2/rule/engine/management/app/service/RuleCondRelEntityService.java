package org.o2.rule.engine.management.app.service;

import org.o2.rule.engine.management.domain.entity.RuleCondRelEntity;
import java.util.List;

/**
 * 规则关联条件应用服务
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
public interface RuleCondRelEntityService {

    
    /**
     * 批量保存规则关联条件
     *
     * @param ruleCondRelEntityList 规则关联条件对象列表
     * @return 规则关联条件对象列表
     */
    List<RuleCondRelEntity> batchSave(List<RuleCondRelEntity> ruleCondRelEntityList);


    /**
     * 保存规则关联条件
     *
     * @param ruleCondRelEntity 规则关联条件对象
     * @return 规则关联条件对象
     */
    RuleCondRelEntity save(RuleCondRelEntity ruleCondRelEntity);
}
