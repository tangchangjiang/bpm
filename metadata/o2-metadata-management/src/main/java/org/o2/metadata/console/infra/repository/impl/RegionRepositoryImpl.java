package org.o2.metadata.console.infra.repository.impl;

import org.o2.metadata.console.api.dto.RegionQueryLovInnerDTO;
import org.o2.metadata.console.infra.entity.Region;
import org.o2.metadata.console.infra.lovadapter.repository.RegionLovQueryRepository;
import org.o2.metadata.console.infra.repository.RegionRepository;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Component
public class RegionRepositoryImpl implements RegionRepository {
    private final RegionLovQueryRepository regionLovQueryRepository;

    public RegionRepositoryImpl(RegionLovQueryRepository regionLovQueryRepository) {
        this.regionLovQueryRepository = regionLovQueryRepository;
    }

    @Override
    public List<Region> listRegionWithParent(final String countryCode, final String condition, final Integer enabledFlag, Long tenantId) {
        //1.查询地区
        RegionQueryLovInnerDTO dto = new RegionQueryLovInnerDTO();
        dto.setTenantId(tenantId);
        dto.setCountryCode(countryCode);
        dto.setRegionCode(condition);
        List<Region> regionList = this.listRegionLov(dto, tenantId);
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
     *
     * @param current  一级分类
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
    public List<Region> listRegionLov(RegionQueryLovInnerDTO regionQueryLov, Long tenantId) {
        return regionLovQueryRepository.queryRegion(tenantId, regionQueryLov);
    }
}

