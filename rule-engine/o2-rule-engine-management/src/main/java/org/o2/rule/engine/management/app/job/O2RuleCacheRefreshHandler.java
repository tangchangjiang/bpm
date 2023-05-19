package org.o2.rule.engine.management.app.job;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.core.O2CoreConstants;
import org.o2.core.helper.JsonHelper;
import org.o2.core.thread.ThreadJobPojo;
import org.o2.metadata.management.client.SQLLovClient;
import org.o2.rule.engine.management.domain.bo.RuleEntityBO;
import org.o2.rule.engine.management.domain.bo.RuleQueryBO;
import org.o2.rule.engine.management.domain.entity.Rule;
import org.o2.rule.engine.management.domain.entity.RuleEntity;
import org.o2.rule.engine.management.domain.repository.RuleEntityRepository;
import org.o2.rule.engine.management.domain.repository.RuleRepository;
import org.o2.rule.engine.management.infra.constants.RuleEngineConstants;
import org.o2.rule.engine.management.infra.converts.RuleEntityConverts;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 规则缓存刷新job
 *
 * @author yuncheng.ma@hand-china.com
 * @since 2022-11-02 09:52:02
 */
@Slf4j
@JobHandler("o2RuleCacheRefreshHandler")
public class O2RuleCacheRefreshHandler extends AbstractRuleBatchThreadJob {

    private static final String RULE_CODE = "ruleCodes";

    private final RuleRepository ruleRepository;
    private final RuleEntityRepository ruleEntityRepository;

    public O2RuleCacheRefreshHandler(RuleRepository ruleRepository,
                                     RuleEntityRepository ruleEntityRepository,
                                     SQLLovClient sqlLovClient) {
        super(sqlLovClient);
        this.ruleRepository = ruleRepository;
        this.ruleEntityRepository = ruleEntityRepository;
    }

    @Override
    public void doExecute(SchedulerTool tool, List<Long> data, ThreadJobPojo threadJobPojo) {
        // job参数获取
        Map<String, String> map = Optional.ofNullable(threadJobPojo.getJobParams()).orElse(new HashMap<>());
        String tenantIdParam = map.get(O2CoreConstants.EntityDomain.FIELD_TENANT_ID);
        // 如果设置了租户Id，则不查询所有租户
        if (StringUtils.isNotBlank(tenantIdParam)) {
            data = Collections.singletonList(Long.valueOf(tenantIdParam));
        }

        for (Long tenantId : data) {
            try {
                refreshRuleCache(map, tenantId);
            } catch (Exception ex) {
                log.error("refresh rule cache failed, param: {}, tenantId: {}", JsonHelper.objectToString(map), tenantId, ex);
            }
        }
    }

    protected void refreshRuleCache(Map<String, String> map, Long tenantId) {
        String ruleCodes = map.getOrDefault(RULE_CODE, null);
        final List<String> ruleCodeList = StringUtils.isEmpty(ruleCodes) ? Collections.emptyList() :
                Arrays.asList(ruleCodes.split(BaseConstants.Symbol.COMMA));
        log.info("Rule cache refresh, tenantId : {}", tenantId);
        log.info("Rule cache refresh, ruleCodes :{}", ruleCodeList);

        // 查询规则实体
        List<RuleEntity> ruleEntities = ruleEntityRepository.selectByCondition(Condition.builder(RuleEntity.class)
                .andWhere(Sqls.custom().andEqualTo(RuleEntity.FIELD_ENABLE_FLAG, BaseConstants.Flag.YES)
                        .andEqualTo(RuleEntity.FIELD_TENANT_ID, tenantId)).build());
        // 查询规则
        List<Rule> ruleList = ruleRepository.selectByCondition(Condition.builder(Rule.class)
                .andWhere(Sqls.custom().andEqualTo(Rule.FIELD_TENANT_ID, tenantId)
                        .andIn(Rule.FIELD_RULE_CODE, ruleCodeList, true)
                        .andEqualTo(Rule.FIELD_RULE_STATUS, RuleEngineConstants.RuleStatus.ENABLE)).build());

        if (log.isDebugEnabled()) {
            log.debug("rule entity : {}", ruleEntities);
            log.debug("rule : {}", ruleList);
        }

        if (CollectionUtils.isNotEmpty(ruleEntities)) {
            Map<String, String> ruleEntityMap = new HashMap<>(ruleEntities.size());
            for (RuleEntity ruleEntity : ruleEntities) {
                RuleEntityBO ruleEntityBO = RuleEntityConverts.toRuleEntityBO(ruleEntity);
                ruleEntityMap.put(ruleEntity.getRuleEntityCode(), JsonHelper.objectToString(ruleEntityBO));
            }
            ruleEntityRepository.batchSaveRedis(tenantId, ruleEntityMap);
        }

        if (CollectionUtils.isNotEmpty(ruleList)) {
            List<Long> ids = ruleList.stream().map(Rule::getRuleId).collect(Collectors.toList());
            RuleQueryBO query = new RuleQueryBO(tenantId, ids);
            final List<Rule> rules = ruleRepository.findRuleList(query);
            ruleRepository.loadToCache(tenantId, rules);
        }
    }
}
