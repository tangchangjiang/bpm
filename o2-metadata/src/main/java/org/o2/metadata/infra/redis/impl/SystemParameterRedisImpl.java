package org.o2.metadata.infra.redis.impl;

import com.alibaba.fastjson.JSONArray;
import org.apache.commons.collections4.CollectionUtils;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.infra.constants.SystemParameterConstants;
import org.o2.metadata.infra.entity.SystemParamValue;
import org.o2.metadata.infra.entity.SystemParameter;
import org.o2.metadata.infra.redis.SystemParameterRedis;
import org.springframework.stereotype.Component;

import java.util.*;

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
        String key = String.format(SystemParameterConstants.Redis.KEY, tenantId, SystemParameterConstants.ParamType.KV);
        String object = redisCacheClient.<String,String>opsForHash().get(key, paramCode);
        if (object != null) {
            systemParameter.setDefaultValue(object);
            return systemParameter;
        }
        //set类型 不重复
        String keySet = String.format(SystemParameterConstants.Redis.KEY, tenantId, SystemParameterConstants.ParamType.SET);
        object = redisCacheClient.<String,String>opsForHash().get(keySet, paramCode);
        if (null != object) {
            systemParameter.setSetSystemParamValue(new HashSet(JSONArray.parseArray(object, SystemParamValue.class)));
            return systemParameter;
        }
        //map类型
        systemParameter.setSetSystemParamValue(listSystemParamValue(tenantId,paramCode));
        return systemParameter;

    }

    @Override
    public List<SystemParameter> listSystemParameters(List<String> paramCodeList, Long tenantId) {
        List<SystemParameter> doList = new ArrayList<>();
        String key = String.format(SystemParameterConstants.Redis.KEY, tenantId, SystemParameterConstants.ParamType.KV);
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
        String keySet = String.format(SystemParameterConstants.Redis.KEY, tenantId, SystemParameterConstants.ParamType.SET);
        List<String> listSet = redisCacheClient.<String,String>opsForHash().multiGet(keySet,paramCodeList);
        if (CollectionUtils.isNotEmpty(listSet)) {
            for (int i = 0; i < paramCodeList.size(); i++) {
                SystemParameter systemParameter = new SystemParameter();
                systemParameter.setParamCode(paramCodeList.get(i));
                systemParameter.setSetSystemParamValue(new HashSet(JSONArray.parseArray(listSet.get(i), SystemParamValue.class)));
                doList.add(systemParameter);
            }
            return doList;
        }
        for (String paramCode : paramCodeList) {
            SystemParameter systemParameter = new SystemParameter();
            systemParameter.setParamCode(paramCode);
            systemParameter.setSetSystemParamValue(listSystemParamValue(tenantId,paramCode));
            doList.add(systemParameter);
        }
        return doList;
    }
    /**
     * 获取map类型的参数值
     * @param  tenantId 租户ID
     * @param  paramCode 参数编码
     * @return  set
     */
    private Set<SystemParamValue> listSystemParamValue(Long tenantId,String paramCode) {
        String mapKey = String.format(SystemParameterConstants.Redis.MAP_KEY, tenantId,SystemParameterConstants.ParamType.MAP,paramCode);
        Map<String,String> valueMap = redisCacheClient.<String,String>opsForHash().entries(mapKey);
        Set<SystemParamValue> setList = new HashSet<>(4);
        if (valueMap.isEmpty()) {
            return setList;
        }
        valueMap.forEach((k,v)->{
            SystemParamValue value = new SystemParamValue();
            value.setParamKey(k);
            value.setParamValue(v);
            setList.add(value);
        });
        return setList;
    }
}
