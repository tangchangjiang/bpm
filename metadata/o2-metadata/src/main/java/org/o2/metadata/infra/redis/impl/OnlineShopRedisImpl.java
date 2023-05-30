package org.o2.metadata.infra.redis.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.o2.cache.util.CacheHelper;
import org.o2.core.helper.JsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.infra.constants.MetadataCacheConstants;
import org.o2.metadata.infra.constants.OnlineShopConstants;
import org.o2.metadata.infra.entity.OnlineShop;
import org.o2.metadata.infra.redis.OnlineShopRedis;
import org.o2.multi.language.infra.util.O2RedisMultiLanguageHelper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 网店
 *
 * @author yipeng.zhu@hand-china.com 2021-08-10
 **/
@Component
@Slf4j
public class OnlineShopRedisImpl implements OnlineShopRedis {
    private final RedisCacheClient redisCacheClient;
    private final O2RedisMultiLanguageHelper o2RedisMultiLanguageHelper;


    public OnlineShopRedisImpl(RedisCacheClient redisCacheClient, O2RedisMultiLanguageHelper o2RedisMultiLanguageHelper) {
        this.redisCacheClient = redisCacheClient;
        this.o2RedisMultiLanguageHelper = o2RedisMultiLanguageHelper;
    }

    @Override
    public OnlineShop getOnlineShop(String onlineShopCode) {
        String key = OnlineShopConstants.Redis.getOnlineShopDetailKey();
        String shopJsonStr = redisCacheClient.<String, String>opsForHash().get(key, onlineShopCode);
        if (StringUtils.isBlank(shopJsonStr)) {
            return null;
        }
        if (log.isDebugEnabled()) {
            log.info("getOnlineShop onlineShopValue:{}", shopJsonStr);
        }
        OnlineShop onlineShop = JsonHelper.stringToObject(shopJsonStr, OnlineShop.class);
        try {
            o2RedisMultiLanguageHelper.getMultiLang(onlineShop, OnlineShopConstants.Redis.getOnlineShopMultiKey(onlineShop.getTenantId(), onlineShopCode));
        } catch (Exception ex) {
            log.error("online shop multi language query failed, onlineShopCode: {}", onlineShopCode, ex);
        }
        return onlineShop;
    }

    @Override
    public List<OnlineShop> selectShopList(List<String> onlineShopCodes) {
        String key = OnlineShopConstants.Redis.getOnlineShopDetailKey();
        List<String> shopJsonList = redisCacheClient.<String, String>opsForHash().multiGet(key, onlineShopCodes);
        if (CollectionUtils.isEmpty(shopJsonList)) {
            return Collections.emptyList();
        }
        List<OnlineShop> shopList = new ArrayList<>();
        for (String shopJson : shopJsonList) {
            if (StringUtils.isBlank(shopJson)) {
                continue;
            }
            shopList.add(JsonHelper.stringToObject(shopJson, OnlineShop.class));
        }
        if (CollectionUtils.isEmpty(shopList)) {
            return shopList;
        }
        shopList.forEach(shop -> {
            try {
                o2RedisMultiLanguageHelper.getMultiLang(shop, OnlineShopConstants.Redis.getOnlineShopMultiKey(shop.getTenantId(), shop.getOnlineShopCode()));
            } catch (Exception ex) {
                log.error("online shop multi language query failed, onlineShopCode: {}", shop.getOnlineShopCode(), ex);
            }
        });
        return shopList;
    }

    @Override
    public List<OnlineShop> selectShopListByType(Long tenantId, String onlineShopType) {
        List<OnlineShop> onlineShops = CacheHelper.getCache(MetadataCacheConstants.CacheName.O2MD_METADATA,
                MetadataCacheConstants.CacheKey.getOnlineShopPrefix(tenantId, onlineShopType),
                tenantId, onlineShopType,
                this::getOnlineShopByType,
                false);
        if (CollectionUtils.isEmpty(onlineShops)) {
            return onlineShops;
        }
        onlineShops.forEach(shop -> {
            try {
                o2RedisMultiLanguageHelper.getMultiLang(shop,
                        OnlineShopConstants.Redis.getOnlineShopMultiKey(shop.getTenantId(), shop.getOnlineShopCode()));
            } catch (Exception ex) {
                log.error("online shop multi language query failed, onlineShopCode: {}", shop.getOnlineShopCode(), ex);
            }
        });
        return onlineShops;

    }

    /**
     * 通过类型查询网店
     *
     * @param tenantId       租户Id
     * @param onlineShopType 网店类型
     * @return 网店
     */
    protected List<OnlineShop> getOnlineShopByType(Long tenantId, String onlineShopType) {
        String onlineShopKey = OnlineShopConstants.Redis.getOnlineShopKey(tenantId);
        Set<String> onlineShopCodes = redisCacheClient.opsForSet().members(onlineShopKey);
        if (CollectionUtils.isEmpty(onlineShopCodes)) {
            return Collections.emptyList();
        }
        String detailKey = OnlineShopConstants.Redis.getOnlineShopDetailKey();
        List<String> shopJsonList = redisCacheClient.<String, String>opsForHash().multiGet(detailKey, onlineShopCodes);
        if (CollectionUtils.isEmpty(shopJsonList)) {
            return Collections.emptyList();
        }

        List<OnlineShop> shopList = new ArrayList<>();
        shopJsonList.forEach(shopJson -> {
            OnlineShop onlineShop = JsonHelper.stringToObject(shopJson, OnlineShop.class);
            if (onlineShopType.equals(onlineShop.getOnlineShopType())) {
                shopList.add(onlineShop);
            }
        });
        return shopList;
    }
}
