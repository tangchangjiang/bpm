package org.o2.metadata.console.infra.repository.impl;

import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.core.helper.FastJsonHelper;
import org.o2.metadata.console.api.dto.RegionQueryLovDTO;
import org.o2.metadata.console.infra.constant.RegionConstants;
import org.o2.metadata.console.infra.entity.Region;
import org.o2.metadata.console.infra.repository.RegionRepository;
import org.o2.metadata.console.api.vo.RegionVO;
import org.o2.metadata.console.infra.mapper.RegionMapper;
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
    private final LovAdapter lovAdapter;

    public RegionRepositoryImpl(final RegionMapper regionMapper, LovAdapter lovAdapter) {
        this.regionMapper = regionMapper;
        this.lovAdapter = lovAdapter;
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
    @Override
    public List<Region> listRegionLov(RegionQueryLovDTO regionQueryLov, Long tenantId) {
        List<Region> regionList = new ArrayList<>();
        Map<String,String> queryParams = new HashMap<>(16);
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(regionQueryLov.getRegionCodes())){
            queryParams.put(RegionConstants.RegionLov.REGION_CODES.getCode(), org.apache.commons.lang3.StringUtils.join(regionQueryLov.getRegionCodes(), BaseConstants.Symbol.COMMA));
        }
        queryParams.put(RegionConstants.RegionLov.COUNTRY_CODE.getCode(), regionQueryLov.getCountryCode());
        queryParams.put(RegionConstants.RegionLov.REGION_CODE.getCode(), regionQueryLov.getRegionCode());
        queryParams.put(RegionConstants.RegionLov.REGION_NAME.getCode(), regionQueryLov.getRegionName());

        List<Map<String,Object>> list = lovAdapter.queryLovData(RegionConstants.RegionLov.LOV_CODE.getCode(),tenantId, null,  BaseConstants.PAGE_NUM, null , queryParams);
        if (list.isEmpty()){
            return regionList;
        }
        for (Map<String, Object> map : list) {
            regionList.add(FastJsonHelper.stringToObject(FastJsonHelper.objectToString(map), Region.class));
        }
        return regionList;
    }
}

