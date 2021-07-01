package org.o2.metadata.console.app.service.impl;

import com.alibaba.fastjson.JSONArray;

import org.apache.commons.collections4.CollectionUtils;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.console.app.service.SystemParamValueService;
import org.o2.metadata.console.domain.entity.SystemParameter;
import org.o2.metadata.console.domain.repository.SystemParamValueRepository;
import org.o2.metadata.console.domain.repository.SystemParameterRepository;
import org.o2.metadata.console.api.vo.SystemParamValueVO;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 系统参数值应用服务默认实现
 *
 * @author hongyun.wang01@hand-china.com 2020-02-13
 */
@Service
public class SystemParamValueServiceImpl implements SystemParamValueService {
    @Autowired
    private SystemParamValueRepository systemParamValueRepository;
    @Autowired
    private SystemParameterRepository systemParameterRepository;
    @Autowired
    private RedisCacheClient redisCacheClient;

    @Override
    public String getSysValueByParam(String paramCode, Long tenantId) {
        if (MetadataConstants.ParamType.KV.equalsIgnoreCase(getParamTypeByCode(paramCode, tenantId))) {
            return systemParamValueRepository.getSysValueByParam(paramCode, tenantId);
        }
        return null;
    }

    @Override
    public List<String> getSysListByParam(String paramCode, Long tenantId) {
        if (MetadataConstants.ParamType.LIST.equalsIgnoreCase(getParamTypeByCode(paramCode, tenantId))) {
            return systemParamValueRepository.getSysListByParam(paramCode, tenantId);
        }
        return new ArrayList<>();

    }

    @Override
    public Set<String> getSysSetByParam(String paramCode, Long tenantId) {
        if (MetadataConstants.ParamType.SET.equalsIgnoreCase(getParamTypeByCode(paramCode, tenantId))) {
            return systemParamValueRepository.getSysSetByParam(paramCode, tenantId);
        }
        return new HashSet<>();

    }

    /**
     * 从Redis获取系统参数
     *
     * @param paramCode 参数code
     * @param tenantId  租户id
     * @return StringList<String>
     */
    @Override
    public String getSysValueFromRedis(String paramCode, Long tenantId) {
        String valueKv = getSysValueKvFromRedis(paramCode, tenantId);
        if (null != valueKv) {
            return valueKv;
        }
        String key = String.format(MetadataConstants.SystemParameter.KEY, tenantId, MetadataConstants.ParamType.SET);
        Object valueObj = redisCacheClient.opsForHash().get(key, paramCode);
        return null == valueObj ? null : String.valueOf(valueObj);
    }

    @Override
    public String getSysValueKvFromRedis(String paramCode, Long tenantId) {
        String key = String.format(MetadataConstants.SystemParameter.KEY, tenantId, MetadataConstants.ParamType.KV);
        Object valueObj = redisCacheClient.opsForHash().get(key, paramCode);
        return null == valueObj ? null : String.valueOf(valueObj);
    }

    @Override
    public List<SystemParamValueVO> getSysValueSetFromRedis(String paramCode, Long tenantId) {
        String key = String.format(MetadataConstants.SystemParameter.KEY, tenantId, MetadataConstants.ParamType.SET);
        Object valueObj = redisCacheClient.opsForHash().get(key, paramCode);
        return null == valueObj ? null : JSONArray.parseArray(String.valueOf(valueObj), SystemParamValueVO.class);
    }

    /**
     * 获取系统参数类型
     */
    private String getParamTypeByCode(String paramCode, Long tenantId) {
        SystemParameter condition = new SystemParameter();
        condition.setParamCode(paramCode);
        condition.setTenantId(tenantId);
        List<SystemParameter> result = systemParameterRepository.select(condition);
        if (CollectionUtils.isNotEmpty(result)) {
            return result.get(0).getParamTypeCode();
        }
        return null;
    }
}
