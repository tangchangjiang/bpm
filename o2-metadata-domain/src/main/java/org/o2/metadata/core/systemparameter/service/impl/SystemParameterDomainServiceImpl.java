package org.o2.metadata.core.systemparameter.service.impl;

import com.alibaba.fastjson.JSONArray;
import org.apache.commons.collections4.CollectionUtils;
import org.o2.metadata.core.infra.constants.MetadataDomainConstants;
import org.o2.metadata.core.systemparameter.domain.SystemParamValueDO;
import org.o2.metadata.core.systemparameter.domain.SystemParameterDO;
import org.o2.metadata.core.systemparameter.repository.SystemParameterDomainRepository;
import org.o2.metadata.core.systemparameter.service.SystemParameterDomainService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
        String object = systemParameterDomainRepository.getSystemParameter(paramCode, key);
        if (object != null) {
            response.setDefaultValue(object);
        }
        //set类型 不重复
        String keySet = String.format(MetadataDomainConstants.SystemParameter.KEY, tenantId, MetadataDomainConstants.ParamType.SET);
        object = systemParameterDomainRepository.getSystemParameter(paramCode, keySet);
        if (null != object) {
            response.setSetSystemParamValue(new HashSet(JSONArray.parseArray(object, SystemParamValueDO.class)));
        }
        return response;
    }

    @Override
    public List<SystemParameterDO> listSystemParameters(List<String> paramCodes, Long tenantId) {
        List<SystemParameterDO> doList = new ArrayList<>();
        String key = String.format(MetadataDomainConstants.SystemParameter.KEY, tenantId, MetadataDomainConstants.ParamType.KV);
        List<String> list = systemParameterDomainRepository.listSystemParameters(paramCodes, key);
        if (CollectionUtils.isNotEmpty(list)) {
            for (int i = 0; i < paramCodes.size(); i++) {
                SystemParameterDO systemParameterDO = new SystemParameterDO();
                systemParameterDO.setParamCode(paramCodes.get(i));
                systemParameterDO.setDefaultValue(list.get(i));
                doList.add(systemParameterDO);
            }
            return doList;
        }
        String keySet = String.format(MetadataDomainConstants.SystemParameter.KEY, tenantId, MetadataDomainConstants.ParamType.SET);
        List<String> listSet = systemParameterDomainRepository.listSystemParameters(paramCodes, keySet);
        if (CollectionUtils.isNotEmpty(list)) {
            for (int i = 0; i < paramCodes.size(); i++) {
                SystemParameterDO systemParameterDO = new SystemParameterDO();
                systemParameterDO.setParamCode(paramCodes.get(i));
                systemParameterDO.setSetSystemParamValue(new HashSet(JSONArray.parseArray(listSet.get(i), SystemParamValueDO.class)));
                doList.add(systemParameterDO);
            }
            return doList;
        }
        return doList;
    }
}
