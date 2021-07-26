package org.o2.metadata.console.infra.redis.impl;

import com.alibaba.fastjson.JSONArray;
import io.choerodon.core.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.inventory.management.client.O2InventoryClient;
import org.o2.metadata.console.api.vo.SystemParamValueVO;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.constant.SystemParameterConstants;
import org.o2.metadata.console.infra.entity.SystemParamValue;
import org.o2.metadata.console.infra.entity.SystemParameter;
import org.o2.metadata.console.infra.mapper.SystemParamValueMapper;
import org.o2.metadata.console.infra.redis.SystemParameterRedis;
import org.o2.metadata.console.infra.repository.SystemParameterRepository;
import org.o2.metadata.console.infra.util.SystemParameterRedisUtil;
import org.o2.metadata.domain.systemparameter.domain.SystemParamValueDO;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * 系统参数
 *
 * @author yipeng.zhu@hand-china.com 2021-07-11
 **/
@Component
@Slf4j
public class SystemParameterRedisImpl implements SystemParameterRedis {
    private final RedisCacheClient redisCacheClient;
    private SystemParameterRepository systemParameterRepository;
    private SystemParamValueMapper systemParamValueMapper;
    private O2InventoryClient o2InventoryClient;


    public SystemParameterRedisImpl(RedisCacheClient redisCacheClient, SystemParameterRepository systemParameterRepository, SystemParamValueMapper systemParamValueMapper, O2InventoryClient o2InventoryClient) {
        this.redisCacheClient = redisCacheClient;
        this.systemParameterRepository = systemParameterRepository;
        this.systemParamValueMapper = systemParamValueMapper;
        this.o2InventoryClient = o2InventoryClient;
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
    @Override
    public void synToRedis(final List<SystemParameter> ts,
                           final Long tenantId) {
        if (CollectionUtils.isEmpty(ts)) {
            return;
        }
        try {
            // 分组  0失效delete，1有效save
            Map<Integer, List<SystemParameter>> groupByMap = ts.stream().collect(Collectors.groupingBy(SystemParameter::getActiveFlag));
            Map<String, Map<String, Object>> filedMaps = new HashMap<>(8);
            for (Map.Entry<Integer, List<SystemParameter>> tEntry : groupByMap.entrySet()) {
                // 新增/更新
                if (tEntry.getKey() == 1) {
                    // 获取hashMap
                    Map<String, Object> kvHashMap = new HashMap<>(4);
                    Map<String, Object> setHashMap = new HashMap<>(4);
                    // 获取hashKey
                    final String kvHashKey = String.format(SystemParameterConstants.Redis.KEY, tenantId, SystemParameterConstants.ParamType.KV);
                    final String setHashKey = String.format(SystemParameterConstants.Redis.KEY, tenantId, SystemParameterConstants.ParamType.SET);
                    for (SystemParameter t : tEntry.getValue()) {
                        final String paramTypeCode = t.getParamTypeCode();
                        final String paramCode = t.getParamCode();
                        if (SystemParameterConstants.ParamType.KV.equalsIgnoreCase(paramTypeCode)) {
                            List<SystemParameter> list = systemParameterRepository.selectByCondition(Condition.builder(SystemParameter.class).andWhere(Sqls.custom()
                                    .andEqualTo(SystemParameter.FIELD_PARAM_CODE, paramCode)
                                    .andEqualTo(SystemParameter.FIELD_TENANT_ID, tenantId)).build());
                            if (CollectionUtils.isNotEmpty(list)) {
                                kvHashMap.put(paramCode, list.get(0).getDefaultValue());
                            }
                        } else if (SystemParameterConstants.ParamType.SET.equalsIgnoreCase(paramTypeCode)) {
                            List<SystemParamValue> voList = systemParamValueMapper.getSysSetWithParams(paramCode, tenantId);
                            if (CollectionUtils.isNotEmpty(voList)) {
                                setHashMap.put(paramCode, JSONArray.toJSONString(voList));
                            }
                        }
                    }
                    filedMaps.put(kvHashKey, kvHashMap);
                    filedMaps.put(setHashKey, setHashMap);
                }
                // 删除
                else {
                    for (SystemParameter t : tEntry.getValue()) {
                        // 获取hashKey
                        final String hashKey = String.format(SystemParameterConstants.Redis.KEY, tenantId, t.getParamTypeCode());
                        // 获取hashMap
                        filedMaps.put(hashKey, null);
                    }
                }
            }
            SystemParameterRedisUtil.executeScript(filedMaps, Collections.emptyList(), MetadataConstants.LuaCode.BATCH_SAVE_REDIS_HASH_VALUE_LUA, redisCacheClient);
        } catch (Exception e) {
            throw new CommonException(SystemParameterConstants.Message.SYSTEM_PARAMETER_SUCCESS_NUM);
        }
    }
    @Override
    public void updateToRedis(SystemParameter systemParameter, Long tenantId) {
        final String paramTypeCode = systemParameter.getParamTypeCode();
        final String paramCode = systemParameter.getParamCode();
        //参数redis更新
        if (SystemParameterConstants.ParamType.KV.equalsIgnoreCase(paramTypeCode)) {
            final String kvHashKey = String.format(SystemParameterConstants.Redis.KEY, tenantId, SystemParameterConstants.ParamType.KV);
            redisCacheClient.opsForHash().put(kvHashKey, paramCode, systemParameter.getDefaultValue());
        }
        //参数值redis更新
        if (SystemParameterConstants.ParamType.SET.equalsIgnoreCase(paramTypeCode)) {
            final String setHashKey = String.format(SystemParameterConstants.Redis.KEY, tenantId, SystemParameterConstants.ParamType.SET);
            List<SystemParamValue> voList = systemParamValueMapper.getSysSetWithParams(systemParameter.getParamCode(), tenantId);
            if (CollectionUtils.isNotEmpty(voList)) {
                redisCacheClient.opsForHash().put(setHashKey, systemParameter.getParamCode(), JSONArray.toJSONString(voList));
                return;
            }
            redisCacheClient.opsForHash().delete(setHashKey, systemParameter.getParamCode());
        }

        if (SystemParameterConstants.ParamType.MAP.equals(paramCode)) {
            final String hashMapKey = String.format(SystemParameterConstants.Redis.MAP_KEY, tenantId, SystemParameterConstants.ParamType.MAP);
            List<SystemParamValue> list = systemParamValueMapper.getSysSetWithParams(systemParameter.getParamCode(), tenantId);
            Map<String,String> map = new HashMap<>(4);
            for (SystemParamValue value : list) {
                map.put(value.getParamKey(),value.getParamValue());
            }
            redisCacheClient.opsForHash().putAll(hashMapKey, map);
        }

    }


    @Override
    public void extraOperate(String paramCode, Long tenantId) {
        if (SystemParameterConstants.Parameter.DEFAULT_WH_UPLOAD_RATIO.equals(paramCode) || SystemParameterConstants.Parameter.DEFAULT_WH_SAFETY_STOCK.equals(paramCode)) {
            o2InventoryClient.triggerAllWhStockCal(tenantId);
        }
        if (SystemParameterConstants.Parameter.DEFAULT_SHOP_UPLOAD_RATIO.equals(paramCode) || SystemParameterConstants.Parameter.DEFAULT_SHOP_SAFETY_STOCK.equals(paramCode)) {
            o2InventoryClient.triggerAllShopStockCal(tenantId);
        }
    }

}
