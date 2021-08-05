package org.o2.metadata.console.domain.repository.impl;

import org.o2.metadata.console.infra.convertor.SysParameterConverter;
import org.o2.metadata.console.infra.redis.SystemParameterRedis;
import org.o2.metadata.domain.systemparameter.domain.SystemParameterDO;
import org.o2.metadata.domain.systemparameter.repository.SystemParameterDomainRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * 系统参数仓库层实现
 *
 * @author yipeng.zhu@hand-china.com 2021-07-09
 **/
@Component
public class SystemParameterDomainRepositoryImpl implements SystemParameterDomainRepository {
    private final SystemParameterRedis systemParameterRedis;

    public SystemParameterDomainRepositoryImpl(SystemParameterRedis systemParameterRedis) {
        this.systemParameterRedis = systemParameterRedis;
    }


    @Override
    public List<SystemParameterDO> listSystemParameters(List<String> paramCodeList, Long tenantId) {
        return SysParameterConverter.poToDoListObjects(systemParameterRedis.listSystemParameters(paramCodeList,tenantId));
    }

    @Override
    public SystemParameterDO getSystemParameter(String paramCode, Long tenantId) {
        return SysParameterConverter.poToDoObject( systemParameterRedis.getSystemParameter(paramCode,tenantId));

    }
}
