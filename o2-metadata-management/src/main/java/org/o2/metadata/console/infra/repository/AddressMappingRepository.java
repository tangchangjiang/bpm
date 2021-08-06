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


    /**
     * 内部方法 批量查询地址匹配
     * @param externalCodes 外部编码
     * @param addressTypeCodes 类型
     * @param externalNames 外部名称
     * @param tenantId 租户ID
     * @return  list
     */
    List<AddressMapping> listAddressMappings(List<String> externalCodes,List<String> addressTypeCodes,List<String> externalNames , Long tenantId);
}
