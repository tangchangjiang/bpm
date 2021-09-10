package org.o2.metadata.infra.redis.impl;

import org.o2.core.helper.JsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.infra.constants.OnlineShopConstants;
import org.o2.metadata.infra.entity.OnlineShop;
import org.o2.metadata.infra.redis.OnlineShopRedis;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 *
 * 网店
 *
 * @author yipeng.zhu@hand-china.com 2021-08-10
 **/
@Component
public class OnlineShopRedisImpl implements OnlineShopRedis {
    private final RedisCacheClient redisCacheClient;

    public OnlineShopRedisImpl(RedisCacheClient redisCacheClient) {
        this.redisCacheClient = redisCacheClient;
    }

    @Override
    public OnlineShop getOnlineShop(String onlineShopCode) {
        String key = OnlineShopConstants.Redis.getOnlineShopKey(onlineShopCode);
        Map<String,String> map = redisCacheClient.<String,String>opsForHash().entries(key);
        return JsonHelper.stringToObject(JsonHelper.mapToString(map), OnlineShop.class);
    }
}
