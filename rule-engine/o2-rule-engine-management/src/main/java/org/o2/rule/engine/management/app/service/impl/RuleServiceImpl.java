package org.o2.rule.engine.management.app.service.impl;

import org.o2.code.builder.app.service.CodeBuildService;
import org.o2.core.helper.TransactionalHelper;
import org.o2.core.helper.UserHelper;
import org.o2.rule.engine.management.app.service.RuleService;
import org.o2.rule.engine.management.domain.dto.RuleConditionDTO;
import org.o2.rule.engine.management.domain.entity.Rule;
import org.o2.rule.engine.management.domain.entity.RuleCondRelEntity;
import org.o2.rule.engine.management.domain.repository.RuleCondRelEntityRepository;
import org.o2.rule.engine.management.domain.repository.RuleRepository;
import org.o2.rule.engine.management.infra.constants.RuleEngineConstants;
import org.springframework.stereotype.Service;

/**
 * 规则应用服务默认实现
 *
 * @author xiang.zhao@hand-china.com 2022-10-10
 */
@Service
public class RuleServiceImpl implements RuleService {
                                                                                                
    private final RuleRepository ruleRepository;
    private final CodeBuildService codeBuildService;
    private final TransactionalHelper transactionalHelper;
    private final RuleCondRelEntityRepository ruleCondRelEntityRepository;

    public RuleServiceImpl(final RuleRepository ruleRepository,
                           final CodeBuildService codeBuildService,
                           final TransactionalHelper transactionalHelper,
                           final RuleCondRelEntityRepository ruleCondRelEntityRepository) {
        this.ruleRepository = ruleRepository;
        this.codeBuildService = codeBuildService;
        this.transactionalHelper = transactionalHelper;
        this.ruleCondRelEntityRepository = ruleCondRelEntityRepository;
    }

    @Override
    public Rule createRule(final Long organizationId, final Rule rule) {

        rule.validRule();

        final RuleConditionDTO conditionDTO = rule.getConditionDTO();

        final String ruleCode = codeBuildService.makePrimaryKey(organizationId, UserHelper.getUserId(), RuleEngineConstants.GenerateTypeCode.RULE_ENGINE_CODE);
        // 构建ruleJson和conditionExpression
        rule.setRuleCode(ruleCode);
        rule.buildRule();

        final RuleCondRelEntity ruleCondRelEntity = new RuleCondRelEntity();
        ruleCondRelEntity.setRuleEntityCondId(conditionDTO.getRuleEntityConditionId());
        ruleCondRelEntity.setRuleCode(ruleCode);
        ruleCondRelEntity.setTenantId(organizationId);

        transactionalHelper.transactionOperation(() -> {
            ruleRepository.insertSelective(rule);
            ruleCondRelEntity.setRuleId(rule.getRuleId());

            ruleCondRelEntityRepository.insertSelective(ruleCondRelEntity);
        });
        //保存规则
        return rule;
    }
}
