package org.o2.metadata.console.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.console.domain.entity.CarrierMapping;
import org.o2.metadata.console.domain.repository.CarrierMappingRepository;
import org.o2.metadata.console.api.vo.CarrierMappingVO;
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
    public List<CarrierMappingVO> listCarrierMappingByCondition(final CarrierMappingVO carrierMappingVO) {
        return carrierMappingMapper.listCarrierMappingByCondition(carrierMappingVO);
    }
}
