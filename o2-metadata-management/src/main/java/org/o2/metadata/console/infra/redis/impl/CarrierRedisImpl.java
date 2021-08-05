package org.o2.metadata.console.infra.redis.impl;

import com.google.common.collect.Maps;
import org.o2.core.helper.FastJsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.console.app.bo.CarrierRedisBO;
import org.o2.metadata.console.infra.constant.CarrierConstants;
import org.o2.metadata.console.infra.entity.Carrier;
import org.o2.metadata.console.infra.redis.CarrierRedis;
import org.o2.metadata.console.infra.repository.CarrierRepository;
import org.springframework.stereotype.Component;

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
    private final CarrierRepository carrierRepository;

    public CarrierRedisImpl(RedisCacheClient redisCacheClient, CarrierRepository carrierRepository) {
        this.redisCacheClient = redisCacheClient;
        this.carrierRepository = carrierRepository;
    }

    @Override
    public void batchUpdateRedis(Long tenantId) {
        Carrier query = new Carrier();
        query.setTenantId(tenantId);
        query.setActiveFlag(1);
        List<Carrier> carriers = carrierRepository.select(query);
        Map<String,String> hashMap = Maps.newHashMapWithExpectedSize(carriers.size());
        CarrierRedisBO bo = new CarrierRedisBO();
        for (Carrier carrier : carriers) {
            bo.setCarrierCode(carrier.getCarrierCode());
            bo.setCarrierName(carrier.getCarrierName());
            bo.setCarrierTypeCode(carrier.getCarrierTypeCode());
            hashMap.put(carrier.getCarrierCode(), FastJsonHelper.objectToString(bo));
        }
       String key = CarrierConstants.Redis.getCarrierKey(tenantId);
        redisCacheClient.opsForHash().putAll(key,hashMap);

    }
}
