package org.o2.metadata.infra.redis.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.helper.LanguageHelper;
import org.o2.core.helper.JsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.infra.constants.FreightConstants;
import org.o2.metadata.infra.entity.FreightInfo;
import org.o2.metadata.infra.entity.FreightTemplate;
import org.o2.metadata.infra.entity.FreightTemplateDetail;
import org.o2.metadata.infra.redis.FreightRedis;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 *
 * 运费模版
 *
 * @author yipeng.zhu@hand-china.com 2021-07-19
 **/
@Component
@Slf4j
public class FreightRedisImpl implements FreightRedis {
    private final RedisCacheClient redisCacheClient;

    public FreightRedisImpl(RedisCacheClient redisCacheClient) {
        this.redisCacheClient = redisCacheClient;
    }

    @Override
    public FreightInfo getFreightTemplate(String regionCode, String templateCode, Long tenantId) {
        FreightInfo freightInfo = new FreightInfo();
        freightInfo.setFreightTemplateCode(templateCode);
        String freightDetailKey = FreightConstants.RedisKey.getFreightDetailKey(tenantId, templateCode);
        // 按头模板 ,默认模板，地区模板取值
        List<String> freightTemplates = redisCacheClient.<String, String>opsForHash().multiGet(freightDetailKey, Arrays.asList(FreightConstants.RedisKey.FREIGHT_HEAD_KEY, FreightConstants.RedisKey.FREIGHT_DEFAULT_KEY, regionCode));
        String headTemplate =  freightTemplates.get(0);
        if (StringUtils.isEmpty(headTemplate)) {
            freightInfo.setHeadTemplate(null);
        } else {
            FreightTemplate one = JsonHelper.stringToObject(headTemplate, FreightTemplate.class);
            // 对运费模板多语言进行处理
            if (Objects.nonNull(one) && MapUtils.isNotEmpty(one.getTlsMap())
                    && one.getTlsMap().containsKey("templateName")) {
                String tlsName = one.getTlsMap().get("templateName").get(LanguageHelper.language());
                if (StringUtils.isNotBlank(tlsName)) {
                    one.setTemplateName(tlsName);
                }
            }
            freightInfo.setHeadTemplate(one);

        }
        // 地区模板没有值，取默认模板
        String cityTemplate = StringUtils.isEmpty(freightTemplates.get(2)) ? freightTemplates.get(1) : freightTemplates.get(2);
        if (StringUtils.isEmpty(cityTemplate)) {
            freightInfo.setRegionTemplate(null);
        } else {
            freightInfo.setRegionTemplate(JsonHelper.stringToObject(cityTemplate, FreightTemplateDetail.class));
        }
        return freightInfo;
    }

    @Override
    public List<FreightInfo> listFreightTemplate(Long tenantId, List<String> templateCodes) {
        List<FreightInfo> freightInfos = new ArrayList<>();
        redisCacheClient.executePipelined((RedisCallback<FreightInfo>) redisConnection -> {
            for (String templateCode : templateCodes) {
                FreightInfo freightInfo = new FreightInfo();
                freightInfo.setFreightTemplateCode(templateCode);
                String freightDetailKey = FreightConstants.RedisKey.getFreightDetailKey(tenantId, templateCode);
                List<String> paramCodes = new ArrayList<>(2);
                paramCodes.add(FreightConstants.RedisKey.FREIGHT_HEAD_KEY);
                List<String> freightTemplates = redisCacheClient.<String, String>opsForHash().multiGet(freightDetailKey, paramCodes);
                String headTemplate =  freightTemplates.get(0);
                if (StringUtils.isEmpty(headTemplate)) {
                    freightInfo.setHeadTemplate(null);
                } else {
                    FreightTemplate one = JsonHelper.stringToObject(headTemplate, FreightTemplate.class);
                    // 对运费模板多语言进行处理
                    if (Objects.nonNull(one) && MapUtils.isNotEmpty(one.getTlsMap())
                            && one.getTlsMap().containsKey("templateName")) {
                        String tlsName = one.getTlsMap().get("templateName").get(LanguageHelper.language());
                        if (StringUtils.isNotBlank(tlsName)) {
                            one.setTemplateName(tlsName);
                        }
                    }
                    freightInfo.setHeadTemplate(one);

                }
                freightInfos.add(freightInfo);
            }
            return null;
        });
        return freightInfos;
    }
}
