package org.o2.metadata.console.infra.redis.impl;

import com.google.common.collect.Maps;
import org.o2.core.helper.JsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.console.app.bo.CarrierRedisBO;
import org.o2.metadata.console.infra.constant.CarrierConstants;
import org.o2.metadata.console.infra.entity.Carrier;
import org.o2.metadata.console.infra.redis.CarrierRedis;
import org.o2.metadata.console.infra.repository.CarrierRepository;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
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
        query.setActiveFlag(0);
        List<Carrier> deletes = carrierRepository.select(query);
        Map<String, String> hashMap = Maps.newHashMapWithExpectedSize(carriers.size());
        Map<String, String> deleteMap = Maps.newHashMapWithExpectedSize(deletes.size());
        CarrierRedisBO bo = new CarrierRedisBO();
        for (Carrier carrier : carriers) {
            bo.setCarrierCode(carrier.getCarrierCode());
            bo.setCarrierName(carrier.getCarrierName());
            bo.setCarrierTypeCode(carrier.getCarrierTypeCode());
            hashMap.put(carrier.getCarrierCode(), JsonHelper.objectToString(bo));
        }
        for (Carrier carrier : deletes) {
            deleteMap.put(carrier.getCarrierCode(),String.valueOf(tenantId));
        }
        String key = CarrierConstants.Redis.getCarrierKey(tenantId);
        final DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setScriptSource(CarrierConstants.Redis.CARRIER_CACHE_LUA);
        this.redisCacheClient.execute(defaultRedisScript, Collections.singletonList(key), JsonHelper.objectToString(hashMap),JsonHelper.objectToString(deleteMap));
    }

    @Override
    public void deleteRedis(List<Carrier> list, Long tenantId) {
        if (list.isEmpty()) {
            return;
        }
       List<String> deleteList = new ArrayList<>(list.size());
        for (Carrier carrier : list) {
            deleteList.add(carrier.getCarrierCode());
        }
        String key = CarrierConstants.Redis.getCarrierKey(tenantId);
        redisCacheClient.opsForHash().delete(key,deleteList.toArray());
    }
}
