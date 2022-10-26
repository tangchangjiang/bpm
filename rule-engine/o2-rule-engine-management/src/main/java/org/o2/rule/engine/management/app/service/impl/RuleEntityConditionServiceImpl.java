package org.o2.rule.engine.management.app.service.impl;

import org.hzero.mybatis.helper.UniqueHelper;
import org.o2.rule.engine.management.app.service.RuleEntityConditionService;
import org.o2.rule.engine.management.domain.entity.RuleEntityCondition;
import org.o2.rule.engine.management.domain.repository.RuleEntityConditionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 规则实体条件应用服务默认实现
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
@Service
public class RuleEntityConditionServiceImpl implements RuleEntityConditionService {

    private final RuleEntityConditionRepository ruleEntityConditionRepository;

    public RuleEntityConditionServiceImpl(RuleEntityConditionRepository ruleEntityConditionRepository) {
        this.ruleEntityConditionRepository = ruleEntityConditionRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RuleEntityCondition save(RuleEntityCondition ruleEntityCondition) {
        //保存规则实体条件
        UniqueHelper.valid(ruleEntityCondition, RuleEntityCondition.O2RE_RULE_ENTITY_CONDITION_U1);
        if (ruleEntityCondition.getRuleEntityConditionId() == null) {
            ruleEntityConditionRepository.insertSelective(ruleEntityCondition);
        } else {
            ruleEntityConditionRepository.updateOptional(ruleEntityCondition,
                    RuleEntityCondition.FIELD_CONDITION_NAME,
                    RuleEntityCondition.FIELD_ENABLE_FLAG,
                    RuleEntityCondition.FIELD_DESCRIPTION,
                    RuleEntityCondition.FIELD_ORDER_SEQ,
                    RuleEntityCondition.FIELD_CONDITION_CODE_ALIAS,
                    RuleEntityCondition.FIELD_COMPONENT_CODE
            );
        }

        return ruleEntityCondition;
    }
}
