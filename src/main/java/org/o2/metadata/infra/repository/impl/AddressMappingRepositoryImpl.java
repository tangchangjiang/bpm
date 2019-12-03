package org.o2.metadata.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.domain.entity.AddressMapping;
import org.o2.metadata.domain.repository.AddressMappingRepository;
import org.o2.metadata.infra.mapper.AddressMappingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 地址匹配 资源库实现
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Component
public class AddressMappingRepositoryImpl extends BaseRepositoryImpl<AddressMapping> implements AddressMappingRepository {

    @Autowired
    private AddressMappingMapper addressMappingMapper;
    /**
     * 更据catalogCode和regionId查询地址匹配数量
     * @param catalogCode meaning
     * @param catalogCode meaning
     * @return the return
     * @throws RuntimeException exception description
     */
    @Override
    public List<AddressMapping> queryAddressByCondition(String catalogCode, Long regionId,Long tenantId) {
        return addressMappingMapper.queryAddressByCondition(catalogCode,regionId,tenantId);
    }
}
