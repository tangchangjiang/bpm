package org.o2.metadata.app.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.api.dto.FreightDTO;
import org.o2.metadata.api.vo.FreightInfoVO;
import org.o2.metadata.app.service.FreightService;
import org.o2.metadata.infra.constants.FreightConstants;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 运费计算服务默认实现
 *
 * @author peng.xu@hand-china.com 2019-06-18
 */
@Service
public class FreightServiceImpl implements FreightService {
    private RedisCacheClient redisCacheClient;

    public FreightServiceImpl(RedisCacheClient redisCacheClient) {
        this.redisCacheClient = redisCacheClient;
    }

    @Override
    public FreightInfoVO getFreightTemplate(FreightDTO freight) {
        String templateCode = freight.getTemplateCodes();
        String cityCode = freight.getRegionCode();
        FreightInfoVO freightInfo = new FreightInfoVO();

        String freightDetailKey = FreightConstants.RedisKey.getFreightDetailKey(freight.getTenantId(), templateCode);
        List<String> freightTemplates = redisCacheClient.<String, String>opsForHash().multiGet(freightDetailKey, Arrays.asList(FreightConstants.RedisKey.FREIGHT_HEAD_KEY, FreightConstants.RedisKey.FREIGHT_DEFAULT_KEY, cityCode));
        freightInfo.setHeadTemplate(freightTemplates.get(0));
        String cityTemplate = StringUtils.isEmpty(freightTemplates.get(2)) ? freightTemplates.get(1) : freightTemplates.get(2);
        freightInfo.setRegionTemplate(cityTemplate);
        return freightInfo;
    }

}
