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
import org.apache.commons.lang3.StringUtils;

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
    public List<Region> listRegionWithParent(final String countryCode, final String condition, final Integer enabledFlag,Long tenantId) {
        //1.查询地区
        RegionQueryLovDTO dto = new RegionQueryLovDTO();
        dto.setTenantId(tenantId);
        dto.setEnabledFlag(enabledFlag);
        dto.setCountryCode(countryCode);
        dto.setRegionCode(condition);
        final Set<Region> regionSet = new HashSet<>(this.listRegionLov(dto,tenantId));
        // 2.地址编码集合
        final Set<String> regionSetCodes = new HashSet<>();
        for (final Region r : regionSet) {
            regionSetCodes.add(r.getRegionCode());
        }
        // 3.查询父地区
        if (!CollectionUtils.isEmpty(regionSet) && StringUtils.isNotEmpty(condition)) {
            RegionQueryLovDTO queryParent = new RegionQueryLovDTO();
            Set<Long> parentIds = regionSet.stream().filter(item -> item.getParentRegionId() != null).map(Region::getParentRegionId).collect(Collectors.toSet());
            boolean hasFather = !parentIds.isEmpty();
            while (hasFather) {
                queryParent.setParentRegionIds(new ArrayList<>(parentIds));
                queryParent.setEnabledFlag(enabledFlag);
                final Set<Region> fatherList = new HashSet<>(this.listRegionLov(queryParent,tenantId));
                for (final Region r : fatherList) {
                    if (!regionSetCodes.contains(r.getRegionCode())) {
                        regionSet.add(r);
                        regionSetCodes.add(r.getRegionCode());
                    }
                }
                //父ID
                parentIds = fatherList.stream().filter(item -> item.getParentRegionId() != null).map(Region::getParentRegionId).collect(Collectors.toSet());
                //是否还有父级
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
        if (!CollectionUtils.isEmpty(regionQueryLov.getRegionCodes())){
            queryParams.put(RegionConstants.RegionLov.REGION_CODES.getCode(), StringUtils.join(regionQueryLov.getRegionCodes(), BaseConstants.Symbol.COMMA));
        }
        queryParams.put(RegionConstants.RegionLov.COUNTRY_CODE.getCode(), regionQueryLov.getCountryCode());
        queryParams.put(RegionConstants.RegionLov.REGION_CODE.getCode(), regionQueryLov.getRegionCode());
        queryParams.put(RegionConstants.RegionLov.REGION_NAME.getCode(), regionQueryLov.getRegionName());
        Long  parentRegionId = regionQueryLov.getParentRegionId();
        if (null != parentRegionId) {
            queryParams.put(RegionConstants.RegionLov.PARENT_REGION_ID.getCode(), String.valueOf(regionQueryLov.getParentRegionId()) );

        }
        queryParams.put(RegionConstants.RegionLov.PARENT_REGION_CODE.getCode(), regionQueryLov.getParentRegionCode());
        if (!CollectionUtils.isEmpty(regionQueryLov.getParentRegionIds())) {
            queryParams.put(RegionConstants.RegionLov.PARENT_REGION_IDS.getCode(), StringUtils.join(regionQueryLov.getParentRegionIds(), BaseConstants.Symbol.COMMA));

        }
        if (null != regionQueryLov.getEnabledFlag()) {
            queryParams.put(RegionConstants.RegionLov.ENABLED_FLAG.getCode(), String.valueOf(regionQueryLov.getEnabledFlag()));
        }

        if (!CollectionUtils.isEmpty(regionQueryLov.getNotInRegionCodes())) {
            queryParams.put(RegionConstants.RegionLov.NOT_IN_REGION_CODE.getCode(), StringUtils.join(regionQueryLov.getNotInRegionCodes(), BaseConstants.Symbol.COMMA));
        }

        if (!CollectionUtils.isEmpty(regionQueryLov.getRegionIds())) {
            queryParams.put(RegionConstants.RegionLov.REGION_IDS.getCode(), StringUtils.join(regionQueryLov.getRegionIds(), BaseConstants.Symbol.COMMA));
        }
        if (null != regionQueryLov.getLevelNumber()) {
            queryParams.put(RegionConstants.RegionLov.LEVEL_NUMBER.getCode(),String.valueOf(regionQueryLov.getLevelNumber()));
        }

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

