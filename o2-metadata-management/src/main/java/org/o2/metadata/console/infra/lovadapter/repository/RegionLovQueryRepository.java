package org.o2.metadata.console.infra.lovadapter.repository;

import org.o2.metadata.console.api.co.PageCO;
import org.o2.metadata.console.api.co.RegionCO;

import java.util.List;
import java.util.Map;

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
     * @param queryParam 查询条件
     * @return  list
     */
    List<RegionCO> queryRegion(Long tenantId, Map<String,String> queryParam);

    /**
     * 分页查询地区值集
     * @param tenantId 租户ID
     * @param page page 页码
     * @param size 大小
     * @param queryParam 查询参数
     * @return page
     */
    PageCO<RegionCO> queryRegionPage(Long tenantId, Integer page, Integer size, Map<String,String> queryParam);

}
