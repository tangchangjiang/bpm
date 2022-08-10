package org.o2.business.process.management.infra.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.o2.business.process.management.domain.repository.BusinessProcessRedisRepository;
import org.o2.business.process.management.infra.constant.RedisKeyConstants;
import org.o2.core.helper.JsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

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
                .get(RedisKeyConstants.BusinessProcess.getBusinessProcessKey(tenantId), processCode);
    }

    @Override
    public void updateNodeStatus(String key, String hashKey, Integer value) {
        redisCacheClient.opsForHash().put(key, hashKey, String.valueOf(value));
    }

    @Override
    public Map<String, Integer> listNodeStatus(List<String> keys, Long tenantId) {
        DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(RedisKeyConstants.BusinessNode.UPDATE_REFUND_LUA);
        redisScript.setResultType(String.class);
        String result = redisCacheClient.execute(redisScript, Collections.singletonList(RedisKeyConstants.BusinessNode.getNodeStatusKey(tenantId)), keys);
        if (StringUtils.isBlank(result)) {
            return Collections.emptyMap();
        }
        return JsonHelper.stringToMap(result);
    }
}
