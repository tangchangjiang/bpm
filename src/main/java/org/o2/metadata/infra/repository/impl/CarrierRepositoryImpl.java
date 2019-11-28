package org.o2.metadata.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.domain.entity.Carrier;
import org.o2.metadata.domain.repository.CarrierRepository;
import org.o2.metadata.infra.mapper.CarrierMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 承运商 资源库实现
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Component
public class CarrierRepositoryImpl extends BaseRepositoryImpl<Carrier> implements CarrierRepository {
    private final CarrierMapper carrierMapper;

    public CarrierRepositoryImpl(final CarrierMapper carrierMapper) {
        this.carrierMapper = carrierMapper;
    }

    @Override
    public List<Carrier> listCarrier(final Carrier carrier) {
        return carrierMapper.listCarrier(carrier);
    }
}
