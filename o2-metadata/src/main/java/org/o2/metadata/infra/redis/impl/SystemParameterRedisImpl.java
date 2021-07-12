package org.o2.metadata.infra.redis.impl;

import com.alibaba.fastjson.JSONArray;
import org.apache.commons.collections4.CollectionUtils;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.domain.infra.constants.MetadataDomainConstants;
import org.o2.metadata.domain.systemparameter.domain.SystemParamValueDO;
import org.o2.metadata.infra.entity.SystemParameter;
import org.o2.metadata.infra.redis.SystemParameterRedis;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *
 * 系统参数
 *
 * @author yipeng.zhu@hand-china.com 2021-07-11
 **/
@Component
public class SystemParameterRedisImpl implements SystemParameterRedis {
    private final RedisCacheClient redisCacheClient;

    public SystemParameterRedisImpl(RedisCacheClient redisCacheClient) {
        this.redisCacheClient = redisCacheClient;
    }

    @Override
    public SystemParameter getSystemParameter(String paramCode, Long tenantId) {

        SystemParameter systemParameter = new SystemParameter();
        systemParameter.setParamCode(paramCode);
        //hash类型
        String key = String.format(MetadataDomainConstants.SystemParameter.KEY, tenantId, MetadataDomainConstants.ParamType.KV);
        String object = redisCacheClient.<String,String>opsForHash().get(key, paramCode);
        if (object != null) {
            systemParameter.setDefaultValue(object);
        }
        //set类型 不重复
        String keySet = String.format(MetadataDomainConstants.SystemParameter.KEY, tenantId, MetadataDomainConstants.ParamType.SET);
        object = redisCacheClient.<String,String>opsForHash().get(keySet, paramCode);
        if (null != object) {
            systemParameter.setSetSystemParamValue(new HashSet(JSONArray.parseArray(object, SystemParamValueDO.class)));
        }
        return systemParameter;
    }

    @Override
    public List<SystemParameter> listSystemParameters(List<String> paramCodeList, Long tenantId) {
        List<SystemParameter> doList = new ArrayList<>();
        String key = String.format(MetadataDomainConstants.SystemParameter.KEY, tenantId, MetadataDomainConstants.ParamType.KV);
        List<String> list = redisCacheClient.<String,String>opsForHash().multiGet(key,paramCodeList);
        if (CollectionUtils.isNotEmpty(list)) {
            for (int i = 0; i < paramCodeList.size(); i++) {
                SystemParameter systemParameter = new SystemParameter();
                systemParameter.setParamCode(paramCodeList.get(i));
                systemParameter.setDefaultValue(list.get(i));
                doList.add(systemParameter);
            }
            return doList;
        }
        String keySet = String.format(MetadataDomainConstants.SystemParameter.KEY, tenantId, MetadataDomainConstants.ParamType.SET);
        List<String> listSet = redisCacheClient.<String,String>opsForHash().multiGet(keySet,paramCodeList);
        if (CollectionUtils.isNotEmpty(listSet)) {
            for (int i = 0; i < paramCodeList.size(); i++) {
                SystemParameter systemParameter = new SystemParameter();
                systemParameter.setParamCode(paramCodeList.get(i));
                systemParameter.setSetSystemParamValue(new HashSet(JSONArray.parseArray(listSet.get(i), SystemParamValueDO.class)));
                doList.add(systemParameter);
            }
            return doList;
        }
        return doList;
    }
}
