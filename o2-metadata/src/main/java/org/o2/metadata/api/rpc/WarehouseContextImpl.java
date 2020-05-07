package org.o2.metadata.api.rpc;

import org.apache.commons.collections4.CollectionUtils;
import org.o2.context.metadata.api.IWarehouseContext;
import org.o2.core.helper.FastJsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.core.infra.constants.MetadataConstants;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.*;

/**
 * Warehouse RPC Provider
 *
 * @author yuying.shi@hand-china.com 2020/3/13
 */
public class WarehouseContextImpl implements IWarehouseContext {

    private final RedisCacheClient redisCacheClient;

    public WarehouseContextImpl(RedisCacheClient redisCacheClient) {
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
        executeScript(MetadataConstants.WarehouseCache.EXPRESS_LIMIT_COLLECTION,warehouseCode, expressQuantity, tenantId, EXPRESS_LIMIT_CACHE_LUA);
    }

    @Override
    public void savePickUpQuantity(String warehouseCode, String pickUpQuantity, Long tenantId) {
        executeScript(MetadataConstants.WarehouseCache.PICK_UP_LIMIT_COLLECTION,warehouseCode, pickUpQuantity, tenantId, PICK_UP_LIMIT_CACHE_LUA);
    }

    @Override
    public void updateExpressValue(String warehouseCode, String increment, Long tenantId) {
        executeScript(MetadataConstants.WarehouseCache.EXPRESS_LIMIT_COLLECTION,warehouseCode, increment, tenantId, EXPRESS_VALUE_CACHE_LUA);
    }

    @Override
    public void updatePickUpValue(String warehouseCode, String increment, Long tenantId) {
        executeScript(MetadataConstants.WarehouseCache.PICK_UP_LIMIT_COLLECTION,warehouseCode, increment, tenantId, PICK_UP_VALUE_CACHE_LUA);
    }

    @Override
    public String getExpressLimit(String warehouseCode, Long tenantId) {
        return this.redisCacheClient.<String, String>boundHashOps(warehouseCacheKey(warehouseCode, tenantId)).get(MetadataConstants.WarehouseCache.EXPRESS_LIMIT_QUANTITY);
    }

    @Override
    public String getPickUpLimit(String warehouseCode, Long tenantId) {
        return this.redisCacheClient.<String, String>boundHashOps(warehouseCacheKey(warehouseCode, tenantId)).get(MetadataConstants.WarehouseCache.PICK_UP_LIMIT_QUANTITY);
    }

    @Override
    public String getExpressValue(String warehouseCode, Long tenantId) {
        return this.redisCacheClient.<String, String>boundHashOps(warehouseCacheKey(warehouseCode, tenantId)).get(MetadataConstants.WarehouseCache.EXPRESS_LIMIT_VALUE);
    }

    @Override
    public String getPickUpValue(String warehouseCode, Long tenantId) {
        return this.redisCacheClient.<String, String>boundHashOps(warehouseCacheKey(warehouseCode, tenantId)).get(MetadataConstants.WarehouseCache.PICK_UP_LIMIT_VALUE);
    }

    @Override
    public String warehouseCacheKey(String warehouseCode, Long tenantId) {
        if (tenantId == null) {
            return MetadataConstants.WarehouseCache.warehouseCacheKey(0, warehouseCode);
        }
        return MetadataConstants.WarehouseCache.warehouseCacheKey(tenantId, warehouseCode);
    }

    @Override
    public String warehouseLimitCacheKey(String limit,Long tenantId) {
        if (tenantId == null) {
            return MetadataConstants.WarehouseCache.warehouseLimitCacheKey(limit,0);
        }
        return MetadataConstants.WarehouseCache.warehouseLimitCacheKey(limit,tenantId);
    }

    @Override
    public boolean isWarehouseExpressLimit(String warehouseCode, Long tenantId) {
        final Boolean result = this.redisCacheClient.boundSetOps(MetadataConstants.WarehouseCache.EXPRESS_LIMIT_COLLECTION).isMember(warehouseCacheKey(warehouseCode, tenantId));
        return result != null && result;
    }

    @Override
    public boolean isWarehousePickUpLimit(String warehouseCode, Long tenantId) {
        final Boolean result = this.redisCacheClient.boundSetOps(MetadataConstants.WarehouseCache.PICK_UP_LIMIT_COLLECTION).isMember(warehouseCacheKey(warehouseCode, tenantId));
        return result != null && result;
    }

    @Override
    public Set<String> expressLimitWarehouseCollection(Long tenantId) {
        final Set<String> members = this.redisCacheClient.boundSetOps(warehouseLimitCacheKey(MetadataConstants.WarehouseCache.EXPRESS_LIMIT_COLLECTION,tenantId)).members();
        if (CollectionUtils.isNotEmpty(members)) {
            return new HashSet<>(members);
        } else {
            return null;
        }
    }

    @Override
    public Set<String> pickUpLimitWarehouseCollection(Long tenantId) {
        final Set<String> members = this.redisCacheClient.boundSetOps(warehouseLimitCacheKey(MetadataConstants.WarehouseCache.PICK_UP_LIMIT_COLLECTION,tenantId)).members();
        if (CollectionUtils.isNotEmpty(members)) {
            return new HashSet<>(members);
        } else {
            return null;
        }
    }

    @Override
    public void resetWarehouseExpressLimit(String warehouseCode, Long tenantId) {
        executeScript(warehouseCode,MetadataConstants.WarehouseCache.EXPRESS_LIMIT_COLLECTION,tenantId, EXPRESS_VALUE_CACHE_RESET_LUA);
    }


    @Override
    public void resetWarehousePickUpLimit(String warehouseCode, Long tenantId) {
        executeScript(warehouseCode,MetadataConstants.WarehouseCache.PICK_UP_LIMIT_COLLECTION,tenantId, PICK_UP_VALUE_CACHE_RESET_LUA);

    }

    private void executeScript(final String limit,final String warehouseCode, final String num, final Long tenantId, final ScriptSource scriptSource) {
        final DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setScriptSource(scriptSource);
        this.redisCacheClient.execute(defaultRedisScript, Collections.singletonList(warehouseLimitCacheKey(limit, tenantId)), warehouseCode, num, String.valueOf(tenantId));
    }

    private void executeScript(final String warehouseCode,final String limit,final Long tenantId, final ScriptSource scriptSource) {
        final DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setScriptSource(scriptSource);
        this.redisCacheClient.execute(defaultRedisScript, Collections.singletonList(warehouseLimitCacheKey(limit, tenantId)), warehouseCode, String.valueOf(tenantId));
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
