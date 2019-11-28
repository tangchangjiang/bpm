package org.o2.metadata.app.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.app.service.PosCacheService;
import org.o2.metadata.infra.constants.MetadataConstants;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * 门店缓存服务默认实现
 *
 * @author mark.bao@hand-china.com 2019-04-16
 */
public class PosCacheServiceImpl implements PosCacheService {
    private final RedisCacheClient redisCacheClient;

    public PosCacheServiceImpl(final RedisCacheClient redisCacheClient) {
        this.redisCacheClient = redisCacheClient;
    }

    @Override
    public void saveExpressQuantity(final String posCode, final String expressQuantity) {
        executeScript(posCode, expressQuantity, EXPRESS_LIMIT_CACHE_LUA);
    }

    @Override
    public void savePickUpQuantity(final String posCode, final String pickUpQuantity) {
        executeScript(posCode, pickUpQuantity, PICK_UP_LIMIT_CACHE_LUA);
    }

    @Override
    public void updateExpressValue(final String posCode, final String increment) {
        executeScript(posCode, increment, EXPRESS_VALUE_CACHE_LUA);
    }

    @Override
    public void updatePickUpValue(final String posCode, final String increment) {
        executeScript(posCode, increment, PICK_UP_VALUE_CACHE_LUA);
    }

    @Override
    public String getExpressLimit(final String posCode) {
        return this.redisCacheClient.<String, String>boundHashOps(posCacheKey(posCode)).get(MetadataConstants.PosCache.EXPRESS_LIMIT_QUANTITY);
    }

    @Override
    public String getPickUpLimit(final String posCode) {
        return this.redisCacheClient.<String, String>boundHashOps(posCacheKey(posCode)).get(MetadataConstants.PosCache.PICK_UP_LIMIT_QUANTITY);
    }

    @Override
    public String getExpressValue(final String posCode) {
        return this.redisCacheClient.<String, String>boundHashOps(posCacheKey(posCode)).get(MetadataConstants.PosCache.EXPRESS_LIMIT_VALUE);
    }

    @Override
    public String getPickUpValue(final String posCode) {
        return this.redisCacheClient.<String, String>boundHashOps(posCacheKey(posCode)).get(MetadataConstants.PosCache.PICK_UP_LIMIT_VALUE);
    }

    @Override
    public String posCacheKey(final String posCode) {
        return String.format(MetadataConstants.PosCache.POS_INFO_KEY, posCode);
    }

    @Override
    public boolean isPosExpressLimit(final String posCode) {
        final Boolean result = this.redisCacheClient.boundSetOps(MetadataConstants.PosCache.EXPRESS_LIMIT_COLLECTION).isMember(posCode);
        return result != null && result;
    }

    @Override
    public boolean isPosPickUpLimit(final String posCode) {
        final Boolean result = this.redisCacheClient.boundSetOps(MetadataConstants.PosCache.PICK_UP_LIMIT_COLLECTION).isMember(posCode);
        return result != null && result;
    }

    @Override
    public Set<String> expressLimitPosCollection() {
        final Set<String> members = this.redisCacheClient.boundSetOps(MetadataConstants.PosCache.EXPRESS_LIMIT_COLLECTION).members();
        if (CollectionUtils.isNotEmpty(members)) {
            return new HashSet<>(members);
        } else {
            return null;
        }
    }

    @Override
    public Set<String> pickUpLimitPosCollection() {
        final Set<String> members = this.redisCacheClient.boundSetOps(MetadataConstants.PosCache.PICK_UP_LIMIT_COLLECTION).members();
        if (CollectionUtils.isNotEmpty(members)) {
            return new HashSet<>(members);
        } else {
            return null;
        }
    }

    @Override
    public void resetPosExpressLimit(final String posCode) {
        executeScript(posCode, EXPRESS_VALUE_CACHE_RESET_LUA);
    }

    @Override
    public void resetPosPickUpLimit(final String posCode) {
        executeScript(posCode, PICK_UP_VALUE_CACHE_RESET_LUA);
    }

    private void executeScript(final String posCode, final String num, final ScriptSource scriptSource) {
        final DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setScriptSource(scriptSource);
        this.redisCacheClient.execute(defaultRedisScript, new ArrayList<>(), posCode, num);
    }

    private void executeScript(final String posCode, final ScriptSource scriptSource) {
        final DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setScriptSource(scriptSource);
        this.redisCacheClient.execute(defaultRedisScript, new ArrayList<>(), posCode);
    }

    private static final ResourceScriptSource EXPRESS_LIMIT_CACHE_LUA =
            new ResourceScriptSource(new ClassPathResource("script/lua/pos/express_limit_cache.lua"));
    private static final ResourceScriptSource EXPRESS_VALUE_CACHE_LUA =
            new ResourceScriptSource(new ClassPathResource("script/lua/pos/express_value_cache.lua"));
    private static final ResourceScriptSource EXPRESS_VALUE_CACHE_RESET_LUA =
            new ResourceScriptSource(new ClassPathResource("script/lua/pos/express_value_cache_reset.lua"));
    private static final ResourceScriptSource PICK_UP_LIMIT_CACHE_LUA =
            new ResourceScriptSource(new ClassPathResource("script/lua/pos/pick_up_limit_cache.lua"));
    private static final ResourceScriptSource PICK_UP_VALUE_CACHE_LUA =
            new ResourceScriptSource(new ClassPathResource("script/lua/pos/pick_up_value_cache.lua"));
    private static final ResourceScriptSource PICK_UP_VALUE_CACHE_RESET_LUA =
            new ResourceScriptSource(new ClassPathResource("script/lua/pos/pick_up_value_cache_reset.lua"));
}
