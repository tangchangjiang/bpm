package org.o2.metadata.console.app.service;

import org.o2.metadata.console.api.dto.AreaRegionDTO;
import org.o2.metadata.core.domain.entity.Region;

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
    List<Region> treeRegionWithParent(String countryIdOrCode, String condition, Integer enabledFlag, Long tenantId);


    /**
     * 查询各个大区下的省份
     *
     * @param countryIdOrCode
     * @param enabledFlag
     * @param tenantId 租户ID
     * @return
     */
    List<AreaRegionDTO> listAreaRegion(String countryIdOrCode, Integer enabledFlag, Long tenantId);

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
     * 通过编码获取地址信息
     * @param regionCode 地址编码
     * @return the return
     * @throws RuntimeException exception description
     */
    Region getRegionByCode(String regionCode);
}
