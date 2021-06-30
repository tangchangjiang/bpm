package org.o2.metadata.console.domain.repository;

import org.apache.ibatis.annotations.Param;
import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.console.domain.entity.PosAddress;


/**
 * 详细地址资源库
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface PosAddressRepository extends BaseRepository<PosAddress> {
    /**
     * 查询配货单详细地址，包含省市区name
     *
     * @param addressId id
     * @return 详细地址对象
     */
    PosAddress findDetailedAddressById(@Param("posAddressId") Long addressId);
}
