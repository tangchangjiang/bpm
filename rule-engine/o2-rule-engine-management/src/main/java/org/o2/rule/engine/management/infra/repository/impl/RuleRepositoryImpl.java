package org.o2.rule.engine.management.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.core.helper.JsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.rule.engine.management.domain.entity.Rule;
import org.o2.rule.engine.management.domain.repository.RuleRepository;
import org.o2.rule.engine.management.domain.vo.RuleVO;
import org.o2.rule.engine.management.infra.constants.RuleEngineRedisConstants;
import org.o2.rule.engine.management.infra.mapper.O2reRuleMapper;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 规则 资源库实现
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
@Component
public class RuleRepositoryImpl extends BaseRepositoryImpl<Rule> implements RuleRepository {
    private final O2reRuleMapper o2reRuleMapper;
    private final RedisCacheClient redisCacheClient;

    public RuleRepositoryImpl(final O2reRuleMapper o2reRuleMapper,
                              final RedisCacheClient redisCacheClient) {
        this.o2reRuleMapper = o2reRuleMapper;
        this.redisCacheClient = redisCacheClient;
    }

    @Override
    public Rule getRuleById(Long tenantId, Long ruleId) {
        return o2reRuleMapper.getRuleById(tenantId, ruleId);
    }

    @Override
    public RuleVO getRuleByCode(Long tenantId, String ruleCode) {
        return o2reRuleMapper.getRuleByCode(tenantId, ruleCode);
    }

    @Override
    public void loadToCache(Long tenantId, List<Rule> rules) {
        final String ruleKey = RuleEngineRedisConstants.RedisKey.getRuleKey(tenantId);
        final Map<String, String> ruleMap = rules.stream().collect(Collectors.toMap(Rule::getRuleCode, JsonHelper::objectToString, (r1, r2) -> r1));
        redisCacheClient.opsForHash().putAll(ruleKey, ruleMap);
    }

    @Override
    public void removeCache(Long tenantId, List<Rule> rules) {
        final String ruleKey = RuleEngineRedisConstants.RedisKey.getRuleKey(tenantId);
        final List<String> ruleCodes = rules.stream().map(Rule::getRuleCode).collect(Collectors.toList());
        redisCacheClient.opsForHash().delete(ruleKey, ruleCodes);
    }
}
