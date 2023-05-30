package org.o2.metadata.app.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.o2.cache.util.CacheHelper;
import org.o2.metadata.api.co.CarrierCO;
import org.o2.metadata.app.service.CarrierService;
import org.o2.metadata.domain.carrier.repository.CarrierDomainRepository;
import org.o2.metadata.infra.constants.CarrierConstants;
import org.o2.metadata.infra.constants.MetadataCacheConstants;
import org.o2.metadata.infra.convertor.CarrierConverter;
import org.o2.multi.language.infra.util.O2RedisMultiLanguageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 承运商
 *
 * @author yipeng.zhu@hand-china.com 2021-08-05
 **/
@Service
public class CarrierServiceImpl implements CarrierService {
    private final CarrierDomainRepository carrierDomainRepository;
    private final O2RedisMultiLanguageHelper o2RedisMultiLanguageHelper;

    public CarrierServiceImpl(CarrierDomainRepository carrierDomainRepository, O2RedisMultiLanguageHelper o2RedisMultiLanguageHelper) {
        this.carrierDomainRepository = carrierDomainRepository;
        this.o2RedisMultiLanguageHelper = o2RedisMultiLanguageHelper;
    }

    @Override
    public List<CarrierCO> listCarriers(Long tenantId) {
        List<CarrierCO> carrierList = CacheHelper.getCache(MetadataCacheConstants.CacheName.O2MD_METADATA,
                MetadataCacheConstants.CacheKey.getCarrierPrefix(tenantId),
                String.valueOf(tenantId),
                param -> CarrierConverter.doToCoListObjects(carrierDomainRepository.listCarriers(Long.valueOf(param))),
                false);
        if (CollectionUtils.isNotEmpty(carrierList)) {
            carrierList.forEach(carrier -> o2RedisMultiLanguageHelper.getMultiLang(carrier,
                    CarrierConstants.Redis.getCarrierMultiKey(tenantId, carrier.getCarrierCode())));
        }
        return carrierList;
    }
}
