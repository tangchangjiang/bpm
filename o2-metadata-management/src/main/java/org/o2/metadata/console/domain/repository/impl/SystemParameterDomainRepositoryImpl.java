package org.o2.metadata.console.domain.repository.impl;

import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.core.systemparameter.domain.SystemParameterDO;
import org.o2.metadata.core.systemparameter.repository.SystemParameterDomainRepository;
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
    private  final RedisCacheClient redisCacheClient;

    public SystemParameterDomainRepositoryImpl(RedisCacheClient redisCacheClient) {
        this.redisCacheClient = redisCacheClient;
    }

    @Override
    public List<SystemParameterDO> listSystemParameters(List<String> paramCodeList, Long tenantId) {
        return null;
    }

    @Override
    public Object getSystemParameter(String paramCode, String key) {
        return redisCacheClient.opsForHash().get(key, paramCode);

    }
}
