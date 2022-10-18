package org.o2.rule.engine.management.infra.repository.impl;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.core.helper.DateUtil;
import org.o2.core.helper.JsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.rule.engine.management.domain.bo.RuleRedisBO;
import org.o2.rule.engine.management.domain.entity.Rule;
import org.o2.rule.engine.management.domain.repository.RuleRepository;
import org.o2.rule.engine.management.domain.vo.RuleVO;
import org.o2.rule.engine.management.infra.constants.RuleEngineRedisConstants;
import org.o2.rule.engine.management.infra.convert.RuleConverter;
import org.o2.rule.engine.management.infra.mapper.O2reRuleMapper;
import org.springframework.stereotype.Component;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 规则 资源库实现
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
@Component
@Slf4j
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
        long currentTimeMillis = System.currentTimeMillis();
        final Set<String> entityCodes = rules.stream().map(Rule::getEntityCode).collect(Collectors.toSet());

        final String ruleKey = RuleEngineRedisConstants.RedisKey.getRuleKey(tenantId);
        final String ruleEntityUpdateTimeKeyKey = RuleEngineRedisConstants.RedisKey.getRuleEntityUpdateTimeKey(tenantId);

        final Map<String, String> ruleMap = RuleConverter.convertRedisBO(rules).stream()
                .collect(Collectors.toMap(RuleRedisBO::getRuleCode, JsonHelper::objectToString, (r1, r2) -> r1));
        final Map<String, String> entityMap = Maps.newHashMapWithExpectedSize(entityCodes.size());
        for (String entityCode : entityCodes) {
            entityMap.put(entityCode, String.valueOf(currentTimeMillis));
        }

        redisCacheClient.opsForHash().putAll(ruleKey, ruleMap);
        redisCacheClient.opsForHash().putAll(ruleEntityUpdateTimeKeyKey, entityMap);
    }

    @Override
    public void removeCache(Long tenantId, List<Rule> rules) {
        long currentTimeMillis = System.currentTimeMillis();
        final Set<String> entityCodes = rules.stream().map(Rule::getEntityCode).collect(Collectors.toSet());

        final String ruleKey = RuleEngineRedisConstants.RedisKey.getRuleKey(tenantId);
        final String ruleEntityUpdateTimeKeyKey = RuleEngineRedisConstants.RedisKey.getRuleEntityUpdateTimeKey(tenantId);

        final List<String> ruleCodes = rules.stream().map(Rule::getRuleCode).collect(Collectors.toList());
        final Map<String, String> entityMap = Maps.newHashMapWithExpectedSize(entityCodes.size());
        for (String entityCode : entityCodes) {
            entityMap.put(entityCode, String.valueOf(currentTimeMillis));
        }

        redisCacheClient.opsForHash().delete(ruleKey, ruleCodes);
        redisCacheClient.opsForHash().putAll(ruleEntityUpdateTimeKeyKey, entityMap);
    }

    @Override
    public Map<String, Date> getEntitiesUpdateTime(Long tenantId, List<String> entityCodes) {
        final Map<String, Date> result = Maps.newHashMapWithExpectedSize(entityCodes.size());
        final String ruleEntityUpdateTimeKeyKey = RuleEngineRedisConstants.RedisKey.getRuleEntityUpdateTimeKey(tenantId);

        final List<String> timeMills = redisCacheClient.<String, String>opsForHash().multiGet(ruleEntityUpdateTimeKeyKey, entityCodes);
        int i = 0;
        for (String entityCode : entityCodes) {
            try {
                result.put(entityCode, DateUtil.parseToDate(timeMills.get(i)));
            } catch (ParseException e) {
                log.warn("date parse exception : ", e);
            }
            i++;
        }
        return result;
    }
}
