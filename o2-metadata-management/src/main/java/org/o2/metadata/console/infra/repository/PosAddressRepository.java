package org.o2.metadata.console.infra.repository;

import org.apache.ibatis.annotations.Param;
import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.console.api.dto.PosAddressQueryInnerDTO;
import org.o2.metadata.console.infra.entity.PosAddress;

import java.util.List;


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

    /**
     * 查询服务地址
     * @param posAddressQueryInnerDTO 服务点地址
     * @param tenantId 租户ID
     * @return  list
     */
    List<PosAddress> listPosAddress(PosAddressQueryInnerDTO posAddressQueryInnerDTO, Long tenantId);
}
