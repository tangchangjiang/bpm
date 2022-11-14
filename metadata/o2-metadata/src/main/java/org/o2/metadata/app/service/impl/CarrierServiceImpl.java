package org.o2.metadata.app.service.impl;

import org.o2.cache.util.CacheHelper;
import org.o2.metadata.api.co.CarrierCO;
import org.o2.metadata.app.service.CarrierService;
import org.o2.metadata.domain.carrier.repository.CarrierDomainRepository;
import org.o2.metadata.infra.constants.MetadataCacheConstants;
import org.o2.metadata.infra.convertor.CarrierConverter;
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
    private final CarrierDomainRepository carrierDomainRepository;

    public CarrierServiceImpl(CarrierDomainRepository carrierDomainRepository) {
        this.carrierDomainRepository = carrierDomainRepository;
    }

    @Override
    public List<CarrierCO> listCarriers(Long tenantId) {
        return CacheHelper.getCache(MetadataCacheConstants.CacheName.O2MD_METADATA,
                MetadataCacheConstants.CacheKey.getCarrierPrefix(tenantId),
                String.valueOf(tenantId),
                param -> CarrierConverter.doToCoListObjects(carrierDomainRepository.listCarriers(Long.valueOf(param))),
                false);
    }
}
