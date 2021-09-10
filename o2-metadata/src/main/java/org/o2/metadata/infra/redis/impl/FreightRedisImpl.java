package org.o2.metadata.infra.redis.impl;

import org.apache.commons.lang3.StringUtils;
import org.o2.core.helper.JsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.infra.constants.FreightConstants;
import org.o2.metadata.infra.entity.FreightInfo;
import org.o2.metadata.infra.entity.FreightTemplate;
import org.o2.metadata.infra.entity.FreightTemplateDetail;
import org.o2.metadata.infra.redis.FreightRedis;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

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
        String freightDetailKey = FreightConstants.RedisKey.getFreightDetailKey(tenantId, templateCode);
        List<String> freightTemplates = redisCacheClient.<String, String>opsForHash().multiGet(freightDetailKey, Arrays.asList(FreightConstants.RedisKey.FREIGHT_HEAD_KEY, FreightConstants.RedisKey.FREIGHT_DEFAULT_KEY, regionCode));
        String headTemplate =  freightTemplates.get(0);
        if (StringUtils.isEmpty(headTemplate)) {
            freightInfo.setHeadTemplate(null);
        } else {
            JsonHelper.stringToObject(headTemplate, FreightTemplate.class);
        }

        String cityTemplate = StringUtils.isEmpty(freightTemplates.get(2)) ? freightTemplates.get(1) : freightTemplates.get(2);
        if (StringUtils.isEmpty(cityTemplate)) {
            freightInfo.setRegionTemplate(null);
        } else {
            JsonHelper.stringToObject(cityTemplate, FreightTemplateDetail.class);
        }
        return freightInfo;
    }
}
