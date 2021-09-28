package org.o2.metadata.console.infra.redis.impl;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.o2.core.helper.JsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.console.app.bo.WarehouseCacheBO;
import org.o2.metadata.console.infra.constant.WarehouseConstants;
import org.o2.metadata.console.infra.entity.Warehouse;
import org.o2.metadata.console.infra.redis.WarehouseRedis;
import org.o2.metadata.console.infra.repository.WarehouseRepository;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.ScriptSource;
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
            Warehouse warehouse  = JsonHelper.stringToObject(str,Warehouse.class);
            list.add(warehouse);
        }
        return list;
    }

    @Override
    public void batchUpdateWarehouse(List<String> warehouseCodes, Long tenantId) {
        List<WarehouseCacheBO> list = warehouseRepository.listWarehouseByCode(warehouseCodes, tenantId);
        if (null == list || list.isEmpty()) {
            return;
        }
        Map<String, String> updateMap = Maps.newHashMapWithExpectedSize(list.size());
        for (WarehouseCacheBO bo : list) {
            updateMap.put(bo.getWarehouseCode(), JsonHelper.objectToString(bo));
        }
        List<String> keyList = new ArrayList<>(3);
        // 库存缓存key
        String warehouseCacheKey = WarehouseConstants.WarehouseCache.warehouseCacheKey(tenantId);
        keyList.add(warehouseCacheKey);
        // 仓库快递配送接单量限制 key
        String expressLimitKey = WarehouseConstants.WarehouseCache.getLimitCacheKey(WarehouseConstants.WarehouseCache.EXPRESS_LIMIT_KEY,tenantId);
        keyList.add(expressLimitKey);
        // 仓库自提单量限制 key
        String pickUpLimitKey = WarehouseConstants.WarehouseCache.getLimitCacheKey(WarehouseConstants.WarehouseCache.PICK_UP_LIMIT_KEY,tenantId);
        keyList.add(pickUpLimitKey);
        final DefaultRedisScript<Long> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setScriptSource(WarehouseConstants.WarehouseCache.UPDATE_WAREHOUSE_CACHE_LUA);
        this.redisCacheClient.execute(defaultRedisScript, keyList, JsonHelper.mapToString(updateMap));
    }

    @Override
    public Long updateExpressQuantity(String warehouseCode, String expressQuantity, Long tenantId) {
        // 仓库快递配送接单量限制 key
        String expressLimitKey = WarehouseConstants.WarehouseCache.getLimitCacheKey(WarehouseConstants.WarehouseCache.EXPRESS_LIMIT_KEY,tenantId);
        // 库存缓存key
        String warehouseCacheKey = WarehouseConstants.WarehouseCache.warehouseCacheKey(tenantId);
        List<String> keyList = new ArrayList<>(2);
        keyList.add(expressLimitKey);
        keyList.add(warehouseCacheKey);
        return executeScript(keyList,warehouseCode, expressQuantity,WarehouseConstants.WarehouseCache.EXPRESS_LIMIT_CACHE_LUA);
    }

    @Override
    public Long updatePickUpValue(String warehouseCode, String pickUpQuantity, Long tenantId) {
        // 仓库自提单量限制 key
        String pickUpLimitKey = WarehouseConstants.WarehouseCache.getLimitCacheKey(WarehouseConstants.WarehouseCache.PICK_UP_LIMIT_KEY,tenantId);
        // 库存缓存key
        String warehouseCacheKey = WarehouseConstants.WarehouseCache.warehouseCacheKey(tenantId);
        List<String> keyList = new ArrayList<>(2);
        keyList.add(pickUpLimitKey);
        keyList.add(warehouseCacheKey);
        return executeScript(keyList,warehouseCode, pickUpQuantity,WarehouseConstants.WarehouseCache.PICK_UP_LIMIT_CACHE_LUA);
    }


    /**
     * 执行lua 脚本
     * @param keyList redis key 集合
     * @param warehouseCode 仓库编码
     * @param num 数量
     * @param scriptSource lua
     * @return integer
     */
    private Long executeScript(List<String> keyList, final String warehouseCode, final String num, ScriptSource scriptSource) {
        final DefaultRedisScript<Long> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setScriptSource(scriptSource);
        defaultRedisScript.setResultType(Long.class);
        try {
            return this.redisCacheClient.execute(defaultRedisScript, keyList, warehouseCode, num);
        } catch (Exception e) {
            log.error("execute script error", e);
            return -1L;
        }
    }
}
