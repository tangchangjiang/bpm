package org.o2.metadata.infra.redis.impl;

import org.o2.core.helper.FastJsonHelper;
import org.o2.data.redis.client.RedisCacheClient;

import org.o2.metadata.infra.constants.CarrierConstants;
import org.o2.metadata.infra.entity.Carrier;
import org.o2.metadata.infra.redis.CarrierRedis;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * 承运商
 *
 * @author yipeng.zhu@hand-china.com 2021-08-05
 **/
@Component
public class CarrierRedisImpl implements CarrierRedis {
    private final RedisCacheClient redisCacheClient;

    public CarrierRedisImpl(RedisCacheClient redisCacheClient) {
        this.redisCacheClient = redisCacheClient;
    }

    @Override
    public List<Carrier> listCarriers(Long tenantId) {
      String key = CarrierConstants.Redis.getCarrierKey(tenantId);
      Map<String,String> map = redisCacheClient.<String,String>opsForHash().entries(key);
      List<Carrier> carriers = new ArrayList<>();
      if (map.isEmpty()) {
          return carriers;
      }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String v = entry.getValue();
            carriers.add(FastJsonHelper.stringToObject(v, Carrier.class));
        }
        return carriers;
    }
}
