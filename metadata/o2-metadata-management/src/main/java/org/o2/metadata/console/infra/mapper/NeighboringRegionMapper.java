package org.o2.metadata.console.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.o2.metadata.console.api.dto.NeighboringRegionQueryDTO;
import org.o2.metadata.console.infra.entity.NeighboringRegion;

import java.util.List;

/**
 * 临近省Mapper
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface NeighboringRegionMapper extends BaseMapper<NeighboringRegion> {
    /**
     * 查询临近省结果集
     *
     * @param neighboringRegionDTO    入参
     * @return 查询结果集
     */
    List<NeighboringRegion> findNeighboringRegions(NeighboringRegionQueryDTO neighboringRegionDTO);
}
