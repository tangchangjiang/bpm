package org.o2.metadata.core.systemparameter.service.impl;

import com.alibaba.fastjson.JSONArray;
import org.o2.metadata.core.infra.constants.MetadataDomainConstants;
import org.o2.metadata.core.systemparameter.domain.SystemParamValueDO;
import org.o2.metadata.core.systemparameter.domain.SystemParameterDO;
import org.o2.metadata.core.systemparameter.repository.SystemParameterDomainRepository;
import org.o2.metadata.core.systemparameter.service.SystemParameterDomainService;
import org.springframework.stereotype.Service;

import java.util.HashSet;

/**
 *
 * 系统参数
 *
 * @author yipeng.zhu@hand-china.com 2021-07-09
 **/
@Service
public class SystemParameterDomainServiceImpl implements SystemParameterDomainService {
    private final SystemParameterDomainRepository systemParameterDomainRepository;

    public SystemParameterDomainServiceImpl(SystemParameterDomainRepository systemParameterDomainRepository) {
        this.systemParameterDomainRepository = systemParameterDomainRepository;
    }

    @Override
    public SystemParameterDO getSystemParameter(String paramCode, Long tenantId) {
        SystemParameterDO response = new SystemParameterDO();
        response.setParamCode(paramCode);
        //hash类型
        String key = String.format(MetadataDomainConstants.SystemParameter.KEY, tenantId, MetadataDomainConstants.ParamType.KV);
        Object object =  systemParameterDomainRepository.getSystemParameter(paramCode,key);
        if (object != null) {
            response.setDefaultValue(String.valueOf(object));
        }
        //set类型 不重复
        String keySet = String.format(MetadataDomainConstants.SystemParameter.KEY, tenantId, MetadataDomainConstants.ParamType.SET);
        object =  systemParameterDomainRepository.getSystemParameter(paramCode,keySet);
        if (null != object) {
            response.setSetSystemParamValue(new HashSet(JSONArray.parseArray(String.valueOf(object), SystemParamValueDO.class)));
        }
        return response;
    }
}
