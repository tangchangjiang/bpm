package org.o2.metadata.console.infra.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.console.api.dto.AddressMappingQueryInnerDTO;
import org.o2.metadata.console.api.dto.AddressReleaseDTO;
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
     *
     * @param catalogCode meaning
     * @param regionCode  meaning
     * @param tenantId    meaning
     * @return the return
     * @throws RuntimeException exception description
     */
    List<AddressMapping> queryAddressByCondition(String catalogCode, String regionCode, Long tenantId);

    /**
     * 内部方法 批量查询地址匹配
     *
     * @param addressMappingQueryInts 参数
     * @param tenantId                租户ID
     * @return list
     */
    List<AddressMapping> listAddressMappings(AddressMappingQueryInnerDTO addressMappingQueryInts, Long tenantId);

    /**
     * 查询地区匹配信息
     *
     * @param addressReleaseDTO 查询条件
     * @return AddressMapping
     */
    List<AddressMapping> queryAddress(AddressReleaseDTO addressReleaseDTO);
}
