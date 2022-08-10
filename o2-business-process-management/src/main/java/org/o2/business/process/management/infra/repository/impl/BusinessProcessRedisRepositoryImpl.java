package org.o2.business.process.management.infra.repository.impl;

import lombok.RequiredArgsConstructor;
import org.o2.business.process.management.domain.repository.BusinessProcessRedisRepository;
import org.o2.data.redis.client.RedisCacheClient;
import org.springframework.stereotype.Component;

/**
 * @author zhilin.ren@hand-china.com 2022/08/10 15:01
 */
@Component
@RequiredArgsConstructor
public class BusinessProcessRedisRepositoryImpl implements BusinessProcessRedisRepository {


    private final RedisCacheClient redisCacheClient;

    @Override
    public void updateNodeStatus(String key, String hashKey, Integer value) {
        redisCacheClient.opsForHash().put(key, hashKey, value);
    }
}
