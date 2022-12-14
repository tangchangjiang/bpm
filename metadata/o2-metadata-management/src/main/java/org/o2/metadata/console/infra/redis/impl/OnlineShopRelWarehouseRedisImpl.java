package org.o2.metadata.console.infra.redis.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.o2.core.helper.JsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.console.api.co.OnlineShopRelWarehouseCO;
import org.o2.metadata.console.api.co.PosRedisCO;
import org.o2.metadata.console.api.co.WarehouseCO;
import org.o2.metadata.console.api.dto.OnlineShopRelWarehouseInnerDTO;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.constant.OnlineShopConstants;
import org.o2.metadata.console.infra.constant.PosConstants;
import org.o2.metadata.console.infra.constant.WarehouseConstants;
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
import java.util.function.Function;
import java.util.stream.Collectors;

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
            String key = String.valueOf(entry.getKey());
            Integer value = Integer.parseInt(String.valueOf(entry.getValue()));
            onlineShopRelWarehouse.setActiveFlag(value);
            onlineShopRelWarehouse.setWarehouseCode(key);
            list.add(onlineShopRelWarehouse);
        }
        return list;
    }

    @Override
    public List<OnlineShopRelWarehouseCO> listOnlineShopRelWarehouses(OnlineShopRelWarehouseInnerDTO innerDTO, Long tenantId) {
        List<String> onlineShopCodes = innerDTO.getOnlineShopCodes();
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
        posQuery(innerDTO, tenantId, coList);
        // 批量查询
        return coList;
    }


    /**
     * 当PosQueryFlag时，查询门店
     *
     * @param innerDTO innerDTO
     * @param tenantId tenantId
     * @param coList   coList
     */
    protected void posQuery(OnlineShopRelWarehouseInnerDTO innerDTO, Long tenantId, List<OnlineShopRelWarehouseCO> coList) {
        if (!innerDTO.posQueryOrNot()) {
            return;
        }
        if (CollectionUtils.isEmpty(coList)) {
            return;
        }
        List<String> warehouseCodes = coList.stream().map(OnlineShopRelWarehouseCO::getWarehouseCode).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(warehouseCodes)) {
            return;
        }

        String warehouseRedisKey = WarehouseConstants.WarehouseCache.warehouseCacheKey(tenantId);
        String posDetailRedisKey = PosConstants.RedisKey.getPosDetailKey(tenantId);
        List<String> warehouseDetailList = redisCacheClient.<String, String>opsForHash().multiGet(warehouseRedisKey, warehouseCodes);
        Map<String, WarehouseCO> warehouseByWhCode = warehouseDetailList.stream()
                .map(warehouseJson -> JsonHelper.stringToObject(warehouseJson, WarehouseCO.class))
                .collect(Collectors.toMap(WarehouseCO::getWarehouseCode, Function.identity(), (a1, a2) -> a1));
        List<String> posCodes = warehouseByWhCode.values().stream().map(WarehouseCO::getPosCode).collect(Collectors.toList());
        List<String> posDetailList = redisCacheClient.<String, String>opsForHash().multiGet(posDetailRedisKey, posCodes);
        log.info("posDetailRedisKey: {}, posDetailList: {}", JsonHelper.objectToString(posDetailRedisKey), JsonHelper.objectToString(posDetailList));

        if (CollectionUtils.isNotEmpty(posDetailList)) {
            Map<String, PosRedisCO> posDetailByPosCode = posDetailList.stream()
                    .filter(Objects::nonNull)
                    .map(posJson -> JsonHelper.stringToObject(posJson, PosRedisCO.class))
                    .collect(Collectors.toMap(PosRedisCO::getPosCode, Function.identity(), (a1, a2) -> a1));

            for (OnlineShopRelWarehouseCO onlineShopRelWarehouse : coList) {
                WarehouseCO warehouse = warehouseByWhCode.get(onlineShopRelWarehouse.getWarehouseCode());
                if (warehouse != null) {
                    onlineShopRelWarehouse.setWarehouseDetail(warehouse);
                    String posCode = warehouse.getPosCode();
                    PosRedisCO posRedis = posDetailByPosCode.get(posCode);
                    if (posRedis != null) {
                        warehouse.setPosDetail(posRedis);
                        onlineShopRelWarehouse.setStoreTypeFlag(Objects.equals(MetadataConstants.PosType.STORE, posRedis.getPosTypeCode()));
                    }
                }
            }
        }
    }
}
