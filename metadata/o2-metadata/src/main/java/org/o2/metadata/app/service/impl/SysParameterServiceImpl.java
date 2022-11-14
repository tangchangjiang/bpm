package org.o2.metadata.app.service.impl;

import org.o2.metadata.api.co.SystemParameterCO;
import org.o2.metadata.app.service.SysParameterService;
import org.o2.metadata.domain.systemparameter.domain.SystemParameterDO;
import org.o2.metadata.domain.systemparameter.service.SystemParameterDomainService;
import org.o2.metadata.infra.convertor.SysParameterConverter;
import org.springframework.stereotype.Service;

import java.util.List;


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
    public SystemParameterCO getSystemParameter(String paramCode, Long tenantId) {
        SystemParameterDO systemParameterDO = systemParameterDomainService.getSystemParameter(paramCode, tenantId);
        return SysParameterConverter.doToCoObject(systemParameterDO);
    }

    @Override
    public List<SystemParameterCO> listSystemParameters(List<String> paramCodes, Long organizationId) {
        return SysParameterConverter.doToCoListObjects(systemParameterDomainService.listSystemParameters(paramCodes, organizationId));
    }
}
