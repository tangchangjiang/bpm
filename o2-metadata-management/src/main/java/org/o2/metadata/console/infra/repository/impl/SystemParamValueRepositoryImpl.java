package org.o2.metadata.console.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.console.domain.entity.SystemParamValue;
import org.o2.metadata.console.domain.repository.SystemParamValueRepository;
import org.o2.metadata.console.infra.mapper.SystemParamValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * 系统参数值 资源库实现
 *
 * @author hongyun.wang01@hand-china.com 2020-02-13
 */
@Component
public class SystemParamValueRepositoryImpl extends BaseRepositoryImpl<SystemParamValue> implements SystemParamValueRepository {
    @Autowired
    private SystemParamValueMapper systemParamValueMapper;

    @Override
    public String getSysValueByParam(String paramCode, Long tenantId) {
        return systemParamValueMapper.getSysValueByParam(paramCode, tenantId);
    }

    @Override
    public List<String> getSysListByParam(String paramCode, Long tenantId) {
        return systemParamValueMapper.getSysListByParam(paramCode, tenantId);
    }

    @Override
    public Set<String> getSysSetByParam(String paramCode, Long tenantId) {
        return systemParamValueMapper.getSysSetByParam(paramCode, tenantId);
    }
}
