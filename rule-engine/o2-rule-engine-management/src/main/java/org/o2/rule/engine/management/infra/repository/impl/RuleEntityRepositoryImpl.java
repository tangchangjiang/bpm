package org.o2.rule.engine.management.infra.repository.impl;

import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.core.helper.JsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.rule.engine.management.domain.bo.RuleEntityBO;
import org.o2.rule.engine.management.domain.entity.RuleEntity;
import org.o2.rule.engine.management.domain.repository.RuleEntityRepository;
import org.o2.rule.engine.management.infra.constant.RuleEntityConstants;
import org.o2.rule.engine.management.infra.mapper.O2reRuleEntityMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 规则实体 资源库实现
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
@Component
public class RuleEntityRepositoryImpl extends BaseRepositoryImpl<RuleEntity> implements RuleEntityRepository {

    private final O2reRuleEntityMapper o2reRuleEntityMapper;
    private final RedisCacheClient redisCacheClient;

    public RuleEntityRepositoryImpl(O2reRuleEntityMapper o2reRuleEntityMapper,
                                    RedisCacheClient redisCacheClient) {
        this.o2reRuleEntityMapper = o2reRuleEntityMapper;
        this.redisCacheClient = redisCacheClient;
    }

    @Override
    public List<RuleEntity> selectList(RuleEntity ruleEntity) {
        return o2reRuleEntityMapper.selectList(ruleEntity);
    }

    @Override
    public void saveRedis(Long tenantId, RuleEntityBO ruleEntity) {
        String key = String.format(RuleEntityConstants.RedisKey.RULE_ENTITY, tenantId);
        redisCacheClient.opsForHash().put(key, ruleEntity.getRuleEntityCode(), JsonHelper.objectToString(ruleEntity));
    }

    @Override
    public void batchSaveRedis(Long tenantId, Map<String, String> ruleEntityMap) {
        String key = String.format(RuleEntityConstants.RedisKey.RULE_ENTITY, tenantId);
        redisCacheClient.opsForHash().putAll(key, ruleEntityMap);
    }

    @Override
    public RuleEntity queryRuleEntityByCode(Long tenantId, String ruleEntityCode) {
        RuleEntity query = new RuleEntity();
        query.setTenantId(tenantId);
        query.setRuleEntityCode(ruleEntityCode);
        RuleEntity ruleEntity = this.selectOne(query);
        if (Objects.isNull(ruleEntity) && !Objects.equals(BaseConstants.DEFAULT_TENANT_ID, tenantId)) {
            query.setTenantId(BaseConstants.DEFAULT_TENANT_ID);
            ruleEntity = this.selectOne(query);
        }
        return ruleEntity;
    }
}
