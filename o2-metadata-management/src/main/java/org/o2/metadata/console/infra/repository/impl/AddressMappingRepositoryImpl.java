package org.o2.metadata.console.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.console.api.dto.AddressMappingQueryInnerDTO;
import org.o2.metadata.console.infra.entity.AddressMapping;
import org.o2.metadata.console.infra.repository.AddressMappingRepository;
import org.o2.metadata.console.infra.mapper.AddressMappingMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 地址匹配 资源库实现
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Component
public class AddressMappingRepositoryImpl extends BaseRepositoryImpl<AddressMapping> implements AddressMappingRepository {

    private final AddressMappingMapper addressMappingMapper;

    public AddressMappingRepositoryImpl(AddressMappingMapper addressMappingMapper) {
        this.addressMappingMapper = addressMappingMapper;
    }

    /**
     * 更据catalogCode和regionId查询地址匹配数量
     * @param platformCode 目录编码
     * @param regionCode 地区编码
     * @param tenantId 租户ID
     * @return the list
     * @throws RuntimeException exception description
     */
    @Override
    public List<AddressMapping> queryAddressByCondition(String platformCode, String regionCode,Long tenantId) {
        return addressMappingMapper.queryAddressByCondition(platformCode,regionCode,tenantId);
    }

    @Override
    public List<AddressMapping> listAddressMappings(AddressMappingQueryInnerDTO addressMappingQueryInts, Long tenantId) {
        return addressMappingMapper.listAddressMappings(addressMappingQueryInts,tenantId);
    }

}
