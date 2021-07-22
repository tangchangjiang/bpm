package org.o2.metadata.app.service.impl;


import org.o2.metadata.api.dto.FreightDTO;
import org.o2.metadata.api.vo.FreightInfoVO;
import org.o2.metadata.app.service.FreightService;
import org.o2.metadata.domain.freight.repository.FreightTemplateDomainRepository;
import org.o2.metadata.infra.convertor.FreightConvertor;
import org.springframework.stereotype.Service;

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
    public FreightInfoVO getFreightTemplate(FreightDTO freight) {
      return FreightConvertor.doToVoObject(freightTemplateDomainRepository.getFreightTemplate(freight.getRegionCode(),freight.getTemplateCodes(),freight.getTenantId()));
    }

}
