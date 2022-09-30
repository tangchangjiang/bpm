package org.o2.metadata.app.service.impl;

import org.o2.metadata.api.co.CarrierCO;
import org.o2.metadata.app.service.CarrierService;
import org.o2.metadata.domain.carrier.repository.CarrierDomainRepository;
import org.o2.metadata.infra.convertor.CarrierConverter;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * 承运商
 *
 * @author yipeng.zhu@hand-china.com 2021-08-05
 **/
@Service
public class CarrierServiceImpl implements CarrierService {
    private CarrierDomainRepository carrierDomainRepository;

    public CarrierServiceImpl(CarrierDomainRepository carrierDomainRepository) {
        this.carrierDomainRepository = carrierDomainRepository;
    }

    @Override
    @Cacheable(value = "O2MD_METADATA", key = "'carrier'+'_'+#tenantId")
    public List<CarrierCO> listCarriers(Long tenantId) {
        return CarrierConverter.doToCoListObjects(carrierDomainRepository.listCarriers(tenantId));
    }
}
