package org.o2.metadata.console.app.service.impl;

import com.alibaba.fastjson.JSONArray;

import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.inventory.management.client.O2InventoryClient;
import org.o2.metadata.console.app.service.SysParamService;
import org.o2.metadata.console.infra.constant.O2MdConsoleConstants;
import org.o2.metadata.console.infra.convertor.SysParameterConvertor;
import org.o2.metadata.console.infra.util.MetadataRedisUtil;
import org.o2.metadata.console.infra.entity.SystemParameter;
import org.o2.metadata.console.infra.repository.SystemParameterRepository;
import org.o2.metadata.console.api.vo.SystemParameterVO;
import org.o2.metadata.console.api.vo.SystemParamValueVO;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.mapper.SystemParamValueMapper;
import org.o2.metadata.core.systemparameter.domain.SystemParameterDO;
import org.o2.metadata.core.systemparameter.service.SystemParameterDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.choerodon.core.exception.CommonException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yong.nie@hand-china.com
 * @date 2020/6/30 21:26
 **/
@Slf4j
@Service
public class SysParamServiceImpl implements SysParamService {

    private static String DEFAULT_WH_UPLOAD_RATIO = "DEFAULT_WH_UPLOAD_RATIO";
    private static String DEFAULT_WH_SAFETY_STOCK = "DEFAULT_WH_SAFETY_STOCK";
    private static String DEFAULT_SHOP_UPLOAD_RATIO = "DEFAULT_SHOP_UPLOAD_RATIO";
    private static String DEFAULT_SHOP_SAFETY_STOCK = "DEFAULT_SHOP_SAFETY_STOCK";

    private SystemParameterRepository systemParameterRepository;

    private SystemParamValueMapper systemParamValueMapper;

    private O2InventoryClient o2InventoryClient;

    private RedisCacheClient redisCacheClient;
    private SystemParameterDomainService systemParameterDomainService;

    @Autowired
    public SysParamServiceImpl(SystemParameterRepository systemParameterRepository,
                               SystemParamValueMapper systemParamValueMapper,
                               O2InventoryClient o2InventoryClient,
                               RedisCacheClient redisCacheClient,
                               SystemParameterDomainService systemParameterDomainService) {
        this.systemParameterRepository = systemParameterRepository;
        this.systemParamValueMapper = systemParamValueMapper;
        this.o2InventoryClient = o2InventoryClient;
        this.redisCacheClient = redisCacheClient;
        this.systemParameterDomainService = systemParameterDomainService;
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
                    final String kvHashKey = String.format(MetadataConstants.SystemParameter.KEY, tenantId, MetadataConstants.ParamType.KV);
                    final String setHashKey = String.format(MetadataConstants.SystemParameter.KEY, tenantId, MetadataConstants.ParamType.SET);
                    for (SystemParameter t : tEntry.getValue()) {
                        final String paramTypeCode = t.getParamTypeCode();
                        final String paramCode = t.getParamCode();
                        if (MetadataConstants.ParamType.KV.equalsIgnoreCase(paramTypeCode)) {
                            List<SystemParameter> list = systemParameterRepository.selectByCondition(Condition.builder(SystemParameter.class).andWhere(Sqls.custom()
                                    .andEqualTo(SystemParameter.FIELD_PARAM_CODE, paramCode)
                                    .andEqualTo(SystemParameter.FIELD_TENANT_ID, tenantId)).build());
                            if (CollectionUtils.isNotEmpty(list)) {
                                kvHashMap.put(paramCode, list.get(0).getDefaultValue());
                            }
                        } else if (MetadataConstants.ParamType.SET.equalsIgnoreCase(paramTypeCode)) {
                            List<SystemParamValueVO> voList = systemParamValueMapper.getSysSetWithParams(paramCode, tenantId);
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
                        final String hashKey = String.format(MetadataConstants.SystemParameter.KEY, tenantId, t.getParamTypeCode());
                        // 获取hashMap
                        filedMaps.put(hashKey, null);
                    }
                }
            }
            MetadataRedisUtil.executeScript(filedMaps, Collections.emptyList(), O2MdConsoleConstants.LuaCode.BATCH_SAVE_REDIS_HASH_VALUE_LUA, redisCacheClient);
        } catch (Exception e) {
            throw new CommonException(MetadataConstants.Message.SYSTEM_PARAMETER_SUCCESS_NUM);
        }
    }

    @Override
    public SystemParameterVO getSystemParameter(String paramCode, Long tenantId) {
        SystemParameterDO systemParameterDO = systemParameterDomainService.getSystemParameter(paramCode,tenantId);
        return SysParameterConvertor.doToVoObject(systemParameterDO);
    }

    @Override
    public List<SystemParameterVO> listSystemParameters(List<String> paramCodes, Long tenantId) {
        return SysParameterConvertor.doToVoListObjects(systemParameterDomainService.listSystemParameters(paramCodes,tenantId));
    }

    @Override
    public void updateToRedis(SystemParameter systemParameter, Long tenantId) {
        // 获取hashKey
        final String kvHashKey = String.format(MetadataConstants.SystemParameter.KEY, tenantId, MetadataConstants.ParamType.KV);
        final String setHashKey = String.format(MetadataConstants.SystemParameter.KEY, tenantId, MetadataConstants.ParamType.SET);
        final String paramTypeCode = systemParameter.getParamTypeCode();
        final String paramCode = systemParameter.getParamCode();
        if (MetadataConstants.ParamType.KV.equalsIgnoreCase(paramTypeCode)) {
            List<SystemParameter> list = systemParameterRepository.selectByCondition(Condition.builder(SystemParameter.class).andWhere(Sqls.custom()
                    .andEqualTo(SystemParameter.FIELD_PARAM_CODE, paramCode)
                    .andEqualTo(SystemParameter.FIELD_TENANT_ID, tenantId)).build());
            if (CollectionUtils.isNotEmpty(list)) {
                String defaultValue = list.get(0).getDefaultValue();
                log.info("sysparam key({}), paramCode({}), value({})", kvHashKey, paramCode, defaultValue);
                redisCacheClient.opsForHash().put(kvHashKey, paramCode, null == defaultValue ? "" : defaultValue);
            }
        } else if (MetadataConstants.ParamType.SET.equalsIgnoreCase(paramTypeCode)) {
            List<SystemParamValueVO> voList = systemParamValueMapper.getSysSetWithParams(paramCode, tenantId);
            if (CollectionUtils.isNotEmpty(voList)) {
                redisCacheClient.opsForHash().put(setHashKey, paramCode, JSONArray.toJSONString(voList));
            }
        }
    }


    @Override
    public void updateToRedis(Long paramId, Long tenantId) {
        SystemParameter queryParam = new SystemParameter();
        queryParam.setParamId(paramId);
        queryParam.setTenantId(tenantId);

        SystemParameter systemParameter = systemParameterRepository.selectOne(queryParam);
        if (null != systemParameter) {
            this.updateToRedis(systemParameter, systemParameter.getTenantId());
        }
    }

    @Override
    public void extraOperate(String paramCode, Long tenantId) {
        if (DEFAULT_WH_UPLOAD_RATIO.equals(paramCode) || DEFAULT_WH_SAFETY_STOCK.equals(paramCode)) {
            o2InventoryClient.triggerAllWhStockCal(tenantId);
        }
        if (DEFAULT_SHOP_UPLOAD_RATIO.equals(paramCode) || DEFAULT_SHOP_SAFETY_STOCK.equals(paramCode)) {
            o2InventoryClient.triggerAllShopStockCal(tenantId);
        }
    }
}
