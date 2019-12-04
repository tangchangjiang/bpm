package org.o2.metadata.app.service.impl;

import com.google.common.base.Preconditions;
import org.o2.metadata.app.service.NeighboringRegionService;
import org.o2.metadata.domain.entity.NeighboringRegion;
import org.o2.metadata.domain.repository.NeighboringRegionRepository;
import org.o2.metadata.infra.constants.BasicDataConstants;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.List;

/**
 * 临近省应用服务默认实现
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Service
public class NeighboringRegionServiceImpl implements NeighboringRegionService {
    private final NeighboringRegionRepository neighboringRegionRepository;

    public NeighboringRegionServiceImpl(final NeighboringRegionRepository neighboringRegionRepository) {
        this.neighboringRegionRepository = neighboringRegionRepository;
    }

    @Override
    public List<NeighboringRegion> batchInsert(final List<NeighboringRegion> neighboringRegions) {

        final boolean isRepeat = neighboringRegions.size() == new HashSet<>(neighboringRegions).size();
        Assert.isTrue(isRepeat, "多条数据【服务点类型、发货省、收货省】不能重复");

        for (final NeighboringRegion region : neighboringRegions) {
            Preconditions.checkArgument(null != region.getTenantId(), BasicDataConstants.ErrorCode.BASIC_DATA_TENANT_ID_IS_NULL);
            Assert.isTrue(!region.exist(neighboringRegionRepository, region), "存在重复的数据");
        }
        return neighboringRegionRepository.batchInsertSelective(neighboringRegions);
    }


    @Override
    public List<NeighboringRegion> findNeighboringRegions(final NeighboringRegion neighboringRegion) {
        return neighboringRegionRepository.findNeighboringRegions(
                neighboringRegion.getPosTypeCode(),
                neighboringRegion.getSourceRegionId(),
                neighboringRegion.getTargetRegionId());
    }
}
