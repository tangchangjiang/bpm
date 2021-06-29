package org.o2.metadata.core.infra.repository.impl;

import org.o2.metadata.core.domain.entity.Region;
import org.o2.metadata.core.domain.repository.RegionRepository;
import org.o2.metadata.core.domain.vo.RegionVO;
import org.o2.metadata.core.infra.mapper.RegionMapper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Component
public class RegionRepositoryImpl extends BaseRepositoryImpl<Region> implements RegionRepository {
    private final RegionMapper regionMapper;

    public RegionRepositoryImpl(final RegionMapper regionMapper) {
        this.regionMapper = regionMapper;
    }

    @Override
    public List<Region> listRegionWithParent(final String countryIdOrCode, final String condition, final Integer enabledFlag,Long tenantId) {
        final Set<Region> regionSet = regionMapper.selectRegion(countryIdOrCode, condition, enabledFlag,tenantId);
        final Set<String> regionSetCodes = new HashSet<>();
        for (final Region r : regionSet) {
            regionSetCodes.add(r.getRegionCode());
        }
        if (!CollectionUtils.isEmpty(regionSet) && StringUtils.hasText(condition)) {
            Set<Long> parentIds = regionSet.stream().filter(item -> item.getParentRegionId() != null).map(Region::getParentRegionId).collect(Collectors.toSet());
            boolean hasFather = !parentIds.isEmpty();
            while (hasFather) {
                final Set<Region> fatherList = regionMapper.selectRegionParent(parentIds, enabledFlag);
                for (final Region r : fatherList) {
                    if (!regionSetCodes.contains(r.getRegionCode())) {
                        regionSet.add(r);
                        regionSetCodes.add(r.getRegionCode());
                    }
                }
                parentIds = fatherList.stream().filter(item -> item.getParentRegionId() != null).map(Region::getParentRegionId).collect(Collectors.toSet());
                hasFather = !parentIds.isEmpty();
            }
        }
        return new ArrayList<>(regionSet).stream().sorted(Comparator.comparingLong(Region::getRegionId)).collect(Collectors.toList());
    }

    @Override
    public List<RegionVO> listChildren(final String countryIdOrCode, final Long parentRegionId, final Integer enabledFlag,Long tenantId) {
        return regionMapper.listChildren(countryIdOrCode, parentRegionId, enabledFlag,tenantId);
    }

    @Override
    public List<Region> listRegionByLevelPath(final List<String> levelPathList,Long tenantId) {
        return regionMapper.listRegionByLevelPath(levelPathList,tenantId);
    }

    @Override
    public List<Region> listRegionChildrenByLevelPath(final String levelPath,final Long tenantId) {
        return regionMapper.listRegionChildrenByLevelPath(levelPath,tenantId);
    }
}

