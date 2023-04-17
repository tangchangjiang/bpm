package org.o2.metadata.infra.repository.impl;

import org.hzero.core.base.BaseConstants;
import org.o2.metadata.domain.freight.domain.FreightInfoDO;
import org.o2.metadata.domain.freight.domain.FreightTemplateDO;
import org.o2.metadata.domain.freight.repository.FreightTemplateDomainRepository;
import org.o2.metadata.infra.convertor.FreightConverter;
import org.o2.metadata.infra.entity.FreightInfo;
import org.o2.metadata.infra.redis.FreightRedis;
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
        if (null == tenantId) {
            tenantId = BaseConstants.DEFAULT_TENANT_ID;
        }
        FreightInfo freightTemplate = freightRedis.getFreightTemplate(regionCode, templateCode, tenantId);
        // 如果当前租户没有运费模板数据，则查询0租户作为兜底
        if (null == freightTemplate.getHeadTemplate() && null == freightTemplate.getRegionTemplate() && !BaseConstants.DEFAULT_TENANT_ID.equals(tenantId)) {
            tenantId = BaseConstants.DEFAULT_TENANT_ID;
            freightTemplate = freightRedis.getFreightTemplate(regionCode, templateCode, tenantId);
        }
        return FreightConverter.poToDoObject(freightTemplate);
    }

    @Override
    public List<FreightTemplateDO> listFreightTemplate(Long tenantId, List<String> templateCodes) {
        List<FreightInfo> freightInfos = freightRedis.listFreightTemplate(tenantId, templateCodes);
        List<FreightTemplateDO> freightTemplateDOs = new ArrayList<>();
        for (FreightInfo freightInfo : freightInfos) {
            FreightTemplateDO freightTemplateDO = FreightConverter.toFreightTemplateDO(freightInfo.getHeadTemplate());
            freightTemplateDOs.add(freightTemplateDO);
        }
        return freightTemplateDOs;
    }

}
