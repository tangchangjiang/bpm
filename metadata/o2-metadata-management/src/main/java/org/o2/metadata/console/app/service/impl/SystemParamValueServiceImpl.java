package org.o2.metadata.console.app.service.impl;

import io.choerodon.core.exception.CommonException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.o2.metadata.console.app.service.SystemParamValueService;
import org.o2.metadata.console.infra.constant.SystemParameterConstants;
import org.o2.metadata.console.infra.entity.SystemParamValue;
import org.o2.metadata.console.infra.entity.SystemParameter;
import org.o2.metadata.console.infra.redis.SystemParameterRedis;
import org.o2.metadata.console.infra.repository.SystemParamValueRepository;
import org.o2.metadata.console.infra.repository.SystemParameterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统参数值应用服务默认实现
 *
 * @author hongyun.wang01@hand-china.com 2020-02-13
 */
@Service
public class SystemParamValueServiceImpl implements SystemParamValueService {
    private final SystemParamValueRepository systemParamValueRepository;
    private final SystemParameterRepository systemParameterRepository;
    private final SystemParameterRedis systemParameterRedis;

    public SystemParamValueServiceImpl(SystemParamValueRepository systemParamValueRepository,
                                       SystemParameterRepository systemParameterRepository,
                                       SystemParameterRedis systemParameterRedis) {
        this.systemParamValueRepository = systemParamValueRepository;
        this.systemParameterRepository = systemParameterRepository;
        this.systemParameterRedis = systemParameterRedis;
    }

    @Override
    public String getSysValueByParam(String paramCode, Long tenantId) {
        tenantId = judgeAndSetSysParamSource(paramCode, tenantId);
        if (SystemParameterConstants.ParamType.KV.equalsIgnoreCase(getParamTypeByCode(paramCode, tenantId))) {
            return systemParamValueRepository.getSysValueByParam(paramCode, tenantId);
        }
        return null;
    }

    @Override
    public List<String> getSysListByParam(String paramCode, Long tenantId) {
        tenantId = judgeAndSetSysParamSource(paramCode, tenantId);
        if (SystemParameterConstants.ParamType.LIST.equalsIgnoreCase(getParamTypeByCode(paramCode, tenantId))) {
            return systemParamValueRepository.getSysListByParam(paramCode, tenantId);
        }
        return new ArrayList<>();

    }

    @Override
    public Map<String, String> getSysMapByParam(String paramCode, Long tenantId) {
        tenantId = judgeAndSetSysParamSource(paramCode, tenantId);
        if (SystemParameterConstants.ParamType.MAP.equalsIgnoreCase(getParamTypeByCode(paramCode, tenantId))) {
            List<SystemParamValue> systemParamValues  = systemParamValueRepository.getSysMapByParam(paramCode, tenantId);
            Map<String, String> result = new HashMap<>();
            if (CollectionUtils.isNotEmpty(systemParamValues)) {
                for (SystemParamValue value : systemParamValues) {
                    result.put(value.getParamKey(), value.getParamValue());
                }
            }
            return result;
        }
        return new HashMap<>();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSystemParamValue(SystemParamValue systemParamValue) {
        SystemParameter systemParameter = getSystemParameter(systemParamValue);
        systemParamValueRepository.insertSelective(systemParamValue);
        systemParameterRedis.updateToRedis(systemParameter, systemParameter.getTenantId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSystemParamValue(SystemParamValue systemParamValue) {
        SystemParameter systemParameter = getSystemParameter(systemParamValue);
        systemParamValueRepository.updateByPrimaryKey(systemParamValue);
        systemParameterRedis.updateToRedis(systemParameter, systemParameter.getTenantId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeSystemParamValue(SystemParamValue systemParamValue) {
        SystemParameter systemParameter = getSystemParameter(systemParamValue);
        systemParamValueRepository.deleteByPrimaryKey(systemParamValue);
        systemParameterRedis.deleteSystemParamValue(systemParameter, systemParamValue);
    }

    @Override
    public void systemParamValueValidate(SystemParamValue systemParamValue) {
        SystemParameter systemParameter = getSystemParameter(systemParamValue);
        if (SystemParameterConstants.ParamType.MAP.equals(systemParameter.getParamTypeCode())) {
           String key =  systemParamValue.getParamKey();
           if (StringUtils.isEmpty(key)) {
               throw new CommonException(SystemParameterConstants.ErrorCode.BASIC_DATA_MAP_KEY_IS_NULL);
           }
        }

    }

    /**
     * 获取系统参数
     * @param  systemParamValue 系统参数值
     * @return 系统参数实体类
     */
    private SystemParameter getSystemParameter(SystemParamValue systemParamValue) {
        Long tenantId = systemParamValue.getTenantId();
        SystemParameter queryParam = new SystemParameter();
        queryParam.setParamId(systemParamValue.getParamId());
        queryParam.setTenantId(tenantId);
        return systemParameterRepository.findOne(queryParam);
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

    /**
     * 判断系统参数在当前租户是否存在，即判断系统参数是否是自定义类型，如果不存在，则默认来源是预定义，将租户设置为默认租户（0租户）
     *
     * @param paramCode 系统参数编码
     * @param tenantId  租户Id
     * @return  系统参数来源租户
     */
    protected Long judgeAndSetSysParamSource(String paramCode, Long tenantId) {
        // 查询系统参数在当前租户是否存在，判断是否是自定义
        SystemParameter queryCount = new SystemParameter();
        queryCount.setParamCode(paramCode);
        queryCount.setTenantId(tenantId);
        int count = systemParameterRepository.selectCount(queryCount);
        if (count <= 0) {
            tenantId = BaseConstants.DEFAULT_TENANT_ID;
        }
        return tenantId;
    }
}
