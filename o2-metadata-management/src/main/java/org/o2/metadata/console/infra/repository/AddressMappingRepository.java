package org.o2.metadata.console.infra.repository;


import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.console.infra.entity.AddressMapping;

import java.util.List;

/**
 * 地址匹配资源库
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface AddressMappingRepository extends BaseRepository<AddressMapping> {

    /**
     * 更据catalogCode和regionId查询地址匹配数量
     * @param catalogCode meaning
     * @param regionId meaning
     * @param tenantId meaning
     * @return the return
     * @throws RuntimeException exception description
     */
    List<AddressMapping> queryAddressByCondition(String catalogCode, Long regionId, Long tenantId);
}
