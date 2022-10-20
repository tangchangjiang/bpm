package org.o2.rule.engine.management.infra.convert;

import org.o2.rule.engine.management.domain.bo.RuleRedisBO;
import org.o2.rule.engine.management.domain.entity.Rule;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiang.zhao@hand-chian.com 2022/10/18
 */
public class RuleConverter {

    private RuleConverter() {

    }

    /**
     * 转换RedisBO
     *
     * @param rules 规则
     * @return {@link List}<{@link RuleRedisBO}>
     */
    public static List<RuleRedisBO> convertRedisBO(List<Rule> rules) {
        final List<RuleRedisBO> results = new ArrayList<>(rules.size());
        for (Rule rule : rules) {
            final RuleRedisBO redisBO = new RuleRedisBO();
            redisBO.setRuleId(rule.getRuleId());
            redisBO.setUsedFlag(rule.getUsedFlag());
            redisBO.setRuleCode(rule.getRuleCode());
            redisBO.setRuleName(rule.getRuleName());
            redisBO.setRuleStatus(rule.getRuleStatus());
            redisBO.setRuleEntityId(rule.getRuleEntityId());
            redisBO.setEntityCode(rule.getEntityCode());
            redisBO.setRuleEntityAlias(rule.getRuleEntityAlias());
            redisBO.setStartTime(rule.getStartTime());
            redisBO.setEndTime(rule.getEndTime());
            redisBO.setConditionExpression(rule.getConditionExpression());
            redisBO.setRuleDescription(rule.getRuleDescription());
            redisBO.setTenantId(rule.getTenantId());
            results.add(redisBO);
        }
        return results;
    }
}
