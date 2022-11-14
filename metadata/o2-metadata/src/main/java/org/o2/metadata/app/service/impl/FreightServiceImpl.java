package org.o2.metadata.app.service.impl;


import org.o2.cache.util.CacheHelper;
import org.o2.metadata.api.co.FreightInfoCO;
import org.o2.metadata.api.dto.FreightDTO;
import org.o2.metadata.app.service.FreightService;
import org.o2.metadata.domain.freight.repository.FreightTemplateDomainRepository;
import org.o2.metadata.infra.constants.MetadataCacheConstants;
import org.o2.metadata.infra.convertor.FreightConverter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 运费计算服务默认实现
 *
 * @author peng.xu@hand-china.com 2019-06-18
 */
@Service
public class FreightServiceImpl implements FreightService {
    private final FreightTemplateDomainRepository freightTemplateDomainRepository;

    public FreightServiceImpl(FreightTemplateDomainRepository freightTemplateDomainRepository) {
        this.freightTemplateDomainRepository = freightTemplateDomainRepository;
    }

    @Override
    public FreightInfoCO getFreightTemplate(FreightDTO freight) {
        return CacheHelper.getCache(MetadataCacheConstants.CacheName.O2MD_METADATA,
                param -> MetadataCacheConstants.CacheKey.getFreightPrefix(freight.toString()),
                freight,
                param -> FreightConverter.doToCoObject(freightTemplateDomainRepository.getFreightTemplate(freight.getRegionCode(), freight.getTemplateCode(), freight.getTenantId())),
                false);
    }

    @Override
    public Map<String, FreightInfoCO> listFreightTemplates(List<FreightDTO> freightList) {
        Map<String,FreightInfoCO> resultMap = new HashMap<>(freightList.size());
        for (FreightDTO e : freightList) {
            FreightInfoCO co = getFreightTemplate(e);
            resultMap.put(co.getFreightTemplateCode(), co);
        }
        return resultMap;
    }
}
