package org.o2.metadata.app.service;

import org.o2.ext.metadata.api.dto.AreaRegionDTO;
import org.o2.ext.metadata.domain.entity.Region;

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
     * @return 当前节点以及父级ID
     */
    List<Region> treeRegionWithParent(String countryIdOrCode, String condition, Integer enabledFlag);


    /**
     * 查询各个大区下的省份
     *
     * @param countryIdOrCode
     * @param enabledFlag
     * @return
     */
    List<AreaRegionDTO> listAreaRegion(String countryIdOrCode, Integer enabledFlag);

    /**
     * 创建地区定义
     *
     * @param region 地区
     * @return 创建之后的地区定义
     */
    Region createRegion(Region region);

    /**
     * 更新地区定义
     *
     * @param region 地区
     * @return 更新之后的地区定义
     */
    Region updateRegion(Region region);

    /**
     * 禁用地区定义
     *
     * @param region 地区
     * @return 更新后的地区定义
     */
    List<Region> disableOrEnable(Region region);

    /**
     * @param regionCode
     * @return
     */
    Region getRegionByCode(String regionCode);
}

