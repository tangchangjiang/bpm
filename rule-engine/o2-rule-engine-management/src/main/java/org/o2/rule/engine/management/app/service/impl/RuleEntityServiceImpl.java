package org.o2.rule.engine.management.app.service.impl;

import org.hzero.mybatis.helper.UniqueHelper;
import org.o2.rule.engine.management.app.service.RuleEntityService;
import org.o2.rule.engine.management.domain.entity.RuleEntity;
import org.o2.rule.engine.management.domain.repository.RuleEntityRepository;
import org.o2.rule.engine.management.infra.converts.RuleEntityConverts;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 规则实体应用服务默认实现
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
@Service
public class RuleEntityServiceImpl implements RuleEntityService {

    private final RuleEntityRepository ruleEntityRepository;

    public RuleEntityServiceImpl(RuleEntityRepository ruleEntityRepository) {
        this.ruleEntityRepository = ruleEntityRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RuleEntity save(RuleEntity ruleEntity) {
        //保存规则实体
        UniqueHelper.valid(ruleEntity, RuleEntity.O2RE_RULE_ENTITY_U1);
        if (ruleEntity.getRuleEntityId() == null) {
            ruleEntityRepository.insertSelective(ruleEntity);
        } else {
            ruleEntityRepository.updateOptional(ruleEntity,
                    RuleEntity.FIELD_RULE_ENTITY_NAME,
                    RuleEntity.FIELD_RULE_ENTITY_ALIAS,
                    RuleEntity.FIELD_ENABLE_FLAG,
                    RuleEntity.FIELD_DESCRIPTION
            );
        }

        ruleEntityRepository.saveRedis(ruleEntity.getTenantId(), RuleEntityConverts.toRuleEntityBO(ruleEntity));

        return ruleEntity;
    }
}
