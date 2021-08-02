package org.o2.metadata.console.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.o2.metadata.console.api.dto.PosAddressDTO;
import org.o2.metadata.console.infra.entity.PosAddress;

import java.util.List;


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

    /**
     * 查询服务地址
     * @param posAddressDTO 服务点地址
     * @param tenantId 租户ID
     * @return  list
     */
    List<PosAddress> listPosAddress(PosAddressDTO posAddressDTO, Long tenantId);
}
