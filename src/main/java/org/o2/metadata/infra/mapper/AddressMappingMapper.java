package org.o2.metadata.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.o2.ext.metadata.domain.entity.AddressMapping;
import org.o2.ext.metadata.domain.vo.RegionTreeChildVO;

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
     * @param addressMapping 地址匹配
     * @return 结果集
     */
    List<RegionTreeChildVO> findAddressMappingByCondition(AddressMapping addressMapping, String countryCode);

    /**
     * 根据region_id  type 查询地址内部外部匹配数据
     *
     * @param id   region_id 地区id
     * @param type 平台类型
     * @return 查询结果集
     */
    List<RegionTreeChildVO> findAddressMappingById(@Param(value = "id") Long id, @Param(value = "type") String type);

    /**
     * 根据regionCode  platformTypeCode查询地址内部外部匹配数据
     *
     * @param regionCode       region_id 地区id
     * @param platformTypeCode 平台类型
     * @return 查询结果集
     */
    RegionTreeChildVO findAddressMappingByCode(@Param(value = "regionCode") String regionCode, @Param(value = "platformTypeCode") String platformTypeCode);
}
