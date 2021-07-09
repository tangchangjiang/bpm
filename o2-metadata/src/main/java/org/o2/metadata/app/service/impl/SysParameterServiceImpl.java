package org.o2.metadata.app.service.impl;


import org.o2.metadata.api.vo.SystemParameterVO;
import org.o2.metadata.app.service.SysParameterService;
import org.o2.metadata.core.systemparameter.domain.SystemParameterDO;
import org.o2.metadata.core.systemparameter.service.SystemParameterDomainService;
import org.o2.metadata.infra.convertor.SysParameteConvertor;
import org.springframework.stereotype.Service;


/**
 * SysParameter RPC Provider
 *
 * @author mark.bao@hand-china.com 2019/11/29
 */
@Service
public class SysParameterServiceImpl implements SysParameterService {
    private final SystemParameterDomainService systemParameterDomainService;

    public SysParameterServiceImpl(SystemParameterDomainService systemParameterDomainService) {
        this.systemParameterDomainService = systemParameterDomainService;
    }

    @Override
    public SystemParameterVO listSystemParameter(String paramCode, Long tenantId) {
        SystemParameterDO systemParameterDO =systemParameterDomainService.getSystemParameter(paramCode,tenantId);
        return SysParameteConvertor.doToVoObject(systemParameterDO);
    }
}
