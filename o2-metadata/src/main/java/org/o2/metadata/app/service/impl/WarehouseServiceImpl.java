package org.o2.metadata.app.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.o2.core.helper.FastJsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.app.service.WarehouseService;
import org.o2.metadata.infra.constants.MetadataConstants;
import org.o2.metadata.infra.constants.WarehouseConstants;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
@Service
public class WarehouseServiceImpl implements WarehouseService {

    private final RedisCacheClient redisCacheClient;

    public WarehouseServiceImpl(RedisCacheClient redisCacheClient) {
        this.redisCacheClient = redisCacheClient;
    }


    @Override
    public void saveWarehouse(String warehouseCode,Map<String, Object> hashMap,  Long tenantId) {
        executeScript(warehouseCode, hashMap, tenantId, SAVE_WAREHOUSE_LUA);
    }

    @Override
    public void updateWarehouse(String warehouseCode, Map<String, Object> hashMap, Long tenantId) {
        executeScript(warehouseCode, hashMap, tenantId, UPDATE_WAREHOUSE_LUA);

    }

    @Override
    public void saveExpressQuantity(String warehouseCode, String expressQuantity, Long tenantId) {
        executeScript(WarehouseConstants.WarehouseCache.EXPRESS_LIMIT_COLLECTION,warehouseCode, expressQuantity, tenantId, EXPRESS_LIMIT_CACHE_LUA);
    }

    @Override
    public void savePickUpQuantity(String warehouseCode, String pickUpQuantity, Long tenantId) {
        executeScript(WarehouseConstants.WarehouseCache.PICK_UP_LIMIT_COLLECTION,warehouseCode, pickUpQuantity, tenantId, PICK_UP_LIMIT_CACHE_LUA);
    }

    @Override
    public void updateExpressValue(String warehouseCode, String increment, Long tenantId) {
        executeScript(WarehouseConstants.WarehouseCache.EXPRESS_LIMIT_COLLECTION,warehouseCode, increment, tenantId, EXPRESS_VALUE_CACHE_LUA);
    }

    @Override
    public void updatePickUpValue(String warehouseCode, String increment, Long tenantId) {
        executeScript(WarehouseConstants.WarehouseCache.PICK_UP_LIMIT_COLLECTION,warehouseCode, increment, tenantId, PICK_UP_VALUE_CACHE_LUA);
    }

    @Override
    public String getExpressLimit(String warehouseCode, Long tenantId) {
        return this.redisCacheClient.<String, String>boundHashOps(warehouseCacheKey(warehouseCode, tenantId)).get(WarehouseConstants.WarehouseCache.EXPRESS_LIMIT_QUANTITY);
    }

    @Override
    public String getPickUpLimit(String warehouseCode, Long tenantId) {
        return this.redisCacheClient.<String, String>boundHashOps(warehouseCacheKey(warehouseCode, tenantId)).get(WarehouseConstants.WarehouseCache.PICK_UP_LIMIT_QUANTITY);
    }

    @Override
    public String getExpressValue(String warehouseCode, Long tenantId) {
        return this.redisCacheClient.<String, String>boundHashOps(warehouseCacheKey(warehouseCode, tenantId)).get(WarehouseConstants.WarehouseCache.EXPRESS_LIMIT_VALUE);
    }

    @Override
    public String getPickUpValue(String warehouseCode, Long tenantId) {
        return this.redisCacheClient.<String, String>boundHashOps(warehouseCacheKey(warehouseCode, tenantId)).get(WarehouseConstants.WarehouseCache.PICK_UP_LIMIT_VALUE);
    }

    @Override
    public String warehouseCacheKey(String warehouseCode, Long tenantId) {
        if (tenantId == null) {
            return WarehouseConstants.WarehouseCache.warehouseCacheKey(0, warehouseCode);
        }
        return WarehouseConstants.WarehouseCache.warehouseCacheKey(tenantId, warehouseCode);
    }

    @Override
    public String warehouseLimitCacheKey(String limit,Long tenantId) {
        if (tenantId == null) {
            return WarehouseConstants.WarehouseCache.warehouseLimitCacheKey(limit,0);
        }
        return WarehouseConstants.WarehouseCache.warehouseLimitCacheKey(limit,tenantId);
    }

    @Override
    public boolean isWarehouseExpressLimit(String warehouseCode, Long tenantId) {
        final Boolean result = this.redisCacheClient.boundSetOps(WarehouseConstants.WarehouseCache.EXPRESS_LIMIT_COLLECTION).isMember(warehouseCacheKey(warehouseCode, tenantId));
        return result != null && result;
    }

    @Override
    public boolean isWarehousePickUpLimit(String warehouseCode, Long tenantId) {
        final Boolean result = this.redisCacheClient.boundSetOps(WarehouseConstants.WarehouseCache.PICK_UP_LIMIT_COLLECTION).isMember(warehouseCacheKey(warehouseCode, tenantId));
        return result != null && result;
    }

    @Override
    public Set<String> expressLimitWarehouseCollection(Long tenantId) {
        final Set<String> members = this.redisCacheClient.boundSetOps(warehouseLimitCacheKey(WarehouseConstants.WarehouseCache.EXPRESS_LIMIT_COLLECTION,tenantId)).members();
        if (CollectionUtils.isNotEmpty(members)) {
            return new HashSet<>(members);
        } else {
            return null;
        }
    }

    @Override
    public Set<String> pickUpLimitWarehouseCollection(Long tenantId) {
        final Set<String> members = this.redisCacheClient.boundSetOps(warehouseLimitCacheKey(WarehouseConstants.WarehouseCache.PICK_UP_LIMIT_COLLECTION,tenantId)).members();
        if (CollectionUtils.isNotEmpty(members)) {
            return new HashSet<>(members);
        } else {
            return null;
        }
    }

    @Override
    public void resetWarehouseExpressLimit(String warehouseCode, Long tenantId) {
        executeScript(warehouseCode,WarehouseConstants.WarehouseCache.EXPRESS_LIMIT_COLLECTION,tenantId, EXPRESS_VALUE_CACHE_RESET_LUA);
    }


    @Override
    public void resetWarehousePickUpLimit(String warehouseCode, Long tenantId) {
        executeScript(warehouseCode, WarehouseConstants.WarehouseCache.PICK_UP_LIMIT_COLLECTION,tenantId, PICK_UP_VALUE_CACHE_RESET_LUA);

    }

    private void executeScript(final String limit,final String warehouseCode, final String num, final Long tenantId, final ScriptSource scriptSource) {
        final DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setScriptSource(scriptSource);
        this.redisCacheClient.execute(defaultRedisScript, Collections.singletonList(warehouseLimitCacheKey(limit, tenantId)), warehouseCode, num, String.valueOf(tenantId), warehouseCacheKey(warehouseCode, tenantId));
    }

    private void executeScript(final String warehouseCode,final String limit,final Long tenantId, final ScriptSource scriptSource) {
        final DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setScriptSource(scriptSource);
        this.redisCacheClient.execute(defaultRedisScript, Collections.singletonList(warehouseLimitCacheKey(limit, tenantId)), warehouseCode, String.valueOf(tenantId), warehouseCacheKey(warehouseCode, tenantId));
    }

    private void executeScript(final String warehouseCode, final Map<String,Object> hashMap, final Long tenantId, final ScriptSource scriptSource) {
        final DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setScriptSource(scriptSource);
        this.redisCacheClient.execute(defaultRedisScript, Collections.singletonList(warehouseCacheKey(warehouseCode, tenantId)), FastJsonHelper.objectToString(hashMap));
    }

    private static final ResourceScriptSource EXPRESS_LIMIT_CACHE_LUA =
            new ResourceScriptSource(new ClassPathResource("script/lua/warehouse/express_limit_cache.lua"));
    private static final ResourceScriptSource EXPRESS_VALUE_CACHE_LUA =
            new ResourceScriptSource(new ClassPathResource("script/lua/warehouse/express_value_cache.lua"));
    private static final ResourceScriptSource EXPRESS_VALUE_CACHE_RESET_LUA =
            new ResourceScriptSource(new ClassPathResource("script/lua/warehouse/express_value_cache_reset.lua"));
    private static final ResourceScriptSource PICK_UP_LIMIT_CACHE_LUA =
            new ResourceScriptSource(new ClassPathResource("script/lua/warehouse/pick_up_limit_cache.lua"));
    private static final ResourceScriptSource PICK_UP_VALUE_CACHE_LUA =
            new ResourceScriptSource(new ClassPathResource("script/lua/warehouse/pick_up_value_cache.lua"));
    private static final ResourceScriptSource PICK_UP_VALUE_CACHE_RESET_LUA =
            new ResourceScriptSource(new ClassPathResource("script/lua/warehouse/pick_up_value_cache_reset.lua"));
    private static final ResourceScriptSource SAVE_WAREHOUSE_LUA =
            new ResourceScriptSource(new ClassPathResource("script/lua/warehouse/save_warehouse_cache.lua"));
    private static final ResourceScriptSource UPDATE_WAREHOUSE_LUA =
            new ResourceScriptSource(new ClassPathResource("script/lua/warehouse/update_warehouse_cache.lua"));

}
