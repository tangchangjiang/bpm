package org.o2.metadata.console.infra.convertor;

import org.apache.commons.collections4.CollectionUtils;
import org.o2.metadata.console.api.vo.RegionTreeChildVO;
import org.o2.metadata.console.api.vo.RegionVO;
import org.o2.metadata.console.app.bo.RegionBO;
import org.o2.metadata.console.app.bo.RegionCacheBO;
import org.o2.metadata.console.infra.entity.Region;
import org.o2.metadata.console.infra.entity.RegionTreeChild;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * 地区
 *
 * @author yipeng.zhu@hand-china.com 2021-08-03
 **/
public class RegionConverter {
    private RegionConverter() {
    }
    /**
     * po 转 vo
     * @param region 地区
     * @return  vo
     */
    public static RegionVO poToVoObject(Region region){


        if (region == null) {
            return null;
        }
        RegionVO regionVO = new RegionVO();
        regionVO.setRegionCode(region.getRegionCode());
        regionVO.setRegionName(region.getRegionName());
        regionVO.setParentRegionCode(region.getParentRegionCode());
        regionVO.setParentRegionName(region.getParentRegionName());
        regionVO.setAreaCode(region.getAreaCode());
        regionVO.setAreaMeaning(region.getAreaMeaning());
        regionVO.setTenantId(region.getTenantId());
        regionVO.setCountryCode(region.getCountryCode());
        regionVO.setCountryName(region.getCountryName());
        regionVO.setLevelNumber(region.getLevelNumber());
        if (CollectionUtils.isNotEmpty(region.getChildren())) {
            regionVO.setChildren(RegionConverter.toRegionVOList(region.getChildren()));
        }
        return regionVO;
    }

    private static RegionCacheBO poToBoObject(Region region) {

        if (region == null) {
            return null;
        }
        RegionCacheBO regionCacheBO = new RegionCacheBO();
        regionCacheBO.setRegionName(region.getRegionName());
        regionCacheBO.setParentRegionCode(region.getParentRegionCode());
        regionCacheBO.setRegionCode(region.getRegionCode());
        regionCacheBO.setCountryCode(region.getCountryCode());
        return regionCacheBO;
    }

    public static List<RegionCacheBO> poToBoListObjects(List<Region> regions) {
        List<RegionCacheBO> regionCacheBOList = new ArrayList<>();
        if (regions.isEmpty()) {
            return  regionCacheBOList;
        }
        for (Region region : regions) {
            regionCacheBOList.add(poToBoObject(region));
        }
        return regionCacheBOList;
    }
    /**
     * PO 转 VO
     * @param regionList 地址
     * @return  list
     */
    public static List<RegionVO> poToVoListObjects(List<Region> regionList) {
        List<RegionVO> regionVOList = new ArrayList<>();
        if (regionList == null) {
            return regionVOList;
        }
        for (Region region : regionList) {
            regionVOList.add(poToVoObject(region));
        }
        return regionVOList;
    }

    public static RegionTreeChildVO poToVoObject(RegionTreeChild regionTreeChild) {

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
        regionTreeChildVO.setPlatformTypeMeaning(regionTreeChild.getPlatformTypeMeaning());
        regionTreeChildVO.setAddressTypeMeaning(regionTreeChild.getAddressTypeMeaning());
        regionTreeChildVO.setPlatformCode(regionTreeChild.getPlatformCode());
        regionTreeChildVO.setPlatformName(regionTreeChild.getPlatformName());
        regionTreeChildVO.setParentRegionCode(regionTreeChild.getParentRegionCode());
        regionTreeChildVO.setLevelPath(regionTreeChild.getLevelPath());
        regionTreeChildVO.setChildren(RegionTreeChildConverter.toRegionTreeChildVOList(regionTreeChild.getChildren()));
        regionTreeChildVO.set_token(regionTreeChild.get_token());

        return regionTreeChildVO;
    }

    /**
     * PO 转 VO
     * @param regionTreeChildrenList 地区树
     * @return  list
     */
    public static List<RegionTreeChildVO> poToVoChildObjects(List<RegionTreeChild> regionTreeChildrenList) {
        List<RegionTreeChildVO> regionTreeChildVOList = new ArrayList<>();
        if (regionTreeChildrenList == null) {
            return regionTreeChildVOList;
        }
        for (RegionTreeChild regionTreeChild : regionTreeChildrenList) {
            regionTreeChildVOList.add(poToVoObject(regionTreeChild));
        }
        return regionTreeChildVOList;
    }

    /**
     * vo ->bo
     * @date 2021-08-13
     * @param regionVO sql值集地区
     * @return  bo
     */
    private static RegionBO voToBoObject(RegionVO regionVO) {

        if (regionVO == null) {
            return null;
        }
        RegionBO regionBO = new RegionBO();
        regionBO.setRegionCode(regionVO.getRegionCode());
        regionBO.setRegionName(regionVO.getRegionName());
        regionBO.setParentRegionCode(regionVO.getParentRegionCode());
        return regionBO;
    }

    /**
     * vO 转 vbo
     * @param regionList 地区树
     * @return  list
     */

    public static List<RegionBO> voToBoListObjects(List<RegionVO> regionList) {
        List<RegionBO> regionBOList = new ArrayList<>();
        if (regionList == null) {
            return regionBOList;
        }
        for (RegionVO vo : regionList) {
            regionBOList.add(voToBoObject(vo));
        }
        return regionBOList;
    }



    private static List<RegionVO> toRegionVOList(List<Region> regionList) {
        if (regionList == null) {
            return Collections.emptyList();
        }
        List<RegionVO> regionVOList = new ArrayList<>();
        for (Region region : regionList) {
            regionVOList.add(poToVoObject(region));
        }
        return regionVOList;
    }
}
