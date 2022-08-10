package org.o2.metadata.infra.redis.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.o2.core.helper.JsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.api.dto.StoreQueryDTO;
import org.o2.metadata.infra.constants.PosConstants;
import org.o2.metadata.infra.entity.Pos;
import org.o2.metadata.infra.redis.PosRedis;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.util.CastUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
        String indexKey = PosConstants.RedisKey.getPosAllStoreKey(tenantId);
        String detailKey = PosConstants.RedisKey.getPosDetailKey(tenantId);
        // 没有传入查询参数时，默认查询所有门店；传入查询参数时，优先按照门店编码查询返回
        // 查询所有门店
        if (ObjectUtils.isEmpty(storeQueryDTO)) {
            return searchPosList(indexKey, detailKey);
        }

        // 根据门店编码查询门店信息
        if (CollectionUtils.isNotEmpty(storeQueryDTO.getPosCodes())) {
            return searchPosByCodes(storeQueryDTO.getPosCodes(), tenantId);
        }

        // 根据省市区查询门店
        if (StringUtils.isNotBlank(storeQueryDTO.getRegionCode()) && StringUtils.isNotBlank(storeQueryDTO.getCityCode())) {
            if (StringUtils.isNotBlank(storeQueryDTO.getDistrictCode())) {
                indexKey = PosConstants.RedisKey.getPosDistrictStoreKey(tenantId, storeQueryDTO.getRegionCode(),
                        storeQueryDTO.getCityCode(), storeQueryDTO.getDistrictCode());
            } else {
                indexKey = PosConstants.RedisKey.getPosCityStoreKey(tenantId, storeQueryDTO.getRegionCode(), storeQueryDTO.getCityCode());
            }
        } else {
            indexKey = PosConstants.RedisKey.getPosAllStoreKey(tenantId);
        }
        return searchPosList(indexKey, detailKey);
    }

    /**
     * 查询门店信息
     *
     * @param indexKey 门店地址mapping key
     * @param detailKey 门店详情key
     * @return
     */
    private List<Pos> searchPosList(String indexKey, String detailKey) {
        DefaultRedisScript<List> getRedisScript = new DefaultRedisScript();
        getRedisScript.setScriptSource(PosConstants.SEARCH_POS_LIST_LUA);
        getRedisScript.setResultType(List.class);
        List<String> posKeys = new ArrayList<>();
        posKeys.add(indexKey);
        posKeys.add(detailKey);
        List<String> posListStr = CastUtils.cast(Optional.ofNullable(redisCacheClient.execute(getRedisScript, posKeys)).orElse(Collections.emptyList()));
        if (CollectionUtils.isEmpty(posListStr)) {
            return Collections.emptyList();
        }
        List<Pos> posList = new ArrayList<>();
        for (String str : posListStr) {
            posList.add(JsonHelper.stringToObject(str, Pos.class));
        }
        return posList;
    }

    /**
     * 根据posCodes查询门店信息
     *
     * @param posCodes 门店编码
     * @param tenantId 租户Id
     * @return 门店信息
     */
    private List<Pos> searchPosByCodes(List<String> posCodes, Long tenantId) {
        List<Object> objectList = new ArrayList<>(posCodes);
        String posDetailKey = PosConstants.RedisKey.getPosDetailKey(tenantId);
        List<Object> objects = redisCacheClient.opsForHash().multiGet(posDetailKey, objectList);
        if (CollectionUtils.isEmpty(objects)) {
            return Collections.emptyList();
        }
        List<Pos> posList = new ArrayList<>();
        for (Object obj : objects) {
            String posStr = String.valueOf(obj);
            posList.add(JsonHelper.stringToObject(posStr, Pos.class));
        }
        return posList;
    }
}
