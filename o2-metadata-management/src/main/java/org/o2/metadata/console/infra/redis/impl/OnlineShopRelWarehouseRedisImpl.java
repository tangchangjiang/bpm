package org.o2.metadata.console.infra.redis.impl;

import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.console.infra.constant.OnlineShopConstants;
import org.o2.metadata.console.infra.entity.OnlineShopRelWarehouse;
import org.o2.metadata.console.infra.redis.OnlineShopRelWarehouseRedis;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * 网店关联仓库
 *
 * @author yipeng.zhu@hand-china.com 2021-07-13
 **/
@Component
public class OnlineShopRelWarehouseRedisImpl implements OnlineShopRelWarehouseRedis {
    private final RedisCacheClient redisCacheClient;

    public OnlineShopRelWarehouseRedisImpl(RedisCacheClient redisCacheClient) {
        this.redisCacheClient = redisCacheClient;
    }

    @Override
    public List<OnlineShopRelWarehouse> listOnlineShopRelWarehouses(String onlineShopCode, Long tenantId) {
        String hashKey = String.format(OnlineShopConstants.Redis.KEY_ONLINE_SHOP_REL_WAREHOUSE, tenantId, onlineShopCode);
        Map<Object,Object> map = redisCacheClient.opsForHash().entries(hashKey);
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
}
