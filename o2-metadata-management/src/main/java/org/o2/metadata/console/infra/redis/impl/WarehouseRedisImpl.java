package org.o2.metadata.console.infra.redis.impl;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseConstants;
import org.o2.core.helper.FastJsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.console.app.bo.WarehouseCacheBO;
import org.o2.metadata.console.infra.constant.WarehouseConstants;
import org.o2.metadata.console.infra.entity.Warehouse;
import org.o2.metadata.console.infra.redis.WarehouseRedis;
import org.o2.metadata.console.infra.repository.WarehouseRepository;
import org.springframework.stereotype.Component;

import java.awt.image.Kernel;
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
@Slf4j
public class WarehouseRedisImpl implements WarehouseRedis {
    private final RedisCacheClient redisCacheClient;
    private final WarehouseRepository warehouseRepository;

    public WarehouseRedisImpl(RedisCacheClient redisCacheClient, WarehouseRepository warehouseRepository) {
        this.redisCacheClient = redisCacheClient;
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public List<Warehouse> listWarehouses(List<String> warehouseCodes, Long tenantId) {
        String key = WarehouseConstants.WarehouseCache.warehouseCacheKey(tenantId);
        List<String> warehouseStr = new ArrayList<>();
        if (null == warehouseCodes) {
          Map<String,String> map  = redisCacheClient.<String,String>opsForHash().entries(key);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String v = entry.getValue();
                warehouseStr.add(v);
            }
        } else {
            warehouseStr = redisCacheClient.<String,String>opsForHash().multiGet(key,warehouseCodes);
        }
        List<Warehouse> list = new ArrayList<>();
        for (String str : warehouseStr) {
            if (null == str) {
                continue;
            }
            Warehouse warehouse  = FastJsonHelper.stringToObject(str,Warehouse.class);
            list.add(warehouse);
        }
        return list;
    }

    @Override
    public void batchUpdateWarehouse(List<String> warehouseCodes, Long tenantId) {
        List<WarehouseCacheBO> list =  warehouseRepository.listWarehouseByCode(warehouseCodes,tenantId);
        if (null == list ||list.isEmpty()) {
            return;
        }
        List<WarehouseCacheBO> update = new ArrayList<>();
        List<String> delete = new ArrayList<>();
        for (WarehouseCacheBO bo : list) {
            if (BaseConstants.Flag.YES.equals(bo.getActiveFlag())) {
                update.add(bo);
                continue;
            }
            delete.add(bo.getWarehouseCode());
        }
        String key = WarehouseConstants.WarehouseCache.warehouseCacheKey(tenantId);
        if (!update.isEmpty()) {
            Map<String,String> updateMap = Maps.newHashMapWithExpectedSize(update.size());
            for (WarehouseCacheBO bo : update) {
                updateMap.put(bo.getWarehouseCode(),FastJsonHelper.objectToString(bo));
            }
            redisCacheClient.opsForHash().putAll(key,updateMap);
        }
        if (!delete.isEmpty()) {
            redisCacheClient.opsForHash().delete(key,delete);
        }

    }
}
