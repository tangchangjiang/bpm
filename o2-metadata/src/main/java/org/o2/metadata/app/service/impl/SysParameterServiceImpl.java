package org.o2.metadata.app.service.impl;


import org.o2.metadata.api.vo.SystemParameterVO;
import org.o2.metadata.app.service.SysParameterService;
import org.o2.metadata.domain.systemparameter.domain.SystemParameterDO;
import org.o2.metadata.domain.systemparameter.service.SystemParameterDomainService;
import org.o2.metadata.infra.convertor.SysParameterConverter;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(value = "O2MD_METADATA", key = "'systemParameter'+'_'+#tenantId+'_'+#paramCode")
    public SystemParameterVO getSystemParameter(String paramCode, Long tenantId) {
        SystemParameterDO systemParameterDO =systemParameterDomainService.getSystemParameter(paramCode,tenantId);
        return SysParameterConverter.doToVoObject(systemParameterDO);
    }

    @Override
    @Cacheable(value = "O2MD_METADATA", key = "'systemParameter'+'_'+#organizationId+'_'+#paramCodes")
    public List<SystemParameterVO> listSystemParameters(List<String> paramCodes, Long organizationId) {
        return SysParameterConverter.doToVoListObjects(systemParameterDomainService.listSystemParameters(paramCodes, organizationId));
    }
}
