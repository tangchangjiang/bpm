package org.o2.metadata.infra.redis.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.o2.core.helper.JsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.infra.constants.SystemParameterConstants;
import org.o2.metadata.infra.entity.SystemParamValue;
import org.o2.metadata.infra.entity.SystemParameter;
import org.o2.metadata.infra.redis.SystemParameterRedis;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 *
 * 系统参数
 *
 * @author yipeng.zhu@hand-china.com 2021-07-11
 **/
@Component
@EnableAspectJAutoProxy( proxyTargetClass = true , exposeProxy = true )
public class SystemParameterRedisImpl implements SystemParameterRedis {
    private final RedisCacheClient redisCacheClient;

    public SystemParameterRedisImpl(RedisCacheClient redisCacheClient) {
        this.redisCacheClient = redisCacheClient;
    }

    @Override
    public SystemParameter getSystemParameter(String paramCode, Long tenantId) {
        SystemParameterRedisImpl currentProxy = (SystemParameterRedisImpl) AopContext.currentProxy();
        return currentProxy.querySystemParameter(paramCode,tenantId);

    }
    @Override
    public List<SystemParameter> listSystemParameters(List<String> paramCodeList, Long tenantId) {
        SystemParameterRedisImpl currentProxy = (SystemParameterRedisImpl) AopContext.currentProxy();
        List<SystemParameter> doList = new ArrayList<>();
        if (CollectionUtils.isEmpty(paramCodeList)) {
            return doList;
        }
        for (String str : paramCodeList) {
            SystemParameter systemParameter = currentProxy.querySystemParameter(str,tenantId);
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
        String mapKey = String.format(SystemParameterConstants.Redis.MAP_KEY, tenantId,paramCode);
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

    @Cacheable(value = "O2MD_METADATA", key = "'systemParameter'+'_'+#tenantId+'_'+#paramCode")
    public SystemParameter querySystemParameter(String paramCode, Long tenantId){
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
            Set<SystemParamValue>  set = new HashSet<>(16);
            List<SystemParamValue>  list = JsonHelper.stringToArray(object, SystemParamValue.class);
            set.addAll(list);
            systemParameter.setSetSystemParamValue(set);
            return systemParameter;
        }
        //map类型
        systemParameter.setSetSystemParamValue(listSystemParamValue(tenantId,paramCode));
        return systemParameter;
    }
}
