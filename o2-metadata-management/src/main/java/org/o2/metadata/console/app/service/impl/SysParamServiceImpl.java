package org.o2.metadata.console.app.service.impl;


import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.o2.metadata.console.api.dto.SystemParameterDTO;
import org.o2.metadata.console.api.vo.ResponseVO;
import org.o2.metadata.console.app.service.SysParamService;
import org.o2.metadata.console.infra.convertor.SysParameterConvertor;

import org.o2.metadata.console.api.vo.SystemParameterVO;
import org.o2.metadata.console.infra.entity.SystemParamValue;
import org.o2.metadata.console.infra.entity.SystemParameter;
import org.o2.metadata.console.infra.redis.SystemParameterRedis;
import org.o2.metadata.console.infra.repository.SystemParamValueRepository;
import org.o2.metadata.console.infra.repository.SystemParameterRepository;
import org.o2.metadata.domain.systemparameter.domain.SystemParameterDO;
import org.o2.metadata.domain.systemparameter.service.SystemParameterDomainService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author yong.nie@hand-china.com
 * @date 2020/6/30 21:26
 **/
@Slf4j
@Service
public class SysParamServiceImpl implements SysParamService {

    private final SystemParameterDomainService systemParameterDomainService;
    private final SystemParameterRepository systemParameterRepository;
    private final SystemParameterRedis systemParameterRedis;
    private final SystemParamValueRepository systemParamValueRepository;

    public SysParamServiceImpl(SystemParameterDomainService systemParameterDomainService,
                               SystemParameterRepository systemParameterRepository,
                               SystemParameterRedis systemParameterRedis, SystemParamValueRepository systemParamValueRepository) {

        this.systemParameterDomainService = systemParameterDomainService;
        this.systemParameterRepository = systemParameterRepository;
        this.systemParameterRedis = systemParameterRedis;
        this.systemParamValueRepository = systemParamValueRepository;
    }


    @Override
    public SystemParameterVO getSystemParameter(String paramCode, Long tenantId) {
        SystemParameterDO systemParameterDO = systemParameterDomainService.getSystemParameter(paramCode, tenantId);
        return SysParameterConvertor.doToVoObject(systemParameterDO);
    }

    @Override
    public List<SystemParameterVO> listSystemParameters(List<String> paramCodes, Long tenantId) {
        return SysParameterConvertor.doToVoListObjects(systemParameterDomainService.listSystemParameters(paramCodes, tenantId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSystemParameter(SystemParameter systemParameter, Long tenantId) {
        systemParameterRepository.insertSelective(systemParameter);
        systemParameterRedis.updateToRedis(systemParameter, tenantId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSystemParameter(SystemParameter systemParameter, Long tenantId) {
        systemParameterRepository.updateByPrimaryKey(systemParameter);
        systemParameterRedis.updateToRedis(systemParameter, tenantId);
        systemParameterRedis.extraOperate(systemParameter.getParamCode(), tenantId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseVO updateSysParameter(SystemParameterDTO systemParameterDTO, Long tenantId) {
        ResponseVO vo = new ResponseVO();
        SystemParameter  select = new SystemParameter();
        select.setParamCode(systemParameterDTO.getParamCode());
        select.setTenantId(tenantId);
        List<SystemParameter>  systemParameterList = systemParameterRepository.select(select);
        if (CollectionUtils.isEmpty(systemParameterList) || systemParameterList.size() > 1) {
            vo.setSuccess(BaseConstants.FIELD_FAILED);
            return vo;
        }

        SystemParameter systemParameter = systemParameterList.get(0);
        SystemParamValue selectValue = new SystemParamValue();
        selectValue.setParamId(systemParameter.getParamId());
        selectValue.setTenantId(tenantId);
        List<SystemParamValue> systemParamValueList =  systemParamValueRepository.select(selectValue);
        if (CollectionUtils.isEmpty(systemParamValueList)) {
            vo.setSuccess(BaseConstants.FIELD_FAILED);
            return vo;
        }

        Map<String,String> map = systemParameterDTO.getHashMap();
        for (SystemParamValue value : systemParamValueList) {
            String  paramValue = map.get(value.getParamKey());
            if (StringUtils.isEmpty(paramValue)) {
                continue;
            }
            value.setParamValue(paramValue);
        }

        systemParamValueRepository.batchUpdateByPrimaryKeySelective(systemParamValueList);
        systemParameterRedis.updateToRedis(systemParameter,tenantId);
        vo.setSuccess(BaseConstants.FIELD_SUCCESS);
        return vo;
    }

}
