package org.o2.metadata.console.infra.redis.impl;

import org.apache.commons.lang3.StringUtils;
import org.o2.core.helper.FastJsonHelper;
import org.o2.data.redis.client.RedisCacheClient;

import org.o2.metadata.console.infra.constant.FreightConstants;
import org.o2.metadata.console.infra.entity.FreightInfo;
import org.o2.metadata.console.infra.entity.FreightTemplate;
import org.o2.metadata.console.infra.entity.FreightTemplateDetail;
import org.o2.metadata.console.infra.redis.FreightRedis;
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
        String freightDetailKey = FreightConstants.RedisKey.getFreightDetailKey(tenantId, templateCode);
        List<String> freightTemplates = redisCacheClient.<String, String>opsForHash().multiGet(freightDetailKey, Arrays.asList(FreightConstants.RedisKey.FREIGHT_HEAD_KEY, FreightConstants.RedisKey.FREIGHT_DEFAULT_KEY, regionCode));
        String headTemplate =  freightTemplates.get(0);
        freightInfo.setHeadTemplate(StringUtils.isEmpty(headTemplate) ? null : FastJsonHelper.stringToObject(headTemplate, FreightTemplate.class));


        String cityTemplate = StringUtils.isEmpty(freightTemplates.get(2)) ? freightTemplates.get(1) : freightTemplates.get(2);
        freightInfo.setRegionTemplate(StringUtils.isEmpty(cityTemplate)?null :FastJsonHelper.stringToObject(cityTemplate, FreightTemplateDetail.class));
        return freightInfo;
    }
}
