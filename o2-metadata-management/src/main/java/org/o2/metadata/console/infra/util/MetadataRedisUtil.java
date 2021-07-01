package org.o2.metadata.console.infra.util;

import org.apache.commons.collections4.CollectionUtils;
import org.hzero.core.message.MessageAccessor;
import org.o2.core.helper.FastJsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.choerodon.core.exception.CommonException;
import lombok.extern.slf4j.Slf4j;

/**
 * redis util
 *
 * @author yuying.shi@hand-china.com 2020/3/25
 */
@Slf4j
public class MetadataRedisUtil {

    private static final String METHOD_GROUP_MAP = "groupMap";
    private static final String METHOD_BUILD_REDIS_HASH_KEY = "buildRedisHashKey";
    private static final String METHOD_BUILD_REDIS_HASH_MAP = "buildRedisHashMap";

    /**
     * synToRedis
     *
     * @param ts                   list
     * @param resourceScriptSource resourceScriptSource
     * @param redisCacheClient     redisCacheClient
     */
    public static <T> void synToRedis(final List<T> ts,
                                      final ResourceScriptSource resourceScriptSource,
                                      final RedisCacheClient redisCacheClient) {
        if (CollectionUtils.isEmpty(ts)) {
            return;
        }
        try {
            Class<?> entityClass = ts.get(0).getClass();
            Method method = entityClass.getMethod(METHOD_GROUP_MAP, List.class);
            // 分组  0失效delete，1有效save
            Map<Integer, List<T>> map = (Map<Integer, List<T>>) method.invoke(null, ts);
            Map<String, Map<String, Object>> filedMaps = new HashMap<>(8);
            for (Map.Entry<Integer, List<T>> tEntry : map.entrySet()) {
                // 新增/更新
                if (tEntry.getKey() == 1) {
                    for (T t : tEntry.getValue()) {
                        Method buildRedisHashKeyMethod = entityClass.getMethod(METHOD_BUILD_REDIS_HASH_KEY);
                        Method buildRedisHashMapMethod = entityClass.getMethod(METHOD_BUILD_REDIS_HASH_MAP);
                        // 获取hashKey
                        final String hashKey = String.valueOf(buildRedisHashKeyMethod.invoke(t));
                        // 获取hashMap
                        filedMaps.put(hashKey, (Map<String, Object>) buildRedisHashMapMethod.invoke(t));
                    }
                }
                // 删除
                else {
                    for (T t : tEntry.getValue()) {
                        Method buildRedisHashKeyMethod = entityClass.getMethod(METHOD_BUILD_REDIS_HASH_KEY);
                        // 获取hashKey
                        final String hashKey = String.valueOf(buildRedisHashKeyMethod.invoke(t));
                        // 获取hashMap
                        filedMaps.put(hashKey, null);
                    }
                }
            }
            executeScript(filedMaps, Collections.emptyList(), resourceScriptSource, redisCacheClient);
        } catch (Exception e) {
            throw new CommonException(MetadataConstants.Message.SYSTEM_PARAMETER_SUCCESS_NUM);
        }
    }

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
        redisCacheClient.execute(defaultRedisScript, keyList, FastJsonHelper.mapToString(filedMaps));
    }

}
