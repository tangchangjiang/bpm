package org.o2.metadata.console.infra.redis.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.o2.core.helper.JsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.console.api.co.OnlineShopRelWarehouseCO;
import org.o2.metadata.console.infra.constant.OnlineShopConstants;
import org.o2.metadata.console.infra.entity.OnlineShopRelWarehouse;
import org.o2.metadata.console.infra.redis.OnlineShopRelWarehouseRedis;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 网店关联仓库
 *
 * @author yipeng.zhu@hand-china.com 2021-07-13
 **/
@Slf4j
@Component
public class OnlineShopRelWarehouseRedisImpl implements OnlineShopRelWarehouseRedis {
    private final RedisCacheClient redisCacheClient;

    public OnlineShopRelWarehouseRedisImpl(RedisCacheClient redisCacheClient) {
        this.redisCacheClient = redisCacheClient;
    }

    @Override
    public List<OnlineShopRelWarehouse> listOnlineShopRelWarehouses(String onlineShopCode, Long tenantId) {
        String hashKey = String.format(OnlineShopConstants.Redis.KEY_ONLINE_SHOP_REL_WAREHOUSE, tenantId, onlineShopCode);
        Map<Object, Object> map = redisCacheClient.opsForHash().entries(hashKey);
        List<OnlineShopRelWarehouse> list = new ArrayList<>();
        if (map.isEmpty()) {
            return list;
        }
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            OnlineShopRelWarehouse onlineShopRelWarehouse = new OnlineShopRelWarehouse();
            String key  = String.valueOf(entry.getKey());
            Integer value = Integer.parseInt(String.valueOf(entry.getValue()));
            onlineShopRelWarehouse.setActiveFlag(value);
            onlineShopRelWarehouse.setWarehouseCode(key);
            list.add(onlineShopRelWarehouse);
        }
        return list;
    }

    @Override
    public List<OnlineShopRelWarehouseCO> listOnlineShopRelWarehouses(List<String> onlineShopCodes, Long tenantId) {
        List<String> keys = new ArrayList<>(onlineShopCodes.size());
        for (String code : onlineShopCodes) {
            String hashKey = String.format(OnlineShopConstants.Redis.KEY_ONLINE_SHOP_REL_WAREHOUSE, tenantId, code);
            keys.add(hashKey);
        }
        List<Object> objects = null;
        try {
            objects = redisCacheClient.executePipelined(new RedisCallback<OnlineShopRelWarehouseCO>() {
                @Override
                public OnlineShopRelWarehouseCO doInRedis(@NonNull RedisConnection redisConnection) throws DataAccessException {
                    StringRedisConnection stringConnection = (StringRedisConnection) redisConnection;
                    for (String key : keys) {
                        stringConnection.hGetAll(key.getBytes());
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            log.error("query  OnlineShopRelWarehouse redis error");
        }
        List<OnlineShopRelWarehouseCO> coList = new ArrayList<>(4);
        for (int i = 0; i < onlineShopCodes.size(); ++i) {
            Object object = Objects.requireNonNull(objects).get(i);
            Map<String, String> map = JsonHelper.byteToMap(JsonHelper.objectToByte(object));
            for (Map.Entry<String, String> entry : map.entrySet()) {
                OnlineShopRelWarehouseCO co = new OnlineShopRelWarehouseCO();
                String key = String.valueOf(entry.getKey());
                Integer value = Integer.parseInt(String.valueOf(entry.getValue()));
                co.setActiveFlag(value);
                co.setWarehouseCode(key);
                co.setOnlineShopCode(onlineShopCodes.get(i));
                coList.add(co);
            }
        }
        return coList;
    }
}
