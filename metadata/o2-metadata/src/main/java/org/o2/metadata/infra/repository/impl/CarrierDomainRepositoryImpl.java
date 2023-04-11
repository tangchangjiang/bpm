package org.o2.metadata.infra.repository.impl;

import org.o2.core.helper.QueryFallbackHelper;
import org.o2.metadata.domain.carrier.domain.CarrierDO;
import org.o2.metadata.domain.carrier.repository.CarrierDomainRepository;
import org.o2.metadata.infra.convertor.CarrierConverter;
import org.o2.metadata.infra.redis.CarrierRedis;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * 承运商
 *
 * @author yipeng.zhu@hand-china.com 2021-08-05
 **/
@Component
public class CarrierDomainRepositoryImpl implements CarrierDomainRepository {
    private final  CarrierRedis carrierRedis;

    public CarrierDomainRepositoryImpl(CarrierRedis carrierRedis) {
        this.carrierRedis = carrierRedis;
    }

    @Override
    public List<CarrierDO> listCarriers(Long tenantId) {
        return CarrierConverter.poToDoListObjects(QueryFallbackHelper.siteFallback(tenantId, carrierRedis::listCarriers));
    }
}
