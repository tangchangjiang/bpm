package org.o2.metadata.console.app.service.impl;


import org.o2.metadata.console.app.service.SysParamService;
import org.o2.metadata.console.infra.convertor.SysParameterConvertor;

import org.o2.metadata.console.api.vo.SystemParameterVO;
import org.o2.metadata.console.infra.entity.SystemParameter;
import org.o2.metadata.console.infra.redis.SystemParameterRedis;
import org.o2.metadata.console.infra.repository.SystemParameterRepository;
import org.o2.metadata.domain.systemparameter.domain.SystemParameterDO;
import org.o2.metadata.domain.systemparameter.service.SystemParameterDomainService;
import org.springframework.stereotype.Service;

import java.util.List;


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

    public SysParamServiceImpl(SystemParameterDomainService systemParameterDomainService,
                               SystemParameterRepository systemParameterRepository,
                               SystemParameterRedis systemParameterRedis) {

        this.systemParameterDomainService = systemParameterDomainService;
        this.systemParameterRepository = systemParameterRepository;
        this.systemParameterRedis = systemParameterRedis;
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

}
