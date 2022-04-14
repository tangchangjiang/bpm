package org.o2.metadata.infra.redis.impl;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.o2.core.helper.JsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.api.dto.StoreQueryDTO;
import org.o2.metadata.infra.constants.PosConstants;
import org.o2.metadata.infra.entity.Pos;
import org.o2.metadata.infra.redis.PosRedis;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chao.yang05@hand-china.com 2022/4/14
 */
@Component
public class PosRedisImpl implements PosRedis {

    private final RedisCacheClient redisCacheClient;

    public PosRedisImpl(RedisCacheClient redisCacheClient) {
        this.redisCacheClient = redisCacheClient;
    }

    @Override
    public Pos getPosPickUpInfo(String posCode, Long tenantId) {
        String posDetailKey = PosConstants.RedisKey.getPosDetailKey(tenantId);
        Object posInfo = redisCacheClient.opsForHash().get(posDetailKey, posCode);
        if (ObjectUtils.isEmpty(posInfo)) {
            return null;
        }
        String posStr = String.valueOf(posInfo);
        return JsonHelper.stringToObject(posStr, Pos.class);
    }

    @Override
    public List<Pos> getStoreInfoList(StoreQueryDTO storeQueryDTO, Long tenantId) {
        if (ObjectUtils.isEmpty(storeQueryDTO)) {
            return null;
        }
        String indexKey;
        String detailKey = PosConstants.RedisKey.getPosDetailKey(tenantId);
        if (storeQueryDTO.getLongitude() != null && storeQueryDTO.getLatitude() != null) {
            indexKey = PosConstants.RedisKey.getPosAllStoreKey(tenantId);
            return searchPosList(indexKey, detailKey);
        }
        if (StringUtils.isNotBlank(storeQueryDTO.getRegionCode()) && StringUtils.isNotBlank(storeQueryDTO.getCityCode())) {
            if (StringUtils.isNotBlank(storeQueryDTO.getDistrictCode())) {
                indexKey = PosConstants.RedisKey.getPosDistrictStoreKey(tenantId, storeQueryDTO.getRegionCode(),
                        storeQueryDTO.getCityCode(), storeQueryDTO.getDistrictCode());
            } else {
                indexKey = PosConstants.RedisKey.getPosCityStoreKey(tenantId, storeQueryDTO.getRegionCode(), storeQueryDTO.getCityCode());
            }
            return searchPosList(indexKey, detailKey);
        }
        return null;
    }

    private List<Pos> searchPosList(String indexKey, String detailKey) {

        DefaultRedisScript getRedisScript = new DefaultRedisScript();
        // 订单key
        getRedisScript.setScriptSource(PosConstants.SEARCH_POS_LIST_LUA);
        getRedisScript.setResultType(List.class);
        List<String> posKeys = new ArrayList<>();
        posKeys.add(indexKey);
        posKeys.add(detailKey);
        Object object = redisCacheClient.execute(getRedisScript, posKeys);
        if (ObjectUtils.isEmpty(object)) {
            return null;
        }
        List<String> posListStr = (List<String>) object;
        List<Pos> posList = new ArrayList<>();
        for (String str : posListStr) {
            posList.add(JsonHelper.stringToObject(str, Pos.class));
        }
        return posList;
    }
}
