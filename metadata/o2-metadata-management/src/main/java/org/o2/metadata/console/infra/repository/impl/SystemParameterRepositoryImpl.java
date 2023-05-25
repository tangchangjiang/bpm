package org.o2.metadata.console.infra.repository.impl;

import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.console.infra.entity.SystemParameter;
import org.o2.metadata.console.infra.repository.SystemParameterRepository;
import org.o2.metadata.console.infra.mapper.SystemParameterMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统参数 资源库实现
 *
 * @author hongyun.wang01@hand-china.com 2020-02-13
 */
@Repository
public class SystemParameterRepositoryImpl extends BaseRepositoryImpl<SystemParameter> implements SystemParameterRepository {

    private final SystemParameterMapper systemParameterMapper;

    public SystemParameterRepositoryImpl(SystemParameterMapper systemParameterMapper) {
        this.systemParameterMapper = systemParameterMapper;
    }

    @Override
    public List<SystemParameter> fuzzyQuery(SystemParameter systemParameter, Long tenantId, Integer siteQueryFlag) {
        return systemParameterMapper.fuzzyQuery(systemParameter, tenantId, siteQueryFlag);
    }

    @Override
    public List<SystemParameter> queryInitData(Long tenantId) {
        return systemParameterMapper.queryInitData(tenantId);
    }

    @Override
    public SystemParameter findOne(SystemParameter systemParameter) {
        if (null == systemParameter.getTenantId()) {
            systemParameter.setTenantId(BaseConstants.DEFAULT_TENANT_ID);
        }
        SystemParameter sysParam = this.selectOne(systemParameter);
        if (null == sysParam && !BaseConstants.DEFAULT_TENANT_ID.equals(systemParameter.getTenantId())) {
            systemParameter.setTenantId(BaseConstants.DEFAULT_TENANT_ID);
            sysParam = this.selectOne(systemParameter);
        }
        return sysParam;
    }
}
