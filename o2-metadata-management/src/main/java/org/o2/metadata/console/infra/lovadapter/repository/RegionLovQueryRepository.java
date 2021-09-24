package org.o2.metadata.console.infra.lovadapter.repository;

import org.o2.metadata.console.api.co.PageCO;
import org.o2.metadata.console.api.dto.RegionQueryLovInnerDTO;
import org.o2.metadata.console.infra.entity.Region;

import java.util.List;

/**
 *
 * 地区值集查询
 *
 * @author yipeng.zhu@hand-china.com 2021-09-22
 **/
public interface RegionLovQueryRepository {

    /**
     * 查询地区值集
     * @param tenantId 租户ID
     * @param innerDTO 查询条件
     * @return  list
     */
    List<Region> queryRegion(Long tenantId, RegionQueryLovInnerDTO innerDTO);

    /**
     * 分页查询地区值集
     * @param tenantId 租户ID
     * @param page page 页码
     * @param size 大小
     * @param innerDTO 查询参数
     * @return page
     */
    PageCO<Region> queryRegionPage(Long tenantId, Integer page, Integer size, RegionQueryLovInnerDTO innerDTO);

}
