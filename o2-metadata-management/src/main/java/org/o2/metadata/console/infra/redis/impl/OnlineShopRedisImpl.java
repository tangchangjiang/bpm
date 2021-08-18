package org.o2.metadata.console.infra.redis.impl;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.o2.core.helper.FastJsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.console.api.dto.OnlineShopQueryInnerDTO;
import org.o2.metadata.console.app.bo.OnlineShopCacheBO;
import org.o2.metadata.console.infra.constant.OnlineShopConstants;
import org.o2.metadata.console.infra.convertor.OnlineShopConverter;
import org.o2.metadata.console.infra.entity.OnlineShop;
import org.o2.metadata.console.infra.entity.OnlineShopRelWarehouse;
import org.o2.metadata.console.infra.redis.OnlineShopRedis;
import org.o2.metadata.console.infra.repository.OnlineShopRepository;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * 网店
 *
 * @author yipeng.zhu@hand-china.com 2021-08-06
 **/
@Component
public class OnlineShopRedisImpl implements OnlineShopRedis {
    private OnlineShopRepository onlineShopRepository;
    private RedisCacheClient redisCacheClient;
    public OnlineShopRedisImpl(OnlineShopRepository onlineShopRepository,
                               RedisCacheClient redisCacheClient) {
        this.onlineShopRepository = onlineShopRepository;
        this.redisCacheClient = redisCacheClient;
    }

    @Override
    public void updateRedis(String onlineShopCode, Long tenantId) {
        OnlineShopQueryInnerDTO query = new OnlineShopQueryInnerDTO();
        query.setOnlineShopCodes(Collections.singletonList(onlineShopCode));

        List<OnlineShop> list = onlineShopRepository.listOnlineShops(query,tenantId);
        if (list.isEmpty()) {
            return;
        }
        OnlineShop onlineShop = list.get(0);
        String key = OnlineShopConstants.Redis.getOnlineShopKey(tenantId);
        if (onlineShop.getActiveFlag().equals(BaseConstants.Flag.NO)) {
            redisCacheClient.opsForHash().delete(key,onlineShopCode);
            return;
        }
        redisCacheClient.opsForHash().put(key,onlineShopCode,FastJsonHelper.objectToString(OnlineShopConverter.poToBoObject(onlineShop)));
    }

    @Override
    public void batchUpdateShopRelWh(List<OnlineShopRelWarehouse> list, Long tenantId, String handleType) {
        if (list.isEmpty() || StringUtils.isEmpty(handleType)) {
            return;
        }
        Map<String, List<OnlineShopRelWarehouse>>  map = list.stream().collect(Collectors.groupingBy(OnlineShopRelWarehouse::getOnlineShopCode));
        Map<String,Map<String,String>> groupMap = Maps.newHashMapWithExpectedSize(map.size());
        for (Map.Entry<String, List<OnlineShopRelWarehouse>> entry : map.entrySet()) {
            String redisKey = OnlineShopConstants.Redis.getOnlineShopRelWarehouseKey( entry.getKey(),tenantId);
            List<OnlineShopRelWarehouse> v = entry.getValue();
            Map<String,String> hashMap = Maps.newHashMapWithExpectedSize(v.size());
            for (OnlineShopRelWarehouse warehouse : v) {
                hashMap.put(warehouse.getWarehouseCode(),String.valueOf(warehouse.getActiveFlag()));
            }
            groupMap.put(redisKey,hashMap);
        }
        final DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript<>();
        if (OnlineShopConstants.Redis.UPDATE.equals(handleType)) {
            defaultRedisScript.setScriptSource(OnlineShopConstants.Redis.UPDATE_CACHE_LUA);
        } else {
            defaultRedisScript.setScriptSource(OnlineShopConstants.Redis.DELETE_CACHE_LUA);
        }
        this.redisCacheClient.execute(defaultRedisScript, new ArrayList<>(), FastJsonHelper.objectToString(groupMap));

    }

    @Override
    public void batchUpdateRedis(List<OnlineShop> list, Long tenantId) {
        if (list.isEmpty()) {
            return;
        }
        List<OnlineShopCacheBO> bos = OnlineShopConverter.poToBoListObjects(list);
        Map<String, String> map = Maps.newHashMapWithExpectedSize(bos.size());
        for (OnlineShopCacheBO bo : bos) {
            map.put(bo.getOnlineShopCode(), FastJsonHelper.objectToString(bo));
        }
        String key = OnlineShopConstants.Redis.getOnlineShopKey(tenantId);
        redisCacheClient.opsForHash().putAll(key, map);
    }
}
