package org.o2.metadata.app.service;

import org.o2.metadata.domain.entity.NeighboringRegion;

import java.util.List;

/**
 * 临近省应用服务
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface NeighboringRegionService {

    /**
     * 添加临近省
     *
     * @param neighboringRegions 临近省数据
     * @param organizationId 租户ID
     * @return 临近省集合
     */
    List<NeighboringRegion> batchInsert(Long organizationId,List<NeighboringRegion> neighboringRegions);

    /**
     * 分页查询临近省
     *
     * @param neighboringRegion 临近省查询条件
     * @return 查询结果集
     */
    List<NeighboringRegion> findNeighboringRegions(NeighboringRegion neighboringRegion);

}
