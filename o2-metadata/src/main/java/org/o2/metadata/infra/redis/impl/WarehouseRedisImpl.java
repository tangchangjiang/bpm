package org.o2.metadata.infra.redis.impl;


import org.apache.commons.collections4.MapUtils;
import org.o2.core.helper.JsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.infra.constants.PosConstants;
import org.o2.metadata.infra.constants.WarehouseConstants;
import org.o2.metadata.infra.entity.Pos;
import org.o2.metadata.infra.entity.Warehouse;
import org.o2.metadata.infra.entity.WarehouseLimit;
import org.o2.metadata.infra.redis.WarehouseRedis;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * 仓库
 *
 * @author yipeng.zhu@hand-china.com 2021-07-13
 **/
@Component
public class WarehouseRedisImpl implements WarehouseRedis {
    private final RedisCacheClient redisCacheClient;

    public WarehouseRedisImpl(RedisCacheClient redisCacheClient) {
        this.redisCacheClient = redisCacheClient;
    }

    @Override
    public List<Warehouse> listWarehouses(List<String> warehouseCodes, Long tenantId) {
        List<Warehouse> list = new ArrayList<>(warehouseCodes.size());
        String key = WarehouseConstants.WarehouseCache.warehouseCacheKey(tenantId);
        List<String> warehouseStr = redisCacheClient.<String,String>opsForHash().multiGet(key,warehouseCodes);
        for (String jsonStr : warehouseStr) {
            Warehouse warehouse =  JsonHelper.stringToObject(jsonStr,Warehouse.class);
            list.add(warehouse);
        }
        return list;
    }

    @Override
    public Map<String, WarehouseLimit> listWarehouseLimit(List<String> warehouseCodes, Long tenantId) {
        Map<String, WarehouseLimit> limitMap = new HashMap<>();
        String limitKey = WarehouseConstants.WarehouseCache.warehouseLimitCacheKey(WarehouseConstants.WarehouseCache.PICK_UP_LIMIT_COLLECTION, tenantId);
        Map<String, String> map = redisCacheClient.<String, String>opsForHash().entries(limitKey);
        if (MapUtils.isEmpty(map)) {
            return limitMap;
        }
        Map<String, String> warehouseMap = map.entrySet().stream().filter(m -> warehouseCodes.contains(m.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        if (MapUtils.isEmpty(warehouseMap)) {
            return limitMap;
        }
        for(Map.Entry<String, String> entry : warehouseMap.entrySet()) {
            WarehouseLimit warehouseLimit = JsonHelper.stringToObject(entry.getValue(), WarehouseLimit.class);
            limitMap.put(entry.getKey(), warehouseLimit);
        }
        return limitMap;
    }

    @Override
    public List<Pos> listWarehousesByPosCode(List<String> posCodes, Long tenantId) {
        List<Pos> list = new ArrayList<>(posCodes.size());
        String key = PosConstants.RedisKey.getPosDetailKey(tenantId);
        List<String> posStr = redisCacheClient.<String,String>opsForHash().multiGet(key,posCodes);
        if (posStr.isEmpty()) {
            return list;
        }
        for (String jsonStr : posStr) {
            Pos pos =  JsonHelper.stringToObject(jsonStr,Pos.class);
            list.add(pos);
        }
        return list;
    }

}
