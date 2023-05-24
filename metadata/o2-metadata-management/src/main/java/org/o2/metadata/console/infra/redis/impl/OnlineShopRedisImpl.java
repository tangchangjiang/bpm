package org.o2.metadata.console.infra.redis.impl;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.o2.core.helper.JsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.console.api.dto.OnlineShopQueryInnerDTO;
import org.o2.metadata.console.app.bo.OnlineShopCacheBO;
import org.o2.metadata.console.app.bo.OnlineShopMultiRedisBO;
import org.o2.metadata.console.infra.constant.OnlineShopConstants;
import org.o2.metadata.console.infra.constant.WarehouseConstants;
import org.o2.metadata.console.infra.convertor.OnlineShopConverter;
import org.o2.metadata.console.infra.entity.OnlineShop;
import org.o2.metadata.console.infra.entity.OnlineShopRelWarehouse;
import org.o2.metadata.console.infra.entity.Warehouse;
import org.o2.metadata.console.infra.redis.OnlineShopRedis;
import org.o2.metadata.console.infra.repository.OnlineShopRepository;
import org.o2.multi.language.management.infra.util.O2RedisMultiLanguageManagementHelper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 网店
 *
 * @author yipeng.zhu@hand-china.com 2021-08-06
 **/
@Component
public class OnlineShopRedisImpl implements OnlineShopRedis {
    private final OnlineShopRepository onlineShopRepository;
    private final RedisCacheClient redisCacheClient;
    private final O2RedisMultiLanguageManagementHelper o2RedisMultiLanguageManagementHelper;

    public OnlineShopRedisImpl(OnlineShopRepository onlineShopRepository,
                               RedisCacheClient redisCacheClient, O2RedisMultiLanguageManagementHelper o2RedisMultiLanguageManagementHelper) {
        this.onlineShopRepository = onlineShopRepository;
        this.redisCacheClient = redisCacheClient;
        this.o2RedisMultiLanguageManagementHelper = o2RedisMultiLanguageManagementHelper;
    }

    @Override
    public void updateRedis(String onlineShopCode, Long tenantId) {
        OnlineShopQueryInnerDTO query = new OnlineShopQueryInnerDTO();
        query.setOnlineShopCodes(Collections.singletonList(onlineShopCode));

        List<OnlineShop> list = onlineShopRepository.listOnlineShops(query, tenantId, BaseConstants.Flag.NO);
        if (list.isEmpty()) {
            return;
        }
        OnlineShop onlineShop = list.get(0);
        redisCacheClient.executePipelined(new SessionCallback<Object>() {
            @SuppressWarnings("unchecked")
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String key = OnlineShopConstants.Redis.getOnlineShopKey(tenantId);
                String detailKey = OnlineShopConstants.Redis.getOnlineShopDetailKey();
                if (onlineShop.getActiveFlag().equals(BaseConstants.Flag.NO)) {
                    operations.opsForSet().remove(key, onlineShopCode);
                    operations.opsForHash().delete(detailKey, onlineShopCode);
                } else {
                    operations.opsForSet().add(key, onlineShopCode);
                    operations.opsForHash().put(detailKey, onlineShopCode, JsonHelper.objectToString(OnlineShopConverter.poToBoObject(onlineShop)));
                }
                return null;
            }
        });
        insertMultiShop(onlineShop);
    }

    @Override
    public void batchUpdateShopRelWh(List<OnlineShopRelWarehouse> list, Long tenantId, String handleType) {
        if (list.isEmpty() || StringUtils.isEmpty(handleType)) {
            return;
        }
        List<String> key = new ArrayList<>();
        Map<String, List<OnlineShopRelWarehouse>> map = list.stream().collect(Collectors.groupingBy(OnlineShopRelWarehouse::getOnlineShopCode));
        Map<String, Map<String, String>> groupMap = Maps.newHashMapWithExpectedSize(map.size());
        for (Map.Entry<String, List<OnlineShopRelWarehouse>> entry : map.entrySet()) {
            String redisKey = OnlineShopConstants.Redis.getOnlineShopRelWarehouseKey(entry.getKey(), tenantId);
            List<OnlineShopRelWarehouse> v = entry.getValue();
            Map<String, String> hashMap = Maps.newHashMapWithExpectedSize(v.size());
            for (OnlineShopRelWarehouse warehouse : v) {
                hashMap.put(warehouse.getWarehouseCode(), String.valueOf(warehouse.getActiveFlag()));
            }
            key.add(redisKey);
            groupMap.put(redisKey, hashMap);
        }
        final DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript<>();
        if (OnlineShopConstants.Redis.UPDATE.equals(handleType)) {
            defaultRedisScript.setScriptSource(OnlineShopConstants.Redis.UPDATE_CACHE_LUA);
        } else {
            // 删除key
            defaultRedisScript.setScriptSource(OnlineShopConstants.Redis.DELETE_CACHE_LUA);
        }
        this.redisCacheClient.execute(defaultRedisScript, key, JsonHelper.objectToString(groupMap));

    }

    @Override
    public void batchUpdateRedis(List<OnlineShop> list, Long tenantId) {
        if (list.isEmpty()) {
            return;
        }
        List<OnlineShopCacheBO> bos = OnlineShopConverter.poToBoListObjects(list);
        Map<String, String> map = Maps.newHashMapWithExpectedSize(bos.size());
        for (OnlineShopCacheBO bo : bos) {
            map.put(bo.getOnlineShopCode(), JsonHelper.objectToString(bo));
        }
        redisCacheClient.executePipelined(new SessionCallback<Object>() {
            @SuppressWarnings("unchecked")
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String[] shopCodes = list.stream().map(OnlineShop::getOnlineShopCode).toArray(String[]::new);
                String key = OnlineShopConstants.Redis.getOnlineShopKey(tenantId);
                String detailKey = OnlineShopConstants.Redis.getOnlineShopDetailKey();
                operations.opsForSet().add(key, shopCodes);
                operations.opsForHash().putAll(detailKey, map);
                return null;
            }
        });
    }

    @Override
    public void syncMerchantMetaInfo(OnlineShop onlineShop, Warehouse warehouse, OnlineShopRelWarehouse shopRelWh) {
        redisCacheClient.executePipelined(new SessionCallback<Object>() {
            @SuppressWarnings({"unchecked"})
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                // 1. 同步网店
                String onlineShopCode = onlineShop.getOnlineShopCode();
                String shopIndexKey = OnlineShopConstants.Redis.getOnlineShopKey(onlineShop.getTenantId());
                String shopDetailKey = OnlineShopConstants.Redis.getOnlineShopDetailKey();
                operations.opsForSet().add(shopIndexKey, onlineShopCode);
                operations.opsForHash().put(shopDetailKey, onlineShopCode, JsonHelper.objectToString(OnlineShopConverter.poToBoObject(onlineShop)));
                // 2. 同步仓库信息
                String warehouseCacheKey = WarehouseConstants.WarehouseCache.warehouseCacheKey(warehouse.getTenantId());
                operations.opsForHash().put(warehouseCacheKey, warehouse.getWarehouseCode(), JsonHelper.objectToString(warehouse));
                // 3. 同步网店关联仓库
                String shopRelWhKey = OnlineShopConstants.Redis.getOnlineShopRelWarehouseKey(onlineShop.getOnlineShopCode(), shopRelWh.getTenantId());
                operations.opsForHash().put(shopRelWhKey, warehouse.getWarehouseCode(), String.valueOf(shopRelWh.getActiveFlag()));
                return null;
            }
        });
    }

    @Override
    public void insertMultiShop(OnlineShop onlineShop) {
        OnlineShopMultiRedisBO onlineShopMultiRedis = new OnlineShopMultiRedisBO();
        onlineShopMultiRedis.setOnlineShopId(onlineShop.getOnlineShopId());
        onlineShopMultiRedis.setOnlineShopName(onlineShop.getOnlineShopName());
        onlineShopMultiRedis.setOnlineShopCode(onlineShop.getOnlineShopCode());
        o2RedisMultiLanguageManagementHelper.saveMultiLanguage(onlineShopMultiRedis, OnlineShopConstants.Redis.getOnlineShopMutliKey(onlineShop.getTenantId(), onlineShop.getOnlineShopCode()));
    }
}
