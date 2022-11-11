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
import org.o2.rule.engine.management.domain.bo.RuleQueryBO;
import org.o2.rule.engine.management.domain.bo.RuleRedisBO;
import org.o2.rule.engine.management.domain.dto.RuleConditionDTO;
import org.o2.rule.engine.management.domain.entity.*;
import org.o2.rule.engine.management.domain.repository.*;
import org.o2.rule.engine.management.domain.vo.RuleVO;
import org.o2.rule.engine.management.infra.constants.RuleEngineConstants;
import org.o2.rule.engine.management.infra.constants.RuleEngineRedisConstants;
import org.o2.rule.engine.management.infra.convert.RuleConverter;
import org.o2.user.helper.IamUserHelper;
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
    private final RuleEntityRepository ruleEntityRepository;
    private final RuleConditionFilterChain ruleConditionFilterChain;
    private final RuleCondRelEntityRepository ruleCondRelEntityRepository;
    private final RuleEntityConditionRepository ruleEntityConditionRepository;

    public RuleServiceImpl(final RuleRepository ruleRepository,
                           final CodeBuildService codeBuildService,
                           final DelayQueueService delayQueueService,
                           final TransactionalHelper transactionalHelper,
                           final RuleParamRepository ruleParamRepository,
                           RuleEntityRepository ruleEntityRepository, final RuleConditionFilterChain ruleConditionFilterChain,
                           final RuleCondRelEntityRepository ruleCondRelEntityRepository,
                           final RuleEntityConditionRepository ruleEntityConditionRepository) {
        this.ruleRepository = ruleRepository;
        this.codeBuildService = codeBuildService;
        this.delayQueueService = delayQueueService;
        this.transactionalHelper = transactionalHelper;
        this.ruleParamRepository = ruleParamRepository;
        this.ruleEntityRepository = ruleEntityRepository;
        this.ruleConditionFilterChain = ruleConditionFilterChain;
        this.ruleCondRelEntityRepository = ruleCondRelEntityRepository;
        this.ruleEntityConditionRepository = ruleEntityConditionRepository;
    }

    @Override
    public Rule detail(Long organizationId, Long ruleId, String ruleCode) {
        final Rule rule = ruleRepository.getRuleDetail(organizationId, ruleId, ruleCode);
        if (Objects.isNull(rule)) {
            throw new CommonException(BaseConstants.ErrorCode.NOT_FOUND);
        }
        rule.setUpdateUserName(IamUserHelper.getRealName(rule.getLastUpdatedBy().toString()));
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

        final RuleRedisBO redisRule = ruleRepository.getCacheRuleByCode(organizationId, ruleCode);

        if (redisRule == null) {
            return null;
        }

        return RuleConverter.convertRedisBo2Vo(redisRule);
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

        rule.init();
        rule.validRule();

        final RuleEntity query = new RuleEntity();
        query.setTenantId(organizationId);
        query.setRuleEntityCode(rule.getEntityCode());
        final RuleEntity ruleEntity = ruleEntityRepository.selectOne(query);

        final RuleConditionDTO conditionDTO = rule.getConditionDTO();

        final String ruleCode = codeBuildService.makePrimaryKey(organizationId, UserHelper.getUserId(), RuleEngineConstants.GenerateTypeCode.RULE_CODE);
        // 构建ruleJson和conditionExpression
        rule.setRuleCode(ruleCode);
        rule.setRuleEntityAlias(ruleEntity.getRuleEntityAlias());

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
                ruleCondRelEntityRepository.batchInsert(ruleCondRelEntities);
            }
            this.addExpireEvent(organizationId, rule);
        });
        //保存规则
        return rule;
    }

    @Override
    public Rule updateRule(Long organizationId, Rule rule) {
        if (!RuleEngineConstants.RuleStatus.NEW.equals(rule.getRuleStatus())) {
            rule.setRuleStatus(RuleEngineConstants.RuleStatus.MODIFIED);
        }

        rule.buildRule();

        final RuleConditionDTO conditionDTO = rule.getConditionDTO();
        final List<Long> conditionIds = conditionDTO.allConditionId();

        final List<RuleCondRelEntity> ruleCondRelEntities = new ArrayList<>(conditionIds.size());
        final List<RuleCondRelEntity> existRelEntities = ruleCondRelEntityRepository.selectByCondition(Condition.builder(RuleCondRelEntity.class).andWhere(Sqls.custom()
                .andEqualTo(RuleCondRelEntity.FIELD_RULE_ID, rule.getRuleId())).build());

        if (CollectionUtils.isNotEmpty(conditionIds)) {
            for (Long conditionId : conditionIds) {
                final RuleCondRelEntity ruleCondRelEntity = new RuleCondRelEntity();
                ruleCondRelEntity.setRuleEntityCondId(conditionId);
                ruleCondRelEntity.setRuleCode(rule.getRuleCode());
                ruleCondRelEntity.setRuleId(rule.getRuleId());
                ruleCondRelEntity.setTenantId(organizationId);
                ruleCondRelEntities.add(ruleCondRelEntity);
            }
        }

        transactionalHelper.transactionOperation(() -> {
            ruleRepository.updateByPrimaryKey(rule);
            if (CollectionUtils.isNotEmpty(ruleCondRelEntities)) {
                ruleCondRelEntityRepository.batchInsert(ruleCondRelEntities);
            }
            if (CollectionUtils.isNotEmpty(existRelEntities)) {
                ruleCondRelEntityRepository.batchDelete(ruleCondRelEntities);
            }
        });
        return rule;
    }

    @Override
    public void enable(Long tenantId, List<Long> ruleIds) {
        if (tenantId == null || CollectionUtils.isEmpty(ruleIds)) {
            return;
        }
        final RuleQueryBO query = new RuleQueryBO(tenantId, ruleIds);
        final List<Rule> rules = ruleRepository.findRuleList(query);
        if (CollectionUtils.isEmpty(rules)) {
            return;
        }
        this.enableRules(rules, tenantId, BaseConstants.Flag.NO);
    }

    @Override
    public void useRule(Long tenantId, List<String> ruleCodes) {
        if (tenantId == null || CollectionUtils.isEmpty(ruleCodes)) {
            return;
        }
        final RuleQueryBO query = new RuleQueryBO(tenantId);
        query.setRuleCodes(ruleCodes);

        final List<Rule> rules = ruleRepository.findRuleList(query);
        if (CollectionUtils.isEmpty(rules)) {
            return;
        }

        this.enableRules(rules, tenantId, BaseConstants.Flag.YES);
    }

    private void enableRules(List<Rule> rules, Long tenantId, Integer usedFlag) {
        rules.forEach(rule -> {
            rule.setRuleStatus(RuleEngineConstants.RuleStatus.ENABLE);
            if (usedFlag != null) {
                rule.setUsedFlag(usedFlag);
            }
        });
        transactionalHelper.transactionOperation(() -> {
            ruleRepository.batchUpdateOptional(rules, Rule.FIELD_RULE_STATUS, Rule.FIELD_USED_FLAG);
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
        if (null == rule.getEndTime()) {
            return;
        }
        String id = String.format(RuleEngineRedisConstants.RedisKey.SCENE_ID, tenantId, rule.getRuleCode());
        long timeMillis = rule.getEndTime().getTime() - System.currentTimeMillis();
        delayQueueService.saveDelayMessage(id, id, timeMillis, RuleEngineRedisConstants.RedisKey.SCENE_EXPIRE);
    }
}