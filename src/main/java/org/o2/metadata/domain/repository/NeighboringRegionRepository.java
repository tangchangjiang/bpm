package org.o2.metadata.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.domain.entity.NeighboringRegion;

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
     * @param posTypeCode    服务点类型
     * @param sourceRegionId 发货省
     * @param targetRegionId 收货省
     * @return 查询结果集
     */
    List<NeighboringRegion> findNeighboringRegions(String posTypeCode, Long sourceRegionId, Long targetRegionId);

}
