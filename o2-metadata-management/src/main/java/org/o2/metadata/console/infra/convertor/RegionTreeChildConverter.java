package org.o2.metadata.console.infra.convertor;

import org.o2.metadata.console.api.vo.RegionTreeChildVO;
import org.o2.metadata.console.infra.entity.RegionTreeChild;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RegionTreeChildConverter{
    private RegionTreeChildConverter() {
        // 无需实现
    }

    private static List<RegionTreeChild> toRegionTreeChildList(List<RegionTreeChildVO> regionTreeChildVOList) {
        if (regionTreeChildVOList == null) {
            return Collections.emptyList();
        }
        List<RegionTreeChild> regionTreeChildList = new ArrayList<>();
        for (RegionTreeChildVO regionTreeChildVO : regionTreeChildVOList) {
            regionTreeChildList.add(toRegionTreeChild(regionTreeChildVO));
        }
        return regionTreeChildList;
    }

    private static RegionTreeChild toRegionTreeChild(RegionTreeChildVO regionTreeChildVO) {
        if (regionTreeChildVO == null) {
            return null;
        }
        RegionTreeChild regionTreeChild = new RegionTreeChild();
        regionTreeChild.setParentRegionId(regionTreeChildVO.getParentRegionId());
        regionTreeChild.setLevelPath(regionTreeChildVO.getLevelPath());
        regionTreeChild.setRegionId(regionTreeChildVO.getRegionId());
        regionTreeChild.setAddressMappingId(regionTreeChildVO.getAddressMappingId());
        regionTreeChild.setRegionCode(regionTreeChildVO.getRegionCode());
        regionTreeChild.setAddressTypeCode(regionTreeChildVO.getAddressTypeCode());
        regionTreeChild.setExternalCode(regionTreeChildVO.getExternalCode());
        regionTreeChild.setExternalName(regionTreeChildVO.getExternalName());
        regionTreeChild.setActiveFlag(regionTreeChildVO.getActiveFlag());
        regionTreeChild.setTenantId(regionTreeChildVO.getTenantId());
        regionTreeChild.setCatalogId(regionTreeChildVO.getCatalogId());
        regionTreeChild.setRegionName(regionTreeChildVO.getRegionName());
        regionTreeChild.setPlatformTypeMeaning(regionTreeChildVO.getPlatformTypeMeaning());
        regionTreeChild.setAddressTypeMeaning(regionTreeChildVO.getAddressTypeMeaning());
        regionTreeChild.setRegionPathIds(regionTreeChildVO.getRegionPathIds());
        regionTreeChild.setRegionPathCodes(regionTreeChildVO.getRegionPathCodes());
        regionTreeChild.setRegionPathNames(regionTreeChildVO.getRegionPathNames());
        regionTreeChild.setCatalogCode(regionTreeChildVO.getCatalogCode());
        regionTreeChild.setCatalogName(regionTreeChildVO.getCatalogName());
        regionTreeChild.setChildren(RegionTreeChildConverter.toRegionTreeChildList(regionTreeChildVO.getChildren()));
        return regionTreeChild;
    }

    public static List<RegionTreeChildVO> toRegionTreeChildVOList(List<RegionTreeChild> regionTreeChildList) {
        if (regionTreeChildList == null) {
            return Collections.emptyList();
        }
        List<RegionTreeChildVO> regionTreeChildVOList = new ArrayList<>();
        for (RegionTreeChild regionTreeChild : regionTreeChildList) {
            regionTreeChildVOList.add(toRegionTreeChildVO(regionTreeChild));
        }
        return regionTreeChildVOList;
    }

    private static RegionTreeChildVO toRegionTreeChildVO(RegionTreeChild regionTreeChild) {
        if (regionTreeChild == null) {
            return null;
        }
        RegionTreeChildVO regionTreeChildVO = new RegionTreeChildVO();
        regionTreeChildVO.setAddressMappingId(regionTreeChild.getAddressMappingId());
        regionTreeChildVO.setRegionCode(regionTreeChild.getRegionCode());
        regionTreeChildVO.setAddressTypeCode(regionTreeChild.getAddressTypeCode());
        regionTreeChildVO.setExternalCode(regionTreeChild.getExternalCode());
        regionTreeChildVO.setExternalName(regionTreeChild.getExternalName());
        regionTreeChildVO.setActiveFlag(regionTreeChild.getActiveFlag());
        regionTreeChildVO.setTenantId(regionTreeChild.getTenantId());
        regionTreeChildVO.setCatalogId(regionTreeChild.getCatalogId());
        regionTreeChildVO.setRegionName(regionTreeChild.getRegionName());
        regionTreeChildVO.setPlatformTypeMeaning(regionTreeChild.getPlatformTypeMeaning());
        regionTreeChildVO.setAddressTypeMeaning(regionTreeChild.getAddressTypeMeaning());
        regionTreeChildVO.setRegionPathIds(regionTreeChild.getRegionPathIds());
        regionTreeChildVO.setRegionPathCodes(regionTreeChild.getRegionPathCodes());
        regionTreeChildVO.setRegionPathNames(regionTreeChild.getRegionPathNames());
        regionTreeChildVO.setCatalogCode(regionTreeChild.getCatalogCode());
        regionTreeChildVO.setCatalogName(regionTreeChild.getCatalogName());
        regionTreeChildVO.setParentRegionId(regionTreeChild.getParentRegionId());
        regionTreeChildVO.setLevelPath(regionTreeChild.getLevelPath());
        regionTreeChildVO.setRegionId(regionTreeChild.getRegionId());
        regionTreeChildVO.setChildren(RegionTreeChildConverter.toRegionTreeChildVOList(regionTreeChild.getChildren()));

        return regionTreeChildVO;
    }
}