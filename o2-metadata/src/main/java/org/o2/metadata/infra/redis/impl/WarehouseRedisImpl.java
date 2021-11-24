package org.o2.metadata.infra.redis.impl;

import org.o2.core.helper.JsonHelper;
import org.o2.data.redis.client.RedisCacheClient;

import org.o2.metadata.infra.constants.WarehouseConstants;
import org.o2.metadata.infra.entity.Warehouse;
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

}
