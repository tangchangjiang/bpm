package org.o2.metadata.console.infra.redis.impl;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.o2.core.helper.FastJsonHelper;
import org.o2.data.redis.client.RedisCacheClient;

import org.o2.metadata.console.infra.constant.CarrierConstants;
import org.o2.metadata.console.infra.constant.FreightConstants;
import org.o2.metadata.console.infra.entity.FreightInfo;
import org.o2.metadata.console.infra.entity.FreightTemplate;
import org.o2.metadata.console.infra.entity.FreightTemplateDetail;
import org.o2.metadata.console.infra.redis.FreightRedis;
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
        List<String> freightTemplates = redisCacheClient.<String, String>opsForHash().multiGet(freightDetailKey, Arrays.asList(FreightConstants.Redis.FREIGHT_HEAD_KEY, FreightConstants.Redis.FREIGHT_DEFAULT_KEY, regionCode));
        String headTemplate =  freightTemplates.get(0);
        freightInfo.setHeadTemplate(StringUtils.isEmpty(headTemplate) ? null : FastJsonHelper.stringToObject(headTemplate, FreightTemplate.class));


        String cityTemplate = StringUtils.isEmpty(freightTemplates.get(2)) ? freightTemplates.get(1) : freightTemplates.get(2);
        freightInfo.setRegionTemplate(StringUtils.isEmpty(cityTemplate)?null :FastJsonHelper.stringToObject(cityTemplate, FreightTemplateDetail.class));
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
                defaultMap.put(detail.getTemplateCode(), FastJsonHelper.objectToString(detail));
                continue;
            }
            Map<String, String> map = regionMap.computeIfAbsent(detail.getTemplateCode(), k -> new HashMap<>(16));
            map.put(detail.getRegionCode(),FastJsonHelper.objectToString(detail));

        }
        for (Map.Entry<String, FreightTemplate> entry : templateMap.entrySet()) {
            Map<String, String> hashMap = new HashMap<>();
            FreightTemplate v = entry.getValue();
            hashMap.put(FreightConstants.Redis.FREIGHT_HEAD_KEY, FastJsonHelper.objectToString(v));

            String k = entry.getKey();
            hashMap.put(FreightConstants.Redis.FREIGHT_DEFAULT_KEY,defaultMap.get(k));
            hashMap.putAll(regionMap.get(k));
            String redisKey = FreightConstants.Redis.getFreightDetailKey(tenantId, k);
            groupMap.put(redisKey,hashMap);
        }
        final DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setScriptSource(FreightConstants.Redis.BATCH_UPDATE_FREIGHT_LUA);
        this.redisCacheClient.execute(defaultRedisScript, new ArrayList<>(), FastJsonHelper.objectToString(groupMap));
    }
}
