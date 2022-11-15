package org.o2.metadata.console.infra.redis.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.core.base.BaseConstants;
import org.o2.core.helper.JsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.inventory.management.client.O2InventoryClient;
import org.o2.metadata.console.infra.constant.SystemParameterConstants;
import org.o2.metadata.console.infra.entity.SystemParamValue;
import org.o2.metadata.console.infra.entity.SystemParameter;
import org.o2.metadata.console.infra.mapper.SystemParamValueMapper;
import org.o2.metadata.console.infra.redis.SystemParameterRedis;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private SystemParamValueMapper systemParamValueMapper;
    private O2InventoryClient o2InventoryClient;

    public SystemParameterRedisImpl(RedisCacheClient redisCacheClient,
                                    SystemParamValueMapper systemParamValueMapper,
                                    O2InventoryClient o2InventoryClient) {
        this.redisCacheClient = redisCacheClient;
        this.systemParamValueMapper = systemParamValueMapper;
        this.o2InventoryClient = o2InventoryClient;
    }

    @Override
    public SystemParameter getSystemParameter(String paramCode, Long tenantId) {

        SystemParameter systemParameter = new SystemParameter();
        systemParameter.setParamCode(paramCode);
        //hash类型
        String key = String.format(SystemParameterConstants.Redis.KEY, tenantId, SystemParameterConstants.ParamType.KV);
        String object = redisCacheClient.<String, String>opsForHash().get(key, paramCode);
        if (object != null) {
            systemParameter.setDefaultValue(object);
            return systemParameter;
        }
        //set类型 不重复
        String keySet = String.format(SystemParameterConstants.Redis.KEY, tenantId, SystemParameterConstants.ParamType.SET);
        object = redisCacheClient.<String, String>opsForHash().get(keySet, paramCode);
        if (null != object) {
            List<SystemParamValue> list = JsonHelper.stringToArray(object, SystemParamValue.class);
            Set<SystemParamValue> set = new HashSet<>(16);
            set.addAll(list);
            systemParameter.setSetSystemParamValue(set);
            return systemParameter;
        }
        //map类型
        systemParameter.setSetSystemParamValue(listSystemParamValue(tenantId, paramCode));
        return systemParameter;
    }

    @Override
    public List<SystemParameter> listSystemParameters(List<String> paramCodeList, Long tenantId) {
        List<SystemParameter> doList = new ArrayList<>();
        String key = String.format(SystemParameterConstants.Redis.KEY, tenantId, SystemParameterConstants.ParamType.KV);
        List<String> list = redisCacheClient.<String, String>opsForHash().multiGet(key, paramCodeList);
        if (CollectionUtils.isNotEmpty(list) && null != list.get(0)) {
            for (int i = 0; i < paramCodeList.size(); i++) {
                SystemParameter systemParameter = new SystemParameter();
                systemParameter.setParamCode(paramCodeList.get(i));
                systemParameter.setDefaultValue(list.get(i));
                doList.add(systemParameter);
            }
            return doList;
        }
        String keySet = String.format(SystemParameterConstants.Redis.KEY, tenantId, SystemParameterConstants.ParamType.SET);
        List<String> listSet = redisCacheClient.<String, String>opsForHash().multiGet(keySet, paramCodeList);
        if (CollectionUtils.isNotEmpty(listSet) && null != listSet.get(0)) {
            for (int i = 0; i < paramCodeList.size(); i++) {
                SystemParameter systemParameter = new SystemParameter();
                systemParameter.setParamCode(paramCodeList.get(i));
                List<SystemParamValue> values = JsonHelper.stringToArray(listSet.get(i), SystemParamValue.class);
                Set<SystemParamValue> set = new HashSet<>(16);
                set.addAll(values);
                systemParameter.setSetSystemParamValue(set);
                doList.add(systemParameter);
            }
            return doList;
        }
        for (String paramCode : paramCodeList) {
            SystemParameter systemParameter = new SystemParameter();
            systemParameter.setParamCode(paramCode);
            systemParameter.setSetSystemParamValue(listSystemParamValue(tenantId, paramCode));
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
    private Set<SystemParamValue> listSystemParamValue(Long tenantId, String paramCode) {
        String mapKey = String.format(SystemParameterConstants.Redis.MAP_KEY, tenantId, paramCode);
        Map<String, String> valueMap = redisCacheClient.<String, String>opsForHash().entries(mapKey);
        Set<SystemParamValue> setList = new HashSet<>(4);
        if (valueMap.isEmpty()) {
            return setList;
        }
        valueMap.forEach((k, v) -> {
            SystemParamValue value = new SystemParamValue();
            value.setParamKey(k);
            value.setParamValue(v);
            setList.add(value);
        });
        return setList;
    }

    @Override
    public void synToRedis(final List<SystemParameter> list, final Long tenantId) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Map<String, String> kvHashMap = new HashMap<>(4);
        Map<String, String> setHashMap = new HashMap<>(4);
        Map<String, Map<String, String>> mapHashMap = new HashMap<>(4);
        // 构造redis 数据
        for (SystemParameter systemParameter : list) {
            if (SystemParameterConstants.ParamType.KV.equals(systemParameter.getParamTypeCode())) {
                kvHashMap.put(systemParameter.getParamCode(), systemParameter.getDefaultValue());
            }

            if (SystemParameterConstants.ParamType.SET.equals(systemParameter.getParamTypeCode())) {
                setHashMap.put(systemParameter.getParamCode(), JsonHelper.objectToString(systemParameter.getSetSystemParamValue()));
            }

            if (SystemParameterConstants.ParamType.MAP.equals(systemParameter.getParamTypeCode())) {
                Set<SystemParamValue> systemParamValue = systemParameter.getSetSystemParamValue();
                Map<String, String> valueMap = new HashMap<>(4);
                for (SystemParamValue value : systemParamValue) {
                    valueMap.put(value.getParamKey(), value.getParamValue());
                }
                String mapHashKey = String.format(SystemParameterConstants.Redis.MAP_KEY, tenantId, systemParameter.getParamCode());
                mapHashMap.put(mapHashKey, valueMap);
            }
        }
        // redis key
        List<String> keyList = new ArrayList<>(3);
        String kvHashKey = String.format(SystemParameterConstants.Redis.KEY, tenantId, SystemParameterConstants.ParamType.KV);
        keyList.add(kvHashKey);
        String setHashKey = String.format(SystemParameterConstants.Redis.KEY, tenantId, SystemParameterConstants.ParamType.SET);
        keyList.add(setHashKey);
        // 初始化数据
        final DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setScriptSource(SystemParameterConstants.INIT_DATA_REDIS_HASH_VALUE_LUA);
        redisCacheClient.execute(defaultRedisScript, keyList, JsonHelper.mapToString(kvHashMap), JsonHelper.mapToString(setHashMap), JsonHelper.mapToString(mapHashMap));

    }

    @Override
    public void updateToRedis(SystemParameter systemParameter, Long tenantId) {
        String paramTypeCode = systemParameter.getParamTypeCode();
        //kv 类型 redis更新
        if (SystemParameterConstants.ParamType.KV.equalsIgnoreCase(paramTypeCode)) {
            updateKv(systemParameter, tenantId);
        }

        //参数值redis更新
        if (SystemParameterConstants.ParamType.SET.equalsIgnoreCase(paramTypeCode)) {
            updateSet(systemParameter, tenantId);
        }

        if (SystemParameterConstants.ParamType.MAP.equals(paramTypeCode)) {
            updateMap(systemParameter, tenantId);
        }
    }

    /**
     * 更新kv类型系统参数
     * @param systemParameter 系统参数
     * @param tenantId 租户ID
     */
    private void  updateKv(SystemParameter systemParameter, Long tenantId) {
        String paramCode = systemParameter.getParamCode();
        Integer activeFlag = systemParameter.getActiveFlag();
        final String kvHashKey = String.format(SystemParameterConstants.Redis.KEY, tenantId, SystemParameterConstants.ParamType.KV);
        // 删除
        if (BaseConstants.Flag.NO.equals(activeFlag)) {
            redisCacheClient.opsForHash().delete(kvHashKey, paramCode);
            return;
        }
        redisCacheClient.opsForHash().put(kvHashKey, paramCode, systemParameter.getDefaultValue());
    }

    /**
     * 更新set类型系统参数
     * @param systemParameter 系统参数
     * @param tenantId 租户ID
     */
    private void updateSet(SystemParameter systemParameter, Long tenantId) {
        Integer activeFlag = systemParameter.getActiveFlag();
        final String setHashKey = String.format(SystemParameterConstants.Redis.KEY, tenantId, SystemParameterConstants.ParamType.SET);
        // 删除
        if (BaseConstants.Flag.NO.equals(activeFlag)) {
            redisCacheClient.opsForHash().delete(setHashKey, systemParameter.getParamCode());
            return;
        }
        List<SystemParamValue> voList = systemParamValueMapper.getSysSetWithParams(systemParameter.getParamCode(), tenantId);
        if (CollectionUtils.isNotEmpty(voList)) {
            for (SystemParamValue value : voList) {
                value.setFlex(null);
                value.set_token(null);
            }
            redisCacheClient.opsForHash().put(setHashKey, systemParameter.getParamCode(), JsonHelper.objectToString(voList));
        }
    }

    /**
     * 更新map类型系统参数
     * @param systemParameter 系统参数
     * @param tenantId 租户ID
     */
    private void updateMap(SystemParameter systemParameter, Long tenantId) {
        String paramCode = systemParameter.getParamCode();
        Integer activeFlag = systemParameter.getActiveFlag();
        final String hashMapKey = String.format(SystemParameterConstants.Redis.MAP_KEY, tenantId, paramCode);
        // 删除
        if (BaseConstants.Flag.NO.equals(activeFlag)) {
            redisCacheClient.delete(hashMapKey);
            return;
        }
        List<SystemParamValue> list = systemParamValueMapper.getSysSetWithParams(systemParameter.getParamCode(), tenantId);
        Map<String, String> map = new HashMap<>(4);
        if (CollectionUtils.isNotEmpty(list)) {
            for (SystemParamValue value : list) {
                map.put(value.getParamKey(), value.getParamValue());
            }
            redisCacheClient.opsForHash().putAll(hashMapKey, map);
        }
    }

    @Override
    public void extraOperate(String paramCode, Long tenantId) {
        if (SystemParameterConstants.Parameter.DEFAULT_WH_UPLOAD_RATIO.equals(paramCode) || SystemParameterConstants.Parameter.DEFAULT_WH_SAFETY_STOCK.equals(paramCode)) {
           try {
               o2InventoryClient.triggerAllWhStockCal(tenantId);
           } catch (Exception e) {
               log.error("o2InventoryClient: request triggerAllWhStockCal fail {}", e.getMessage());
           }
        }
        if (SystemParameterConstants.Parameter.DEFAULT_SHOP_UPLOAD_RATIO.equals(paramCode) || SystemParameterConstants.Parameter.DEFAULT_SHOP_SAFETY_STOCK.equals(paramCode)) {
           try {
               o2InventoryClient.triggerAllShopStockCal(tenantId);
           } catch (Exception e) {
               log.error("o2InventoryClient: request triggerAllShopStockCal fail {}", e.getMessage());
           }
        }
    }

    @Override
    public void deleteSystemParamValue(SystemParameter systemParameter, SystemParamValue systemParamValue) {
        String paramTypeCode = systemParameter.getParamTypeCode();
        if (SystemParameterConstants.ParamType.MAP.equals(paramTypeCode)) {
            String hashMapKey = String.format(SystemParameterConstants.Redis.MAP_KEY, systemParameter.getTenantId(), systemParameter.getParamCode());
            redisCacheClient.opsForHash().delete(hashMapKey, systemParamValue.getParamKey());
        }
        //参数值redis更新
        if (SystemParameterConstants.ParamType.SET.equalsIgnoreCase(paramTypeCode)) {
            updateSet(systemParameter, systemParameter.getTenantId());
        }
    }

}
