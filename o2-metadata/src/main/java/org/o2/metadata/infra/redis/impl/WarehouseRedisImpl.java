package org.o2.metadata.infra.redis.impl;

import org.o2.core.helper.FastJsonHelper;
import org.o2.data.redis.client.RedisCacheClient;

import org.o2.metadata.infra.constants.WarehouseConstants;
import org.o2.metadata.infra.entity.Warehouse;
import org.o2.metadata.infra.redis.WarehouseRedis;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
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
            Warehouse warehouse =  FastJsonHelper.stringToObject(jsonStr,Warehouse.class);
            warehouse.setWarehouseCode(code);
            list.add(warehouse);
        }
        return list;
    }

    @Override
    public void updateExpressValue(String warehouseCode, String increment, Long tenantId) {
        executeScript(WarehouseConstants.WarehouseCache.EXPRESS_LIMIT_COLLECTION,warehouseCode, increment, tenantId, EXPRESS_VALUE_CACHE_LUA);
    }


    private void executeScript(final String limit,final String warehouseCode, final String num, final Long tenantId, final ScriptSource scriptSource) {
        final DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setScriptSource(scriptSource);
        this.redisCacheClient.execute(defaultRedisScript, Collections.singletonList(warehouseLimitCacheKey(limit, tenantId)), warehouseCode, num, String.valueOf(tenantId), warehouseCacheKey(warehouseCode, tenantId));
    }

    private String warehouseLimitCacheKey(String limit,Long tenantId) {
        if (tenantId == null) {
            return WarehouseConstants.WarehouseCache.warehouseLimitCacheKey(limit,0);
        }
        return WarehouseConstants.WarehouseCache.warehouseLimitCacheKey(limit,tenantId);
    }

    private static final ResourceScriptSource EXPRESS_VALUE_CACHE_LUA =
            new ResourceScriptSource(new ClassPathResource("script/lua/warehouse/express_value_cache.lua"));


    public String warehouseCacheKey(String warehouseCode, Long tenantId) {
        if (tenantId == null) {
            return WarehouseConstants.WarehouseCache.warehouseCacheKey(0, warehouseCode);
        }
        return WarehouseConstants.WarehouseCache.warehouseCacheKey(tenantId, warehouseCode);
    }
}
