package org.o2.metadata.console.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.console.infra.entity.CarrierCantDelivery;
import org.o2.metadata.console.infra.mapper.CarrierCantDeliveryMapper;
import org.o2.metadata.console.infra.repository.CarrierCantDeliveryRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 承运商不可送达范围 资源库实现
 *
 * @author zhilin.ren@hand-china.com 2022-10-10 13:44:09
 */
@Component
public class CarrierCantDeliveryRepositoryImpl extends BaseRepositoryImpl<CarrierCantDelivery> implements CarrierCantDeliveryRepository {

    private final CarrierCantDeliveryMapper carrierCantDeliveryMapper;

    public CarrierCantDeliveryRepositoryImpl(CarrierCantDeliveryMapper carrierCantDeliveryMapper) {
        this.carrierCantDeliveryMapper = carrierCantDeliveryMapper;
    }

    @Override
    public List<CarrierCantDelivery> list(CarrierCantDelivery query) {
        return carrierCantDeliveryMapper.list(query);
    }
}
