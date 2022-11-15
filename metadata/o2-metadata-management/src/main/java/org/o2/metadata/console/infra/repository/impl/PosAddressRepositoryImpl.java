package org.o2.metadata.console.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.console.api.dto.PosAddressQueryInnerDTO;
import org.o2.metadata.console.infra.entity.PosAddress;
import org.o2.metadata.console.infra.repository.PosAddressRepository;
import org.o2.metadata.console.infra.mapper.PosAddressMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 详细地址 资源库实现
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Component
public class PosAddressRepositoryImpl extends BaseRepositoryImpl<PosAddress> implements PosAddressRepository {
    private final PosAddressMapper posAddressMapper;

    public PosAddressRepositoryImpl(final PosAddressMapper posAddressMapper) {
        this.posAddressMapper = posAddressMapper;
    }

    @Override
    public PosAddress findDetailedAddressById(final Long addressId) {
        return posAddressMapper.findDetailedAddressById(addressId);
    }

    @Override
    public List<PosAddress> listPosAddress(PosAddressQueryInnerDTO posAddressQueryInnerDTO, Long tenantId) {
        return posAddressMapper.listPosAddress(posAddressQueryInnerDTO, tenantId);
    }
}
