package org.o2.metadata.console.infra.util;

import lombok.extern.slf4j.Slf4j;
import org.o2.core.helper.JsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.List;
import java.util.Map;

/**
 * redis util
 *
 * @author yuying.shi@hand-china.com 2020/3/25
 */
@Slf4j
public class SystemParameterRedisUtil {

    private SystemParameterRedisUtil() { }

    /**
     * redis execute
     *
     * @param filedMaps            filedMaps
     * @param keyList              keyList
     * @param resourceScriptSource resourceScriptSource
     * @param redisCacheClient     redisCacheClient
     */
    public static <K, V> void executeScript(final Map<K, V> filedMaps,
                                            final List<String> keyList,
                                            final ResourceScriptSource resourceScriptSource,
                                            final RedisCacheClient redisCacheClient) {
        final DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setScriptSource(resourceScriptSource);
        redisCacheClient.execute(defaultRedisScript, keyList, JsonHelper.mapToString(filedMaps));
    }

}
