package org.o2.metadata.console.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.console.domain.entity.NeighboringRegion;
import org.o2.metadata.console.domain.repository.NeighboringRegionRepository;
import org.o2.metadata.console.infra.mapper.NeighboringRegionMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 临近省 资源库实现
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Component
public class NeighboringRegionRepositoryImpl extends BaseRepositoryImpl<NeighboringRegion> implements NeighboringRegionRepository {
    private final NeighboringRegionMapper neighboringRegionMapper;

    public NeighboringRegionRepositoryImpl(final NeighboringRegionMapper neighboringRegionMapper) {
        this.neighboringRegionMapper = neighboringRegionMapper;
    }

    @Override
    public List<NeighboringRegion> findNeighboringRegions(final String posTypeCode,
                                                          final Long sourceRegionId,
                                                          final Long targetRegionId,
                                                          final Long tenantId) {
        return neighboringRegionMapper.findNeighboringRegions(posTypeCode, sourceRegionId, targetRegionId,tenantId);
    }
}
