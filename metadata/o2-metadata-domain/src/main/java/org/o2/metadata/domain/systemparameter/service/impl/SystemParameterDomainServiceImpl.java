package org.o2.metadata.domain.systemparameter.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.hzero.core.base.BaseConstants;
import org.o2.core.helper.QueryFallbackHelper;
import org.o2.metadata.domain.systemparameter.domain.SystemParameterDO;
import org.o2.metadata.domain.systemparameter.repository.SystemParameterDomainRepository;
import org.o2.metadata.domain.systemparameter.service.SystemParameterDomainService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        return QueryFallbackHelper.siteFallback(tenantId, tenant -> systemParameterDomainRepository.getSystemParameter(paramCode, tenant));
    }

    @Override
    public List<SystemParameterDO> listSystemParameters(List<String> paramCodes, Long tenantId) {
        List<SystemParameterDO> sysParams = systemParameterDomainRepository.listSystemParameters(paramCodes, tenantId);
        if (CollectionUtils.isNotEmpty(sysParams)) {
            List<String> selectedParamCodes = sysParams.stream().map(SystemParameterDO::getParamCode).collect(Collectors.toList());
            paramCodes.removeIf(selectedParamCodes::contains);
        }
        if (CollectionUtils.isNotEmpty(paramCodes)) {
            sysParams.addAll(systemParameterDomainRepository.listSystemParameters(paramCodes, BaseConstants.DEFAULT_TENANT_ID));
        }
        return sysParams;
    }
}
