package org.o2.rule.engine.management.infra.convert;

import org.hzero.core.base.BaseConstants;
import org.o2.rule.engine.management.domain.bo.RuleRedisBO;
import org.o2.rule.engine.management.domain.entity.Rule;
import org.o2.rule.engine.management.domain.vo.RuleConditionVO;
import org.o2.rule.engine.management.domain.vo.RuleVO;
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

    /**
     * 转换RedisBO
     *
     * @param redisBO 规则
     * @return {@link List}<{@link RuleRedisBO}>
     */
    public static RuleVO convertRedisBo2Vo(RuleRedisBO redisBO) {
        final RuleVO ruleVO = new RuleVO();
        final RuleConditionVO conditionVO = new RuleConditionVO();
        conditionVO.setConditionExpression(redisBO.getConditionExpression());

        ruleVO.setRuleId(redisBO.getRuleId());
        ruleVO.setRuleCode(redisBO.getRuleCode());
        ruleVO.setTenantId(redisBO.getTenantId());
        ruleVO.setRuleName(redisBO.getRuleName());
        ruleVO.setRuleEntityAlias(redisBO.getRuleEntityAlias());
        ruleVO.setEnableFlag(BaseConstants.Flag.YES);
        ruleVO.setEntityCode(redisBO.getEntityCode());
        ruleVO.setStartTime(redisBO.getStartTime());
        ruleVO.setEndTime(redisBO.getEndTime());
        ruleVO.setCondition(conditionVO);

        return ruleVO;
    }
}
