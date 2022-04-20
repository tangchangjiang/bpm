package org.o2.metadata.infra.redis.impl;

import lombok.extern.slf4j.Slf4j;
import org.o2.core.helper.JsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.infra.constants.OnlineShopConstants;
import org.o2.metadata.infra.entity.OnlineShop;
import org.o2.metadata.infra.redis.OnlineShopRedis;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 *
 * 网店
 *
 * @author yipeng.zhu@hand-china.com 2021-08-10
 **/
@Component
@Slf4j
public class OnlineShopRedisImpl implements OnlineShopRedis {
    private final RedisCacheClient redisCacheClient;

    public OnlineShopRedisImpl(RedisCacheClient redisCacheClient) {
        this.redisCacheClient = redisCacheClient;
    }

    @Override
    public OnlineShop getOnlineShop(String onlineShopCode,Long tenantId) {
        String key = OnlineShopConstants.Redis.getOnlineShopKey(tenantId);
        Map<String,String> map = redisCacheClient.<String,String>opsForHash().entries(key);
        Set<String> keys = map.keySet();
        if(keys.isEmpty()){
            return new OnlineShop();
        }
        for(String onlineShopCodekey:keys) {
            if(onlineShopCode.equals(onlineShopCodekey)){
                String onlineShopValue = map.get(onlineShopCodekey);
                log.info("getOnlineShop onlineShopValue:{}",onlineShopValue);
                return JsonHelper.stringToObject(onlineShopValue,OnlineShop.class);
            }
        }
        return new OnlineShop();
    }
}
