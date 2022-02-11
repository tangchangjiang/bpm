package org.o2.metadata.console.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.console.api.dto.CarrierMappingQueryDTO;
import org.o2.metadata.console.api.dto.CarrierMappingQueryInnerDTO;
import org.o2.metadata.console.infra.entity.CarrierMapping;
import org.o2.metadata.console.infra.repository.CarrierMappingRepository;
import org.o2.metadata.console.infra.mapper.CarrierMappingMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 承运商匹配表 资源库实现
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Component
public class CarrierMappingRepositoryImpl extends BaseRepositoryImpl<CarrierMapping> implements CarrierMappingRepository {

    private final CarrierMappingMapper carrierMappingMapper;

    public CarrierMappingRepositoryImpl(final CarrierMappingMapper carrierMappingMapper) {
        this.carrierMappingMapper = carrierMappingMapper;
    }

    @Override
    public List<CarrierMapping> listCarrierMappingByCondition(final CarrierMappingQueryDTO carrierMappingQueryDTO) {
        return carrierMappingMapper.listCarrierMappingByCondition(carrierMappingQueryDTO);
    }

    @Override
    public List<CarrierMapping> listCarrierMappings(CarrierMappingQueryInnerDTO queryInnerDTO) {
        return carrierMappingMapper.listCarrierMappings(queryInnerDTO);
    }

    @Override
    public List<CarrierMapping> selectByCondition(CarrierMapping carrierMapping) {
        return carrierMappingMapper.selectByCondition(carrierMapping);
    }
}
