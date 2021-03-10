package org.o2.metadata.app.service.impl;

import com.alibaba.fastjson.JSONArray;

import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.app.service.SysParameterService;
import org.o2.metadata.core.domain.vo.SystemParamDetailVO;
import org.o2.metadata.core.domain.vo.SystemParamValueVO;
import org.o2.metadata.core.infra.constants.MetadataConstants;
import org.springframework.stereotype.Service;

import java.util.HashSet;

/**
 * SysParameter RPC Provider
 *
 * @author mark.bao@hand-china.com 2019/11/29
 */
@Service
public class SysParameterServiceImpl implements SysParameterService {
    private final RedisCacheClient redisCacheClient;

    public SysParameterServiceImpl(final RedisCacheClient redisCacheClient) {
        this.redisCacheClient = redisCacheClient;
    }
    @Override
    public SystemParamDetailVO listSystemParameter(String paramCode, Long tenantId) {
        SystemParamDetailVO response = new SystemParamDetailVO();
        response.setParamCode(paramCode);
        String key = String.format(MetadataConstants.SystemParameter.KEY, tenantId, MetadataConstants.ParamType.KV);
        Object valueObj = redisCacheClient.opsForHash().get(key, paramCode);
        if (null != valueObj) {
            response.setKvValue(String.valueOf(valueObj));
        }
        key = String.format(MetadataConstants.SystemParameter.KEY, tenantId, MetadataConstants.ParamType.SET);
        valueObj = redisCacheClient.opsForHash().get(key, paramCode);
        if (null != valueObj) {
            response.setSetValue(new HashSet(JSONArray.parseArray(String.valueOf(valueObj), SystemParamValueVO.class)));
        }
        return response;
    }
}
