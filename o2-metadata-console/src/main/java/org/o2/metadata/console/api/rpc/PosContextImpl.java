package org.o2.metadata.console.api.rpc;

import com.google.common.base.Joiner;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.Service;
import org.o2.context.metadata.MetadataContext;
import org.o2.context.metadata.api.IPosContext;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.core.infra.constants.MetadataConstants;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.o2.metadata.core.infra.constants.MetadataConstants.PosCacheCode.CACHE_SERVICE_NAME_POS;
import static org.o2.metadata.core.infra.constants.MetadataConstants.SysParameterCache.CACHE_SERVICE_NAME;

/**
 * Pos RPC Provider
 *
 * @author mark.bao@hand-china.com 2019/11/29
 */
@Service(version = MetadataContext.PosContext.Version.DEF)
@Component("posContext")
public class PosContextImpl implements IPosContext {
    private final RedisCacheClient redisCacheClient;

    public PosContextImpl(final RedisCacheClient redisCacheClient) {
        this.redisCacheClient = redisCacheClient;
    }

    @Override
    public void saveExpressQuantity(final String posCode, final String expressQuantity, Long tenantId) {
        executeScript(posCode, expressQuantity, tenantId, EXPRESS_LIMIT_CACHE_LUA);
    }

    @Override
    public void savePickUpQuantity(final String posCode, final String pickUpQuantity, Long tenantId) {
        executeScript(posCode, pickUpQuantity, tenantId, PICK_UP_LIMIT_CACHE_LUA);
    }

    @Override
    public void updateExpressValue(final String posCode, final String increment, Long tenantId) {
        executeScript(posCode, increment, tenantId, EXPRESS_VALUE_CACHE_LUA);
    }

    @Override
    public void updatePickUpValue(final String posCode, final String increment, Long tenantId) {
        executeScript(posCode, increment, tenantId, PICK_UP_VALUE_CACHE_LUA);
    }

    @Override
    public String getExpressLimit(final String posCode, Long tenantId) {
        return this.redisCacheClient.<String, String>boundHashOps(posCacheKey(posCode, tenantId)).get(MetadataConstants.PosCache.EXPRESS_LIMIT_QUANTITY);
    }

    @Override
    public String getPickUpLimit(final String posCode, Long tenantId) {
        return this.redisCacheClient.<String, String>boundHashOps(posCacheKey(posCode, tenantId)).get(MetadataConstants.PosCache.PICK_UP_LIMIT_QUANTITY);
    }

    @Override
    public String getExpressValue(final String posCode, Long tenantId) {
        return this.redisCacheClient.<String, String>boundHashOps(posCacheKey(posCode, tenantId)).get(MetadataConstants.PosCache.EXPRESS_LIMIT_VALUE);
    }

    @Override
    public String getPickUpValue(final String posCode, Long tenantId) {
        return this.redisCacheClient.<String, String>boundHashOps(posCacheKey(posCode, tenantId)).get(MetadataConstants.PosCache.PICK_UP_LIMIT_VALUE);
    }

    @Override
    public String posCacheKey(final String posCode, Long tenantId) {
        String tenantStr = null == tenantId ? null : tenantId.toString();
        return Joiner.on(":").skipNulls().join(CACHE_SERVICE_NAME, CACHE_SERVICE_NAME_POS,tenantStr,posCode);
    }

    @Override
    public boolean isPosExpressLimit(final String posCode, Long tenantId) {
        final Boolean result = this.redisCacheClient.boundSetOps(MetadataConstants.PosCache.EXPRESS_LIMIT_COLLECTION).isMember(getCacheKey(posCode,tenantId));
        return result != null && result;
    }

    @Override
    public boolean isPosPickUpLimit(final String posCode, Long tenantId) {
        final Boolean result = this.redisCacheClient.boundSetOps(MetadataConstants.PosCache.PICK_UP_LIMIT_COLLECTION).isMember(getCacheKey(posCode,tenantId));
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
    public void resetPosExpressLimit(final String posCode, Long tenantId) {
        executeScript(posCode, tenantId, EXPRESS_VALUE_CACHE_RESET_LUA);
    }

    @Override
    public void resetPosPickUpLimit(final String posCode, Long tenantId) {
        executeScript(posCode, tenantId, PICK_UP_VALUE_CACHE_RESET_LUA);
    }

    private void executeScript(final String posCode, final String num, final Long tenantId, final ScriptSource scriptSource) {
        final DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setScriptSource(scriptSource);
        this.redisCacheClient.execute(defaultRedisScript, new ArrayList<>(), posCode, num, String.valueOf(tenantId));
    }

    private void executeScript(final String posCode, final Long tenantId, final ScriptSource scriptSource) {
        final DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setScriptSource(scriptSource);
        this.redisCacheClient.execute(defaultRedisScript, new ArrayList<>(), posCode, String.valueOf(tenantId));
    }

    private String getCacheKey(final String posCode,final Long tenantId){
        String tenantStr = null == tenantId ? null : tenantId.toString();
        return Joiner.on(":").skipNulls().join(tenantStr,posCode);
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
