package org.o2.metadata.console.domain.repository.impl;

import org.o2.metadata.console.infra.convertor.FreightConvertor;
import org.o2.metadata.console.infra.redis.FreightRedis;
import org.o2.metadata.domain.freight.domain.FreightInfoDO;
import org.o2.metadata.domain.freight.repository.FreightTemplateDomainRepository;
import org.springframework.stereotype.Component;

/**
 *
 * 获取运费模版信息
 *
 * @author yipeng.zhu@hand-china.com 2021-07-19
 **/
@Component
public class FreightTemplateDomainRepositoryImpl implements FreightTemplateDomainRepository {
    private final FreightRedis freightRedis;

    public FreightTemplateDomainRepositoryImpl(FreightRedis freightRedis) {
        this.freightRedis = freightRedis;
    }

    @Override
    public FreightInfoDO getFreightTemplate(String regionCode, String templateCode, Long tenantId) {
        return FreightConvertor.poToDoObject(freightRedis.getFreightTemplate(regionCode,templateCode,tenantId));
    }
}
