package org.o2.metadata.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.o2.metadata.domain.entity.PosAddress;

/**
 * 详细地址Mapper
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface PosAddressMapper extends BaseMapper<PosAddress> {

    /**
     * 查询配货单详细地址，包含省市区name
     *
     * @param addressId id
     * @return 详细地址对象
     */
    PosAddress findDetailedAddressById(@Param("posAddressId") Long addressId);
}
