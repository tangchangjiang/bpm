package org.o2.metadata.console.app.service.impl;


import io.choerodon.core.exception.CommonException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.util.List;

/**
 * 系统参数值应用服务默认实现
 *
 * @author hongyun.wang01@hand-china.com 2020-02-13
 */
@Service
public class SystemParamValueServiceImpl implements SystemParamValueService {
    private SystemParamValueRepository systemParamValueRepository;
    private SystemParameterRepository systemParameterRepository;
    private SystemParameterRedis systemParameterRedis;

    public SystemParamValueServiceImpl(SystemParamValueRepository systemParamValueRepository,
                                       SystemParameterRepository systemParameterRepository,
                                       SystemParameterRedis systemParameterRedis) {
        this.systemParamValueRepository = systemParamValueRepository;
        this.systemParameterRepository = systemParameterRepository;
        this.systemParameterRedis = systemParameterRedis;
    }


    @Override
    public String getSysValueByParam(String paramCode, Long tenantId) {
        if (SystemParameterConstants.ParamType.KV.equalsIgnoreCase(getParamTypeByCode(paramCode, tenantId))) {
            return systemParamValueRepository.getSysValueByParam(paramCode, tenantId);
        }
        return null;
    }

    @Override
    public List<String> getSysListByParam(String paramCode, Long tenantId) {
        if (SystemParameterConstants.ParamType.LIST.equalsIgnoreCase(getParamTypeByCode(paramCode, tenantId))) {
            return systemParamValueRepository.getSysListByParam(paramCode, tenantId);
        }
        return new ArrayList<>();

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
        systemParameterRedis.deleteSystemParamValue(systemParameter,systemParamValue);
    }

    @Override
    public void systemParamValueValidate(SystemParamValue systemParamValue) {
        SystemParameter systemParameter = getSystemParameter(systemParamValue);
        if (SystemParameterConstants.ParamType.MAP.equals(systemParameter.getParamTypeCode())){
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
        return systemParameterRepository.selectOne(queryParam);
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
