package org.o2.metadata.console.infra.redis.impl;

import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.o2.core.helper.JsonHelper;
import org.o2.data.redis.client.RedisCacheClient;

import org.o2.metadata.console.infra.constant.FreightConstants;
import org.o2.metadata.console.infra.convertor.FreightConverter;
import org.o2.metadata.console.infra.entity.FreightInfo;
import org.o2.metadata.console.infra.entity.FreightTemplate;
import org.o2.metadata.console.infra.entity.FreightTemplateDetail;
import org.o2.metadata.console.infra.redis.FreightRedis;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * 运费模版
 *
 * @author yipeng.zhu@hand-china.com 2021-07-19
 **/
@Component
public class FreightRedisImpl implements FreightRedis {
    private final RedisCacheClient redisCacheClient;

    public FreightRedisImpl(RedisCacheClient redisCacheClient) {
        this.redisCacheClient = redisCacheClient;
    }

    @Override
    public FreightInfo getFreightTemplate(String regionCode, String templateCode, Long tenantId) {
        FreightInfo freightInfo = new FreightInfo();
        freightInfo.setFreightTemplateCode(templateCode);
        String freightDetailKey = FreightConstants.Redis.getFreightDetailKey(tenantId, templateCode);
        List<String>  paramCodes = new ArrayList<>(3);
        paramCodes.add(FreightConstants.Redis.FREIGHT_HEAD_KEY);
        paramCodes.add(FreightConstants.Redis.FREIGHT_DEFAULT_KEY);
        if (StringUtils.isEmpty(regionCode)) {
            paramCodes.add(String.valueOf(BaseConstants.Digital.NEGATIVE_ONE));
        } else {
            paramCodes.add(regionCode);
        }
        List<String> freightTemplates = redisCacheClient.<String, String>opsForHash().multiGet(freightDetailKey, paramCodes);
        String headTemplate =  freightTemplates.get(0);
        freightInfo.setHeadTemplate(StringUtils.isEmpty(headTemplate) ? null : JsonHelper.stringToObject(headTemplate, FreightTemplate.class));


        String cityTemplate = StringUtils.isEmpty(freightTemplates.get(2)) ? freightTemplates.get(1) : freightTemplates.get(2);
        freightInfo.setRegionTemplate(StringUtils.isEmpty(cityTemplate)?null :JsonHelper.stringToObject(cityTemplate, FreightTemplateDetail.class));
        return freightInfo;
    }

    @Override
    public void batchUpdateRedis(List<FreightTemplate> templateList, List<FreightTemplateDetail> detailList, Long tenantId) {
        Map<String, Map<String, String>> groupMap = new HashMap<>(16);
        Map<String, String> defaultMap = new HashMap<>(16);
        Map<String, FreightTemplate> templateMap = templateList.stream().collect(Collectors.toMap(FreightTemplate::getTemplateCode, v -> v));
        Map<String,Map<String,String>> regionMap = new HashMap<>();

        for (FreightTemplateDetail detail : detailList) {

            if (detail.getDefaultFlag() == 1) {
                defaultMap.put(detail.getTemplateCode(), JsonHelper.objectToString(FreightConverter.poToBoObject(detail)));
                continue;
            }
            Map<String, String> map = regionMap.computeIfAbsent(detail.getTemplateCode(), k -> new HashMap<>(16));
            map.put(detail.getRegionCode(),JsonHelper.objectToString(FreightConverter.poToBoObject(detail)));

        }
        List<String> key = new ArrayList<>();
        for (Map.Entry<String, FreightTemplate> entry : templateMap.entrySet()) {
            Map<String, String> hashMap = new HashMap<>();
            FreightTemplate v = entry.getValue();
            hashMap.put(FreightConstants.Redis.FREIGHT_HEAD_KEY, JsonHelper.objectToString(FreightConverter.poToBoObject(v)));
            String k = entry.getKey();
            hashMap.put(FreightConstants.Redis.FREIGHT_DEFAULT_KEY,defaultMap.get(k));
            Map<String,String> map = regionMap.get(k);
            if (null != map && !map.isEmpty()) {
                hashMap.putAll(regionMap.get(k));
            }
            String redisKey = FreightConstants.Redis.getFreightDetailKey(tenantId, k);
            key.add(redisKey);
            groupMap.put(redisKey,hashMap);
        }
        final DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setScriptSource(FreightConstants.Redis.BATCH_UPDATE_FREIGHT_LUA);
        this.redisCacheClient.execute(defaultRedisScript, key, JsonHelper.objectToString(groupMap));
    }

    @Override
    public List<FreightInfo> listFreightTemplate(Long tenantId, List<String> templateCodes) {
        List<FreightInfo> freightInfos = new ArrayList<>();
        redisCacheClient.executePipelined((RedisCallback<FreightInfo>) redisConnection -> {
            for(String templateCode : templateCodes){
                FreightInfo freightInfo = new FreightInfo();
                freightInfo.setFreightTemplateCode(templateCode);
                String freightDetailKey = FreightConstants.Redis.getFreightDetailKey(tenantId, templateCode);
                List<String> paramCodes = new ArrayList<>(2);
                paramCodes.add(FreightConstants.Redis.FREIGHT_HEAD_KEY);
                List<String> freightTemplates = redisCacheClient.<String, String>opsForHash().multiGet(freightDetailKey, paramCodes);
                String headTemplate =  freightTemplates.get(0);
                freightInfo.setHeadTemplate(StringUtils.isEmpty(headTemplate) ? null : JsonHelper.stringToObject(headTemplate, FreightTemplate.class));
                freightInfos.add(freightInfo);
            }
            return null;
        });
        return freightInfos;
    }
}
