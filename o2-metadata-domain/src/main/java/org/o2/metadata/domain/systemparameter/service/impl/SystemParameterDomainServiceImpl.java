package org.o2.metadata.domain.systemparameter.service.impl;

import org.o2.metadata.domain.systemparameter.domain.SystemParameterDO;
import org.o2.metadata.domain.systemparameter.repository.SystemParameterDomainRepository;
import org.o2.metadata.domain.systemparameter.service.SystemParameterDomainService;
import org.springframework.stereotype.Service;


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
        return systemParameterDomainRepository.getSystemParameter(paramCode,tenantId);
    }

    @Override
    public List<SystemParameterDO> listSystemParameters(List<String> paramCodes, Long tenantId) {
        return  systemParameterDomainRepository.listSystemParameters(paramCodes,tenantId);
    }
}
