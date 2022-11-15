package org.o2.metadata.infra.redis.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.o2.cache.util.CacheHelper;
import org.o2.core.helper.JsonHelper;
import org.o2.core.helper.UserHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.infra.constants.MetadataCacheConstants;
import org.o2.metadata.infra.constants.OnlineShopConstants;
import org.o2.metadata.infra.entity.OnlineShop;
import org.o2.metadata.infra.redis.OnlineShopRedis;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 网店
 *
 * @author yipeng.zhu@hand-china.com 2021-08-10
 **/
@Component
@Slf4j
public class OnlineShopRedisImpl implements OnlineShopRedis {
    private final RedisCacheClient redisCacheClient;

    public OnlineShopRedisImpl(RedisCacheClient redisCacheClient) {
        this.redisCacheClient = redisCacheClient;
    }

    @Override
    public OnlineShop getOnlineShop(String onlineShopCode, Long tenantId) {
        String key = OnlineShopConstants.Redis.getOnlineShopKey(tenantId);
        Map<String, String> map = redisCacheClient.<String, String>opsForHash().entries(key);
        if (MapUtils.isEmpty(map)) {
            return new OnlineShop();
        }
        for (Map.Entry<String, String> onlineShopEntry : map.entrySet()) {
            String onlineShopCodeKey = onlineShopEntry.getKey();
            if (onlineShopCode.equals(onlineShopCodeKey)) {
                log.info("getOnlineShop onlineShopValue:{}", onlineShopEntry.getValue());
                return JsonHelper.stringToObject(onlineShopEntry.getValue(), OnlineShop.class);
            }
        }
        return new OnlineShop();
    }

    @Override
    public List<OnlineShop> selectShopList(List<String> onlineShopCodes) {

        String key = OnlineShopConstants.Redis.getOnlineShopKey(UserHelper.getTenantId());
        List<String> shopJsonList = redisCacheClient.<String, String>opsForHash().multiGet(key, onlineShopCodes);
        if (CollectionUtils.isEmpty(shopJsonList)) {
            return Collections.emptyList();
        }
        List<OnlineShop> shopList = new ArrayList<>();
        for (String shopJson : shopJsonList) {
            shopList.add(JsonHelper.stringToObject(shopJson, OnlineShop.class));
        }
        return shopList;
    }

    @Override
    public List<OnlineShop> selectShopListByType(Long tenantId, String onlineShopType) {
        return CacheHelper.getCache(MetadataCacheConstants.CacheName.O2MD_METADATA,
                MetadataCacheConstants.CacheKey.getOnlineShopPrefix(tenantId, onlineShopType),
                tenantId, onlineShopType,
                this::getOnlineShopByType,
                false);

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
        Map<String, String> shopJsonMap = redisCacheClient.<String, String>opsForHash().entries(onlineShopKey);
        if (MapUtils.isEmpty(shopJsonMap)) {
            return Collections.emptyList();
        }

        List<OnlineShop> shopList = new ArrayList<>();
        shopJsonMap.forEach((onlineShopCode, shopJson) -> {
            OnlineShop onlineShop = JsonHelper.stringToObject(shopJson, OnlineShop.class);
            if (onlineShopType.equals(onlineShop.getOnlineShopType())) {
                shopList.add(onlineShop);
            }
        });
        return shopList;
    }

    @Override
    public List<OnlineShop> batchQueryShopList(Long tenantId, List<String> onlineShopCodes) {
        String key = OnlineShopConstants.Redis.getOnlineShopKey(tenantId);
        List<String> shopJsonList = redisCacheClient.<String, String>opsForHash().multiGet(key, onlineShopCodes);
        if (CollectionUtils.isEmpty(shopJsonList)) {
            return Collections.emptyList();
        }
        List<OnlineShop> shopList = new ArrayList<>();
        for (String shopJson : shopJsonList) {
            shopList.add(JsonHelper.stringToObject(shopJson, OnlineShop.class));
        }
        return shopList;
    }
}
