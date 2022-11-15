package org.o2.metadata.console.app.service;

import org.o2.metadata.console.api.dto.RegionQueryDTO;
import org.o2.metadata.console.api.vo.AreaRegionVO;
import org.o2.metadata.console.api.vo.RegionVO;

import java.util.List;

/**
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface RegionService {
    /**
     * 查询地区树，返回父级ID
     *
     * @param countryIdOrCode 国家ID或CODE
     * @param condition       查询条件
     * @param enabledFlag     筛选条件
     * @param tenantId        租户ID
     * @return 当前节点以及父级ID
     */
    List<RegionVO> treeRegionWithParent(String countryIdOrCode, String condition, Integer enabledFlag, Long tenantId);


    /**
     * 查询各个大区下的省份
     *
     * @param countryCode 国家编码
     * @param enabledFlag 是否启用
     * @param tenantId    租户ID
     * @return list
     */
    List<AreaRegionVO> listAreaRegion(String countryCode, Integer enabledFlag, Long tenantId);


    /**
     * 查询地区
     *
     * @param regionQueryDTO 查询条件
     * @param tenantId       租户ID
     * @return 地区列表
     */
    List<RegionVO> listChildren(RegionQueryDTO regionQueryDTO, Long tenantId);

}

