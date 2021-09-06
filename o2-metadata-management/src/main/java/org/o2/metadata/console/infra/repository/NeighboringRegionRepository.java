package org.o2.metadata.console.infra.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.console.api.dto.NeighboringRegionQueryDTO;
import org.o2.metadata.console.infra.entity.NeighboringRegion;

import java.util.List;

/**
 * 临近省资源库
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface NeighboringRegionRepository extends BaseRepository<NeighboringRegion> {

    /**
     * 查询临近省结果集
     *
     * @param neighboringRegionDTO 查询条件
     * @return 查询结果集
     */
    List<NeighboringRegion> findNeighboringRegions(NeighboringRegionQueryDTO neighboringRegionDTO);

}
