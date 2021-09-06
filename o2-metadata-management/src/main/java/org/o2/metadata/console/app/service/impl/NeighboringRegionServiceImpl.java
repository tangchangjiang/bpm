package org.o2.metadata.console.app.service.impl;

import com.google.common.collect.Maps;
import org.o2.metadata.console.api.co.NeighboringRegionCO;
import org.o2.metadata.console.api.dto.NeighboringRegionQueryDTO;
import org.o2.metadata.console.api.dto.RegionQueryLovDTO;
import org.o2.metadata.console.app.service.NeighboringRegionService;
import org.o2.metadata.console.infra.convertor.NeighboringRegionConverter;
import org.o2.metadata.console.infra.entity.NeighboringRegion;
import org.o2.metadata.console.infra.entity.Region;
import org.o2.metadata.console.infra.repository.NeighboringRegionRepository;
import org.o2.metadata.console.infra.repository.RegionRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * 临近省应用服务默认实现
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Service
public class NeighboringRegionServiceImpl implements NeighboringRegionService {
    private final NeighboringRegionRepository neighboringRegionRepository;
    private final RegionRepository regionRepository;

    public NeighboringRegionServiceImpl(final NeighboringRegionRepository neighboringRegionRepository,
                                        RegionRepository regionRepository) {
        this.neighboringRegionRepository = neighboringRegionRepository;
        this.regionRepository = regionRepository;
    }

    @Override
    public List<NeighboringRegion> batchInsert(Long organizationId,final List<NeighboringRegion> neighboringRegions) {

        final boolean isRepeat = neighboringRegions.size() == new HashSet<>(neighboringRegions).size();
        Assert.isTrue(isRepeat, "多条数据【服务点类型、发货省、收货省】不能重复");

        for (final NeighboringRegion region : neighboringRegions) {
            region.setTenantId(organizationId);
            Assert.isTrue(!region.exist(neighboringRegionRepository, region), "存在重复的数据");
        }
        return neighboringRegionRepository.batchInsertSelective(neighboringRegions);
    }


    @Override
    public List<NeighboringRegion> findNeighboringRegions(final NeighboringRegionQueryDTO neighboringRegion) {
        List<NeighboringRegion> list = neighboringRegionRepository.findNeighboringRegions(neighboringRegion);
        List<String> regionCodes = new ArrayList<>();
        for (NeighboringRegion bean : list) {
            regionCodes.add(bean.getSourceRegionCode());
            regionCodes.add(bean.getTargetRegionCode());
        }
        RegionQueryLovDTO dto = new RegionQueryLovDTO();
        dto.setTenantId(neighboringRegion.getTenantId());
        dto.setRegionCodes(regionCodes);
        List<Region> regionList = regionRepository.listRegionLov(dto, neighboringRegion.getTenantId());
        Map<String,Region> regionMap = Maps.newHashMapWithExpectedSize(regionList.size());
        for (Region region : regionList) {
            regionMap.put(region.getRegionCode(), region);
        }
        for (NeighboringRegion bean : list) {
            String sourceCode = bean.getSourceRegionCode();
            Region sourceRegion = regionMap.get(sourceCode);
            String targetCode  = bean.getTargetRegionCode();
            Region targetRegion = regionMap.get(targetCode);
            if (null != targetRegion) {
                bean.setTargetCountryCode(targetRegion.getCountryCode());
                bean.setTargetCountryName(targetRegion.getCountryName());
                bean.setTargetRegionName(targetRegion.getRegionName());
            }
            if (null != sourceRegion) {
                bean.setSourceCountryCode(sourceRegion.getCountryCode());
                bean.setSourceCountryName(sourceRegion.getCountryName());
            }

        }
        return list;
    }

    @Override
    public List<NeighboringRegionCO> listNeighboringRegions(Long organizationId) {
        NeighboringRegionQueryDTO queryDTO = new NeighboringRegionQueryDTO();
        queryDTO.setTenantId(organizationId);
        List<NeighboringRegion> list = this.findNeighboringRegions(queryDTO);
        return NeighboringRegionConverter.poToCoListObjects(list);
    }
}
