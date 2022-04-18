package org.o2.metadata.infra.redis.impl;

import org.apache.commons.collections.CollectionUtils;
import org.o2.core.helper.JsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.infra.constants.WarehouseConstants;
import org.o2.metadata.infra.entity.Warehouse;
import org.o2.metadata.infra.entity.WarehouseLimit;
import org.o2.metadata.infra.redis.WarehouseRedis;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
    public List<WarehouseLimit> listWarehouseLimit(List<String> warehouseCodes, Long tenantId) {
        List<WarehouseLimit> list = new ArrayList<>(warehouseCodes.size());
        String limitKey = WarehouseConstants.WarehouseCache.warehouseLimitCacheKey(WarehouseConstants.WarehouseCache.PICK_UP_LIMIT_COLLECTION, tenantId);
        List<String> warehouseLimitList = redisCacheClient.<String, String>opsForHash().multiGet(limitKey, warehouseCodes);
        if (CollectionUtils.isEmpty(warehouseLimitList)) {
            return null;
        }
        for (String jsonStr : warehouseLimitList) {
            WarehouseLimit warehouseLimit = JsonHelper.stringToObject(jsonStr, WarehouseLimit.class);
            list.add(warehouseLimit);
        }
        return list;
    }

}
