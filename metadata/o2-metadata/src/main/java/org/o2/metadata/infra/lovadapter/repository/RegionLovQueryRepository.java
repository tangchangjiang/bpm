package org.o2.metadata.infra.lovadapter.repository;

import org.o2.metadata.api.dto.RegionQueryLovInnerDTO;
import org.o2.metadata.infra.entity.Region;

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

}
