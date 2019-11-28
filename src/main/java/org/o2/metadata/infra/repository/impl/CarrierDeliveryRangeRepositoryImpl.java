package org.o2.metadata.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.domain.entity.CarrierDeliveryRange;
import org.o2.metadata.domain.repository.CarrierDeliveryRangeRepository;
import org.o2.metadata.infra.mapper.CarrierDeliveryRangeMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 承运商送达范围 资源库实现
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Component
public class CarrierDeliveryRangeRepositoryImpl extends BaseRepositoryImpl<CarrierDeliveryRange> implements CarrierDeliveryRangeRepository {
    private final CarrierDeliveryRangeMapper carrierDeliveryRangeMapper;

    public CarrierDeliveryRangeRepositoryImpl(final CarrierDeliveryRangeMapper carrierDeliveryRangeMapper) {
        this.carrierDeliveryRangeMapper = carrierDeliveryRangeMapper;
    }

    @Override
    public List<CarrierDeliveryRange> list(final CarrierDeliveryRange carrierDeliveryRange) {
        return carrierDeliveryRangeMapper.list(carrierDeliveryRange);
    }

    @Override
    public CarrierDeliveryRange detail(final Long deliveryRangeId) {
        final CarrierDeliveryRange carrierDeliveryRange = new CarrierDeliveryRange();
        carrierDeliveryRange.setDeliveryRangeId(deliveryRangeId);
        final List<CarrierDeliveryRange> list = carrierDeliveryRangeMapper.list(carrierDeliveryRange);
        return list.size() > 0 ? list.get(0) : null;
    }
}
