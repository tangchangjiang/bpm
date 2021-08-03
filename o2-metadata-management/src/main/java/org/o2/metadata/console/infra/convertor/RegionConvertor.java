package org.o2.metadata.console.infra.convertor;

import org.o2.metadata.console.api.vo.RegionVO;
import org.o2.metadata.console.infra.entity.Region;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 地区
 *
 * @author yipeng.zhu@hand-china.com 2021-08-03
 **/
public class RegionConvertor {
    public static RegionVO poToVoObject(Region region){

        if (region == null) {
            return null;
        }
        RegionVO regionVO = new RegionVO();
        regionVO.setRegionId(region.getRegionId());
        regionVO.setRegionCode(region.getRegionCode());
        regionVO.setRegionName(region.getRegionName());
        regionVO.setCountryId(region.getCountryId());
        regionVO.setParentRegionId(region.getParentRegionId());
        regionVO.setLevelPath(region.getLevelPath());
        regionVO.setEnabledFlag(region.getEnabledFlag());
        regionVO.setChildren(region.getChildren());
        regionVO.setAreaCode(region.getAreaCode());
        regionVO.setAreaMeaning(region.getAreaMeaning());
        regionVO.setTenantId(region.getTenantId());
        regionVO.setCountryCode(region.getCountryCode());
        regionVO.setCountryName(region.getCountryName());
        regionVO.setCreationDate(region.getCreationDate());
        regionVO.setCreatedBy(region.getCreatedBy());
        regionVO.setLastUpdateDate(region.getLastUpdateDate());
        regionVO.setLastUpdatedBy(region.getLastUpdatedBy());
        regionVO.setObjectVersionNumber(region.getObjectVersionNumber());
        regionVO.setTableId(region.getTableId());
        regionVO.set_tls(region.get_tls());
        regionVO.set_status(region.get_status());
        regionVO.setFlex(region.getFlex());
        return regionVO;
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
}
