package org.o2.metadata.console.app.service;

import org.o2.metadata.console.api.dto.AddressMappingQueryDTO;
import org.o2.metadata.console.api.dto.AddressMappingQueryIntDTO;
import org.o2.metadata.console.api.vo.AddressMappingVO;
import org.o2.metadata.console.api.vo.RegionTreeChildVO;

import java.util.List;

/**
 * 地址匹配应用服务
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface AddressMappingService {

    /**
     * 地址匹配逆向递归 树状数据结构（根据parent id 分组 减少没必要的递归）
     *
     * @param addressMappingQueryDTO 查询条件
     * @param countryCode 国家编码
     * @return 查询结果集
     */

    List<RegionTreeChildVO> findAddressMappingGroupByCondition(AddressMappingQueryDTO addressMappingQueryDTO, String countryCode);
   
    /**
     * 详情
     * @param addressMappingId 主键
     * @param countryCode 国家编码
     * @param tenantId 租户ID
     * @return 地址匹配
     */
    AddressMappingVO addressMappingDetail(Long addressMappingId, String countryCode,Long tenantId);

    /**
     * 内部方法 批量查询地址匹配
     * @param addressMappingDTO 入参
     * @param tenantId 租户ID
     * @return  list
     */
    List<AddressMappingVO> listAddressMappings(List<AddressMappingQueryIntDTO> addressMappingDTO, Long tenantId);
}
