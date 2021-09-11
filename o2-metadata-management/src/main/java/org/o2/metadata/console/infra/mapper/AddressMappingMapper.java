package org.o2.metadata.console.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.o2.metadata.console.api.dto.AddressMappingQueryDTO;
import org.o2.metadata.console.api.dto.AddressMappingQueryInnerDTO;
import org.o2.metadata.console.infra.entity.AddressMapping;
import org.o2.metadata.console.infra.entity.RegionTreeChild;

import java.util.List;

/**
 * 地址匹配Mapper
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface AddressMappingMapper extends BaseMapper<AddressMapping> {
    /**
     * 根据条件查询地址匹配
     *
     * @param addressMappingQueryDTO 地址匹配
     * @param countryCode    国家编码
     * @return 结果集
     */
    List<RegionTreeChild> findAddressMappingByCondition(@Param("addressMapping") AddressMappingQueryDTO addressMappingQueryDTO, String countryCode);

    /**
     * 更据catalogCode和regionId查询地址匹配数量
     *
     * @param platformCode meaning
     * @param regionCode    meaning
     * @param tenantId    meaning
     * @return the return
     * @throws RuntimeException exception description
     */
    List<AddressMapping> queryAddressByCondition(@Param(value = "platformCode") String platformCode,
                                                 @Param(value = "regionCode") String regionCode,
                                                 @Param(value = "tenantId") Long tenantId);


    /**
     *  批量查询
     * @param  regionCodes 地区编码
     * @param  type 类型
     * @param  tenantId 租户ID
     * @return list
     */
    List<RegionTreeChild> listAddressMapping(@Param(value = "regionCodes")List<String> regionCodes,
                                             @Param(value = "type")String type,
                                             @Param(value = "tenantId") Long tenantId);


    /**
     * 内部方法 批量查询地址匹配
     * @param addressMappingQueryInts 参数
     * @param tenantId 租户ID
     * @param platformCode 平台编码
     * @return  list
     */
    List<AddressMapping> listAddressMappings(@Param("query") List<AddressMappingQueryInnerDTO> addressMappingQueryInts,
                                             @Param("tenantId") Long tenantId,
                                             @Param("platformCode") String platformCode);
}
