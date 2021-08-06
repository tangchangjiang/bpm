package org.o2.metadata.console.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.o2.metadata.console.api.dto.AddressMappingQueryDTO;
import org.o2.metadata.console.api.vo.RegionTreeChildVO;
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
     * 根据region_id  type 查询地址内部外部匹配数据
     *
     * @param id       region_id 地区id
     * @param type     平台类型
     * @param tenantId 租户ID
     * @return 查询结果集
     */
    List<RegionTreeChild> findAddressMappingById(@Param(value = "id") Long id, @Param(value = "type") String type, @Param(value = "tenantId") Long tenantId);

    /**
     * 根据regionCode  catalogCode查询地址内部外部匹配数据
     *
     * @param regionCode  region_id 地区id
     * @param catalogCode 平台类型
     * @param tenantId    租户ID
     * @return 查询结果集
     */
    RegionTreeChildVO findAddressMappingByCode(@Param(value = "regionCode") String regionCode, @Param(value = "catalogCode") String catalogCode, @Param(value = "tenantId") Long tenantId);

    /**
     * 更据catalogCode和regionId查询地址匹配数量
     *
     * @param catalogCode meaning
     * @param regionId    meaning
     * @param tenantId    meaning
     * @return the return
     * @throws RuntimeException exception description
     */
    List<AddressMapping> queryAddressByCondition(@Param(value = "catalogCode") String catalogCode, @Param(value = "regionId") Long regionId, @Param(value = "tenantId") Long tenantId);

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
     * @param externalCodes 外部编码
     * @param addressTypeCodes 类型
     * @param externalNames 外部名称
     * @param tenantId 租户ID
     * @return  list
     */
    List<AddressMapping> listAddressMappings(@Param("externalCodes") List<String> externalCodes,
                                             @Param("addressTypeCodes") List<String> addressTypeCodes,
                                             @Param("externalNames") List<String> externalNames,
                                             @Param("tenantId") Long tenantId);
}
