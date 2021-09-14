package org.o2.metadata.console.infra.repository.impl;

import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.core.helper.JsonHelper;
import org.o2.metadata.console.api.dto.RegionQueryLovDTO;
import org.o2.metadata.console.infra.constant.RegionConstants;
import org.o2.metadata.console.infra.entity.Region;
import org.o2.metadata.console.infra.lovadapter.HzeroLovQueryRepository;
import org.o2.metadata.console.infra.repository.RegionRepository;
import org.o2.metadata.console.api.vo.RegionVO;
import org.o2.metadata.console.infra.mapper.RegionMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Component
public class RegionRepositoryImpl extends BaseRepositoryImpl<Region> implements RegionRepository {
    private final RegionMapper regionMapper;
    private final HzeroLovQueryRepository hzeroLovQueryRepository;

    public RegionRepositoryImpl(RegionMapper regionMapper, HzeroLovQueryRepository hzeroLovQueryRepository) {
        this.regionMapper = regionMapper;
        this.hzeroLovQueryRepository = hzeroLovQueryRepository;
    }


    @Override
    public List<Region> listRegionWithParent(final String countryCode, final String condition, final Integer enabledFlag,Long tenantId) {
        //1.查询地区
        RegionQueryLovDTO dto = new RegionQueryLovDTO();
        dto.setTenantId(tenantId);
        dto.setEnabledFlag(enabledFlag);
        dto.setCountryCode(countryCode);
        dto.setRegionCode(condition);
        List<Region>  regionList  = this.listRegionLov(dto,tenantId);
        List<Region> regionTree = new ArrayList<>();
        for (Region region : regionList) {
            if (StringUtils.isEmpty(region.getParentRegionCode())) {
                region.setChildren(getChildren(region, regionList));
                regionTree.add(region);
            }
        }

        return regionTree;
    }


    /**
     * 递归查找指定分类的所有子分类( 所有菜单的子菜单)
     * @param current 一级分类
     * @param entities 地区数据
     * @return list
     */
    private List<Region> getChildren(Region current, List<Region> entities) {
        //找到子分类
        List<Region> children = new ArrayList<>();
        for (Region regionEntity : entities) {
            if (current.getRegionCode().equals(regionEntity.getParentRegionCode())) {
                regionEntity.setChildren(getChildren(regionEntity, entities));
                children.add(regionEntity);
            }
        }

        return children;
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
        queryParams.put(RegionConstants.RegionLov.ADDRESS_TYPE.getCode(),RegionConstants.RegionLov.DEFAULT_DATA.getCode());
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

        if (!CollectionUtils.isEmpty(regionQueryLov.getParentRegionCodes())) {
            queryParams.put(RegionConstants.RegionLov.PARENT_REGION_CODES.getCode(), StringUtils.join(regionQueryLov.getParentRegionCodes(), BaseConstants.Symbol.COMMA));
        }

        if (null != regionQueryLov.getLevelNumber()) {
            queryParams.put(RegionConstants.RegionLov.LEVEL_NUMBER.getCode(),String.valueOf(regionQueryLov.getLevelNumber()));
        }

        if (StringUtils.isNotEmpty(regionQueryLov.getLang())) {
            queryParams.put(RegionConstants.RegionLov.LANG.getCode(),regionQueryLov.getLang());
        }

        List<Map<String,Object>> list = hzeroLovQueryRepository.queryLovValueMeaning(tenantId,RegionConstants.RegionLov.REGION_LOV_CODE.getCode(), queryParams);
        if (list.isEmpty()){
            return regionList;
        }
        for (Map<String, Object> map : list) {
            regionList.add(JsonHelper.stringToObject(JsonHelper.objectToString(map), Region.class));
        }
        return regionList;
    }
}

