package org.o2.rule.engine.management.app.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.code.builder.app.service.CodeBuildService;
import org.o2.core.helper.JsonHelper;
import org.o2.core.helper.TransactionalHelper;
import org.o2.core.helper.UserHelper;
import org.o2.delay.queue.service.DelayQueueService;
import org.o2.rule.engine.management.app.filter.FilterHandlerContext;
import org.o2.rule.engine.management.app.filter.FilterHandlerService;
import org.o2.rule.engine.management.app.filter.RuleConditionFilterChain;
import org.o2.rule.engine.management.app.service.RuleService;
import org.o2.rule.engine.management.domain.dto.RuleConditionDTO;
import org.o2.rule.engine.management.domain.entity.Rule;
import org.o2.rule.engine.management.domain.entity.RuleCondRelEntity;
import org.o2.rule.engine.management.domain.entity.RuleEntityCondition;
import org.o2.rule.engine.management.domain.entity.RuleParam;
import org.o2.rule.engine.management.domain.repository.RuleCondRelEntityRepository;
import org.o2.rule.engine.management.domain.repository.RuleEntityConditionRepository;
import org.o2.rule.engine.management.domain.repository.RuleParamRepository;
import org.o2.rule.engine.management.domain.repository.RuleRepository;
import org.o2.rule.engine.management.domain.vo.RuleConditionVO;
import org.o2.rule.engine.management.domain.vo.RuleVO;
import org.o2.rule.engine.management.infra.constants.RuleEngineConstants;
import org.o2.rule.engine.management.infra.constants.RuleEngineRedisConstants;
import org.springframework.stereotype.Service;
import java.util.*;

import io.choerodon.core.exception.CommonException;

/**
 * 规则应用服务默认实现
 *
 * @author xiang.zhao@hand-china.com 2022-10-10
 */
@Service
public class RuleServiceImpl implements RuleService {

    private final RuleRepository ruleRepository;
    private final CodeBuildService codeBuildService;
    private final DelayQueueService delayQueueService;
    private final TransactionalHelper transactionalHelper;
    private final RuleParamRepository ruleParamRepository;
    private final RuleConditionFilterChain ruleConditionFilterChain;
    private final RuleCondRelEntityRepository ruleCondRelEntityRepository;
    private final RuleEntityConditionRepository ruleEntityConditionRepository;

    public RuleServiceImpl(final RuleRepository ruleRepository,
                           final CodeBuildService codeBuildService,
                           final DelayQueueService delayQueueService,
                           final TransactionalHelper transactionalHelper,
                           final RuleParamRepository ruleParamRepository,
                           final RuleConditionFilterChain ruleConditionFilterChain,
                           final RuleCondRelEntityRepository ruleCondRelEntityRepository,
                           final RuleEntityConditionRepository ruleEntityConditionRepository) {
        this.ruleRepository = ruleRepository;
        this.codeBuildService = codeBuildService;
        this.delayQueueService = delayQueueService;
        this.transactionalHelper = transactionalHelper;
        this.ruleParamRepository = ruleParamRepository;
        this.ruleConditionFilterChain = ruleConditionFilterChain;
        this.ruleCondRelEntityRepository = ruleCondRelEntityRepository;
        this.ruleEntityConditionRepository = ruleEntityConditionRepository;
    }

    @Override
    public Rule detail(Long organizationId, Long ruleId) {
        final Rule rule = ruleRepository.getRuleById(organizationId, ruleId);
        if (Objects.isNull(rule)) {
            throw new CommonException(BaseConstants.ErrorCode.NOT_FOUND);
        }
        this.convertRuleCondition(rule);
        final RuleConditionDTO conditionDTO = rule.getConditionDTO();

        final List<String> conditionCodes = new ArrayList<>();
        final List<String> paramCodes = new ArrayList<>();
        conditionDTO.allCondCodeParamCode(conditionCodes, paramCodes);

        final List<RuleEntityCondition> ruleEntityConditions = ruleEntityConditionRepository.selectByCondition(Condition.builder(RuleEntityCondition.class).andWhere(Sqls.custom()
                .andEqualTo(RuleEntityCondition.FIELD_TENANT_ID, organizationId)
                .andEqualTo(RuleEntityCondition.FIELD_RULE_ENTITY_ID, rule.getRuleEntityId())
                .andIn(RuleEntityCondition.FIELD_CONDITION_CODE, conditionCodes)).build());
        final List<RuleParam> ruleParams = ruleParamRepository.selectByCondition(Condition.builder(RuleParam.class).andWhere(Sqls.custom()
                .andEqualTo(RuleParam.FIELD_TENANT_ID, organizationId)
                .andEqualTo(RuleParam.FIELD_PARAM_REL_ENTITY_ID, rule.getRuleEntityId())
                .andIn(RuleParam.FIELD_PARAM_CODE, paramCodes)).build());

        if (CollectionUtils.isNotEmpty(ruleParams)) {
            for (RuleParam ruleParam : ruleParams) {
                if (StringUtils.isNotBlank(ruleParam.getFilters())) {
                    final FilterHandlerContext context = new FilterHandlerContext(organizationId, rule);
                    final StringJoiner sj = new StringJoiner(BaseConstants.Symbol.COMMA, "{", "}");
                    final List<FilterHandlerService> handlers = ruleConditionFilterChain.getHandlers(ruleParam.getFilters().split(BaseConstants.Symbol.COMMA));
                    for (FilterHandlerService handler : handlers) {
                        sj.add(handler.getHandle() + BaseConstants.Symbol.COLON + handler.filter(context));
                    }
                    ruleParam.setParamFilters(sj.toString());
                }
            }
        }
        conditionDTO.convertCondition(ruleEntityConditions, ruleParams);

        return rule;
    }

    @Override
    public RuleVO detailByCode(Long organizationId, String ruleCode) {

        final RuleVO rule = ruleRepository.getRuleByCode(organizationId, ruleCode);

        RuleConditionVO conditionVO = new RuleConditionVO();
        conditionVO.setConditionExpression(rule.getConditionExpression());
        rule.setCondition(conditionVO);

        return rule;
    }

    @Override
    public Map<String, Date> getEntityUpdateTime(Long organizationId, String ruleCode) {
        if (StringUtils.isBlank(ruleCode) || organizationId == null) {
            return Collections.emptyMap();
        }

        return ruleRepository.getEntitiesUpdateTime(organizationId, Arrays.asList(ruleCode.split(BaseConstants.Symbol.COMMA)));
    }

    @Override
    public Rule createRule(final Long organizationId, final Rule rule) {

        rule.validRule();

        final RuleConditionDTO conditionDTO = rule.getConditionDTO();

        final String ruleCode = codeBuildService.makePrimaryKey(organizationId, UserHelper.getUserId(), RuleEngineConstants.GenerateTypeCode.RULE_CODE);
        // 构建ruleJson和conditionExpression
        rule.setRuleCode(ruleCode);
        rule.buildRule();

        final List<Long> conditionIds = conditionDTO.allConditionId();

        final List<RuleCondRelEntity> ruleCondRelEntities = new ArrayList<>(conditionIds.size());

        if (CollectionUtils.isNotEmpty(conditionIds)) {
            for (Long conditionId : conditionIds) {
                final RuleCondRelEntity ruleCondRelEntity = new RuleCondRelEntity();
                ruleCondRelEntity.setRuleEntityCondId(conditionId);
                ruleCondRelEntity.setRuleCode(ruleCode);
                ruleCondRelEntity.setTenantId(organizationId);
                ruleCondRelEntities.add(ruleCondRelEntity);
            }
        }

        transactionalHelper.transactionOperation(() -> {
            ruleRepository.insertSelective(rule);
            if (CollectionUtils.isNotEmpty(ruleCondRelEntities)) {
                ruleCondRelEntities.forEach(relEntity -> {
                    relEntity.setRuleId(rule.getRuleId());
                });
            }
            ruleCondRelEntityRepository.batchInsert(ruleCondRelEntities);
            this.addExpireEvent(organizationId, rule);
        });
        //保存规则
        return rule;
    }

    @Override
    public Rule updateRule(Long organizationId, Rule rule) {

        return rule;
    }

    @Override
    public void enable(Long tenantId, List<Long> ruleIds) {
        if (tenantId == null || CollectionUtils.isEmpty(ruleIds)) {
            return;
        }
        final List<Rule> rules = ruleRepository.selectByIds(StringUtils.join(ruleIds, BaseConstants.Symbol.COMMA));
        if (CollectionUtils.isEmpty(rules)) {
            return;
        }
        rules.forEach(rule -> {
            rule.setRuleStatus(RuleEngineConstants.RuleStatus.ENABLE);
        });
        transactionalHelper.transactionOperation(() -> {
            ruleRepository.batchUpdateOptional(rules, Rule.FIELD_RULE_STATUS);
            ruleRepository.loadToCache(tenantId, rules);
        });
    }

    @Override
    public void disable(Long tenantId, List<Long> ruleIds) {
        if (tenantId == null || CollectionUtils.isEmpty(ruleIds)) {
            return;
        }
        final List<Rule> rules = ruleRepository.selectByIds(StringUtils.join(ruleIds, BaseConstants.Symbol.COMMA));
        if (CollectionUtils.isEmpty(rules)) {
            return;
        }
        rules.forEach(rule -> {
            rule.setRuleStatus(RuleEngineConstants.RuleStatus.DISABLE);
        });
        transactionalHelper.transactionOperation(() -> {
            ruleRepository.batchUpdateOptional(rules, Rule.FIELD_RULE_STATUS);
            ruleRepository.removeCache(tenantId, rules);
        });
    }

    /**
     * 转化规则Json
     *
     * @param rule 规则
     */
    public void convertRuleCondition(Rule rule) {
        if (StringUtils.isNotBlank(rule.getRuleJson())) {
            final RuleConditionDTO ruleCondition = JsonHelper.stringToObject(rule.getRuleJson(), RuleConditionDTO.class);

            rule.setConditionDTO(ruleCondition);
        }
    }

    public void addExpireEvent(Long tenantId, Rule rule) {
        String id = String.format(RuleEngineRedisConstants.RedisKey.SCENE_ID, tenantId, rule.getRuleCode());
        long timeMillis = rule.getEndTime().getTime() - System.currentTimeMillis();
        delayQueueService.saveDelayMessage(id, id, timeMillis, RuleEngineRedisConstants.RedisKey.SCENE_EXPIRE);
    }
}
