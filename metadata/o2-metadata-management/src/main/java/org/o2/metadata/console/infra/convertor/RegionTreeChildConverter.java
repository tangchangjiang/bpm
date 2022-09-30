package org.o2.metadata.console.infra.convertor;

import org.o2.metadata.console.api.vo.RegionTreeChildVO;
import org.o2.metadata.console.infra.entity.RegionTreeChild;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 *
 * 地区
 *
 * @author yipeng.zhu@hand-china.com 2021-08-04
 **/
class RegionTreeChildConverter{
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
        regionTreeChild.setParentRegionCode(regionTreeChildVO.getParentRegionCode());
        regionTreeChild.setLevelPath(regionTreeChildVO.getLevelPath());
        regionTreeChild.setRegionCode(regionTreeChildVO.getRegionCode());
        regionTreeChild.setAddressTypeCode(regionTreeChildVO.getAddressTypeCode());
        regionTreeChild.setExternalCode(regionTreeChildVO.getExternalCode());
        regionTreeChild.setExternalName(regionTreeChildVO.getExternalName());
        regionTreeChild.setActiveFlag(regionTreeChildVO.getActiveFlag());
        regionTreeChild.setTenantId(regionTreeChildVO.getTenantId());
        regionTreeChild.setRegionName(regionTreeChildVO.getRegionName());
        regionTreeChild.setPlatformTypeMeaning(regionTreeChildVO.getPlatformTypeMeaning());
        regionTreeChild.setAddressTypeMeaning(regionTreeChildVO.getAddressTypeMeaning());
        regionTreeChild.setPlatformCode(regionTreeChildVO.getPlatformCode());
        regionTreeChild.setPlatformName(regionTreeChildVO.getPlatformName());

        List<RegionTreeChild> children =  RegionTreeChildConverter.toRegionTreeChildList(regionTreeChildVO.getChildren());
        if (!children.isEmpty()) {
            regionTreeChild.setChildren(children);
        }
        return regionTreeChild;
    }

    static List<RegionTreeChildVO> toRegionTreeChildVOList(List<RegionTreeChild> regionTreeChildList) {
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
        regionTreeChildVO.setRegionCode(regionTreeChild.getRegionCode());
        regionTreeChildVO.setAddressTypeCode(regionTreeChild.getAddressTypeCode());
        regionTreeChildVO.setExternalCode(regionTreeChild.getExternalCode());
        regionTreeChildVO.setExternalName(regionTreeChild.getExternalName());
        regionTreeChildVO.setActiveFlag(regionTreeChild.getActiveFlag());
        regionTreeChildVO.setAddressMappingId(regionTreeChild.getAddressMappingId());
        regionTreeChildVO.setTenantId(regionTreeChild.getTenantId());
        regionTreeChildVO.setRegionName(regionTreeChild.getRegionName());
        regionTreeChildVO.setAddressMappingId(regionTreeChild.getAddressMappingId());
        regionTreeChildVO.setPlatformTypeMeaning(regionTreeChild.getPlatformTypeMeaning());
        regionTreeChildVO.setAddressTypeMeaning(regionTreeChild.getAddressTypeMeaning());
        regionTreeChildVO.setPlatformCode(regionTreeChild.getPlatformCode());
        regionTreeChildVO.setPlatformName(regionTreeChild.getPlatformName());
        regionTreeChildVO.setParentRegionCode(regionTreeChild.getParentRegionCode());
        regionTreeChildVO.setLevelPath(regionTreeChild.getLevelPath());
        regionTreeChildVO.set_token(regionTreeChild.get_token());
        List<RegionTreeChildVO> children =  RegionTreeChildConverter.toRegionTreeChildVOList(regionTreeChild.getChildren());
        if (!children.isEmpty()) {
            regionTreeChildVO.setChildren(children);
        }

        return regionTreeChildVO;
    }
}
