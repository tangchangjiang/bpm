package org.o2.business.process.management.infra.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.o2.business.process.management.domain.repository.BusinessProcessRedisRepository;
import org.o2.business.process.management.infra.constant.BusinessProcessRedisConstants;
import org.o2.core.helper.JsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zhilin.ren@hand-china.com 2022/08/10 15:01
 */
@Component
public class BusinessProcessRedisRepositoryImpl implements BusinessProcessRedisRepository {

    private final RedisCacheClient redisCacheClient;

    public BusinessProcessRedisRepositoryImpl(RedisCacheClient redisCacheClient) {
        this.redisCacheClient = redisCacheClient;
    }

    @Override
    public String getBusinessProcessConfig(String processCode, Long tenantId) {
        return redisCacheClient.<String, String>opsForHash()
                .get(BusinessProcessRedisConstants.BusinessProcess.getBusinessProcessKey(tenantId), processCode);
    }

    @Override
    public void updateNodeStatus(String key, String hashKey, Integer value) {
        redisCacheClient.opsForHash().put(key, hashKey, String.valueOf(value));
    }

    @Override
    public Map<String, String> listNodeStatus(List<String> keys, Long tenantId) {
        RedisScript<String> redisScript = BusinessProcessRedisConstants.BusinessProcessLua.LIST_PROCESS_NODE_STATUS;
        String result = redisCacheClient.execute(redisScript,
                Collections.singletonList(BusinessProcessRedisConstants.BusinessNode.getNodeStatusKey(tenantId)), keys.toArray());
        if (StringUtils.isBlank(result) || BusinessProcessRedisConstants.LUA_NULL_MAP.equals(result)) {
            return Collections.emptyMap();
        }
        return JsonHelper.stringToMap(result);
    }

    @Override
    public void updateProcessConfig(String fieldKey, String configJson, Long tenantId) {
        List<String> keys = new ArrayList<>();
        keys.add(BusinessProcessRedisConstants.BusinessProcess.getBusinessProcessKey(tenantId));
        keys.add(BusinessProcessRedisConstants.BusinessProcess.getProcessLastModifiedTimeKey(tenantId));

        List<String> params = new ArrayList<>();
        params.add(fieldKey);
        params.add(configJson);
        params.add(String.valueOf(System.currentTimeMillis()));

        RedisScript<String> redisScript = BusinessProcessRedisConstants.BusinessProcessLua.BUSINESS_PROCESS_CONFIG_UPDATE_LUA;
        redisCacheClient.execute(redisScript, keys, params.toArray());
    }

    @Override
    public void batchUpdateNodeStatus(Long tenantId, Map<String, String> detailMap) {
        redisCacheClient.opsForHash().putAll(BusinessProcessRedisConstants.BusinessNode.getNodeStatusKey(tenantId), detailMap);
    }

    @Override
    public void batchUpdateProcessConfig(Long tenantId, Map<String, String> detailMap) {
        List<String> keys = new ArrayList<>();
        keys.add(BusinessProcessRedisConstants.BusinessProcess.getBusinessProcessKey(tenantId));
        keys.add(BusinessProcessRedisConstants.BusinessProcess.getProcessLastModifiedTimeKey(tenantId));

        List<String> params = new ArrayList<>();
        params.add(JsonHelper.mapToString(detailMap));
        params.add(String.valueOf(System.currentTimeMillis()));

        RedisScript<String> redisScript = BusinessProcessRedisConstants.BusinessProcessLua.BUSINESS_PROCESS_CONFIG_BATCH_UPDATE_LUA;
        redisCacheClient.execute(redisScript, keys, params.toArray());
    }

    @Override
    public String getProcessLastUpdateTime(String processCode, Long tenantId) {
        return redisCacheClient.<String, String>opsForHash().get(BusinessProcessRedisConstants.BusinessProcess.getProcessLastModifiedTimeKey(tenantId), processCode);
    }
}
