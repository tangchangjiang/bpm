package org.o2.metadata.console.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.console.domain.entity.PosAddress;
import org.o2.metadata.console.domain.repository.PosAddressRepository;
import org.o2.metadata.console.infra.mapper.PosAddressMapper;
import org.springframework.stereotype.Component;

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
}
