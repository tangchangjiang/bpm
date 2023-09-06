package org.o2.metadata.console.infra.util;

import lombok.extern.slf4j.Slf4j;
import org.o2.core.helper.JsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.data.redis.helper.ScriptHelper;
import org.springframework.data.redis.core.script.RedisScript;

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
     * @param filedMaps        filedMaps
     * @param keyList          keyList
     * @param luaPath          luaPath
     * @param redisCacheClient redisCacheClient
     */
    public static <K, V> void executeScript(final Map<K, V> filedMaps,
                                            final List<String> keyList,
                                            final String luaPath,
                                            final RedisCacheClient redisCacheClient) {
        final RedisScript<Boolean> defaultRedisScript = ScriptHelper.of(luaPath, Boolean.class);
        redisCacheClient.execute(defaultRedisScript, keyList, JsonHelper.mapToString(filedMaps));
    }

}
