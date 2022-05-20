package org.o2.metadata.console.infra.repository.impl;

import org.o2.metadata.console.infra.convertor.FreightConverter;
import org.o2.metadata.console.infra.entity.FreightInfo;
import org.o2.metadata.console.infra.redis.FreightRedis;
import org.o2.metadata.domain.freight.domain.FreightInfoDO;
import org.o2.metadata.domain.freight.repository.FreightTemplateDomainRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
        return FreightConverter.poToDoObject(freightRedis.getFreightTemplate(regionCode,templateCode,tenantId));
    }

    @Override
    public List<FreightInfoDO> listFreightTemplates(Long tenantId, List<String> templateCodes) {
        List<FreightInfo> freightInfos = freightRedis.listFreightTemplates(tenantId, templateCodes);
        List<FreightInfoDO> freightInfoDOS = new ArrayList<>();
        for(FreightInfo freightInfo : freightInfos){
            FreightInfoDO freightInfoDO = FreightConverter.poToDoObject(freightInfo);
            freightInfoDOS.add(freightInfoDO);
        }
        return freightInfoDOS;
    }

}
