package org.o2.metadata.console.domain.repository.impl;

import com.alibaba.fastjson.JSONArray;
import org.o2.core.helper.FastJsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.console.api.vo.SystemParamValueVO;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.core.infra.constants.MetadataDomainConstants;
import org.o2.metadata.core.systemparameter.domain.SystemParameterDO;
import org.o2.metadata.core.systemparameter.repository.SystemParameterDomainRepository;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

/**
 *
 * 系统参数仓库层实现
 *
 * @author yipeng.zhu@hand-china.com 2021-07-09
 **/
@Component
public class SystemParameterDomainRepositoryImpl implements SystemParameterDomainRepository {
    private  final RedisCacheClient redisCacheClient;

    public SystemParameterDomainRepositoryImpl(RedisCacheClient redisCacheClient) {
        this.redisCacheClient = redisCacheClient;
    }

    @Override
    public List<SystemParameterDO> listSystemParameters(List<String> paramCodeList, Long tenantId) {
        return null;
    }

    @Override
    public Object getSystemParameter(String paramCode, String key) {
/*        SystemParameterDO response = new SystemParameterDO();
        response.setParamCode(paramCode);
        String key = String.format(MetadataDomainConstants.SystemParameter.KEY, tenantId, MetadataConstants.ParamType.KV);*/
        return redisCacheClient.opsForHash().get(key, paramCode);
/*        if (null != valueObj) {
           return valueObj;
        }
        key = String.format(MetadataConstants.SystemParameter.KEY, tenantId, MetadataConstants.ParamType.SET);
        valueObj = redisCacheClient.opsForHash().get(key, paramCode);
        if (null != valueObj) {
            response.setSetSystemParamValue(new HashSet(FastJsonHelper.stringToArray(String.valueOf(valueObj), SystemParamValueVO.class)));
        }
        return response;*/
    }
}
