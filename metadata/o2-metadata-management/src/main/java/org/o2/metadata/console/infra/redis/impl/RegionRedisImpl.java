package org.o2.metadata.console.infra.redis.impl;

import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.console.infra.constant.CountryConstants;
import org.o2.metadata.console.infra.redis.RegionRedis;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class RegionRedisImpl implements RegionRedis {
    private final RedisCacheClient redisCacheClient;

    public RegionRedisImpl(RedisCacheClient redisCacheClient) {
        this.redisCacheClient = redisCacheClient;
    }

    private static String trimDomainPrefix(String resourceUrl) {
        if (StringUtils.isBlank(resourceUrl)) {
            return "";
        }

        String[] httpSplits = resourceUrl.split(BaseConstants.Symbol.DOUBLE_SLASH);
        if (httpSplits.length < 2) {
            return resourceUrl;
        }

        String domainSuffix = httpSplits[1];
        return domainSuffix.substring(domainSuffix.indexOf(BaseConstants.Symbol.SLASH));
    }

    @Override
    public void saveRegionUrlToRedis(Long tenantId, String countryCode, Map<String, String> resourceUrlMap) {
        if (MapUtils.isEmpty(resourceUrlMap)) {
            return;
        }

        // 获取Redis key
        String resourceUrlKey = CountryConstants.Redis.getRegionResourceUrlKey(tenantId, countryCode);

        // 截取domain
        resourceUrlMap.forEach((lang, resourceUrl) -> {
            if (StringUtils.isNotBlank(resourceUrl)) {
                resourceUrlMap.put(lang, trimDomainPrefix(resourceUrl));
            }
        });

        // 保存Redis
        redisCacheClient.opsForHash().putAll(resourceUrlKey, resourceUrlMap);
    }

    @Override
    public Map<String, String> queryRegionUrlFromRedis(Long tenantId, String lang, List<String> countryCodeList) {
        if (CollectionUtils.isEmpty(countryCodeList)) {
            return Collections.emptyMap();
        }

        // 获取Redis key
        List<Object> regionResourceUrlList = redisCacheClient.executePipelined((RedisCallback<?>) con -> {
            countryCodeList.forEach(countryCode -> {
                String resourceUrlKey = CountryConstants.Redis.getRegionResourceUrlKey(tenantId, countryCode);
                con.hGet(resourceUrlKey.getBytes(StandardCharsets.UTF_8), lang.getBytes(StandardCharsets.UTF_8));
            });
            return null;
        });

        if (CollectionUtils.isEmpty(regionResourceUrlList)) {
            return Collections.emptyMap();
        }

        // 组装map
        Map<String, String> resourceUrlMap = Maps.newHashMapWithExpectedSize(countryCodeList.size());
        for (int i = 0; i < countryCodeList.size(); i++) {
            if (Objects.isNull(regionResourceUrlList.get(i))) {
                continue;
            }
            String regionResourceUrl = String.valueOf(regionResourceUrlList.get(i));
            resourceUrlMap.put(countryCodeList.get(i), regionResourceUrl);
        }

        return resourceUrlMap;
    }
}
