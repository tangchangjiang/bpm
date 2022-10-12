package org.o2.metadata.console.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.console.api.dto.CarrierFreightDTO;
import org.o2.metadata.console.api.dto.CarrierQueryInnerDTO;
import org.o2.metadata.console.app.bo.CarrierLogisticsCostBO;
import org.o2.metadata.console.infra.entity.Carrier;
import org.o2.metadata.console.infra.mapper.CarrierMapper;
import org.o2.metadata.console.infra.repository.CarrierRepository;
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

    @Override
    public List<Carrier> batchSelect(CarrierQueryInnerDTO carrierQueryInnerDTO, Long tenantId) {
        return carrierMapper.batchSelect(carrierQueryInnerDTO, tenantId);
    }

    @Override
    public List<CarrierLogisticsCostBO> listCarrierLogisticsCost(CarrierFreightDTO carrierFreightDTO) {
        return carrierMapper.listCarrierLogisticsCost(carrierFreightDTO);
    }
}
