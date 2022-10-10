package org.o2.metadata.app.service.impl;


import org.o2.metadata.api.dto.FreightDTO;
import org.o2.metadata.api.co.FreightInfoCO;
import org.o2.metadata.app.service.FreightService;
import org.o2.metadata.domain.freight.repository.FreightTemplateDomainRepository;
import org.o2.metadata.infra.convertor.FreightConverter;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(value = "O2MD_METADATA", key = "'freight'+'_'+#freight")
    public FreightInfoCO getFreightTemplate(FreightDTO freight) {
      return FreightConverter.doToCoObject(freightTemplateDomainRepository.getFreightTemplate(freight.getRegionCode(),freight.getTemplateCode(),freight.getTenantId()));
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