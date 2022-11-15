package org.o2.metadata.console.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.console.infra.entity.SystemParamValue;
import org.o2.metadata.console.infra.repository.SystemParamValueRepository;
import org.o2.metadata.console.infra.mapper.SystemParamValueMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 系统参数值 资源库实现
 *
 * @author hongyun.wang01@hand-china.com 2020-02-13
 */
@Component
public class SystemParamValueRepositoryImpl extends BaseRepositoryImpl<SystemParamValue> implements SystemParamValueRepository {
    private  final  SystemParamValueMapper systemParamValueMapper;

    public SystemParamValueRepositoryImpl(SystemParamValueMapper systemParamValueMapper) {
        this.systemParamValueMapper = systemParamValueMapper;
    }

    @Override
    public String getSysValueByParam(String paramCode, Long tenantId) {
        return systemParamValueMapper.getSysValueByParam(paramCode, tenantId);
    }

    @Override
    public List<String> getSysListByParam(String paramCode, Long tenantId) {
        return systemParamValueMapper.getSysListByParam(paramCode, tenantId);
    }

}
