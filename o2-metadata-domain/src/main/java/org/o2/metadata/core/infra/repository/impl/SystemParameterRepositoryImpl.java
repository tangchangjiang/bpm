package org.o2.metadata.core.infra.repository.impl;


import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.core.domain.entity.SystemParameter;
import org.o2.metadata.core.domain.repository.SystemParameterRepository;
import org.o2.metadata.core.infra.mapper.SystemParameterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统参数 资源库实现
 *
 * @author hongyun.wang01@hand-china.com 2020-02-13
 */
@Repository
public class SystemParameterRepositoryImpl extends BaseRepositoryImpl<SystemParameter> implements SystemParameterRepository {

    @Autowired
    private SystemParameterMapper systemParameterMapper;

    @Override
    public List<SystemParameter> fuzzyQuery(SystemParameter systemParameter, Long tenantId) {
        return systemParameterMapper.fuzzyQuery(systemParameter, tenantId);
    }
}
