package org.o2.metadata.infra.redis.impl;

import org.o2.core.helper.FastJsonHelper;
import org.o2.data.redis.client.RedisCacheClient;

import org.o2.metadata.infra.constants.WarehouseConstants;
import org.o2.metadata.infra.entity.Warehouse;
import org.o2.metadata.infra.redis.WarehouseRedis;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        for (String code : warehouseCodes) {
            String key = WarehouseConstants.WarehouseCache.warehouseCacheKey(tenantId, code);
            Map<Object,Object> wareHouseMap = redisCacheClient.opsForHash().entries(key);
            String jsonStr = FastJsonHelper.mapToString(wareHouseMap);
            list.add(FastJsonHelper.stringToObject(jsonStr,Warehouse.class));
        }
        return list;
    }
}
