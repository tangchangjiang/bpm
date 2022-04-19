package org.o2.metadata.app.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.o2.metadata.api.co.PosPickUpInfoCO;
import org.o2.metadata.api.co.PosStoreInfoCO;
import org.o2.metadata.api.dto.RegionQueryLovInnerDTO;
import org.o2.metadata.api.dto.StoreQueryDTO;
import org.o2.metadata.app.service.LovAdapterService;
import org.o2.metadata.app.service.PosService;
import org.o2.metadata.infra.convertor.PosConverter;
import org.o2.metadata.infra.entity.Pos;
import org.o2.metadata.infra.entity.Region;
import org.o2.metadata.infra.redis.PosRedis;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chao.yang05@hand-china.com 2022/4/14
 */
@Service
public class PosServiceImpl implements PosService {

    private final PosRedis posRedis;
    private final LovAdapterService lovAdapterService;

    public PosServiceImpl(PosRedis posRedis,
                          LovAdapterService lovAdapterService) {
        this.posRedis = posRedis;
        this.lovAdapterService = lovAdapterService;
    }

    @Override
    public PosPickUpInfoCO getPosPickUpInfo(String posCode, Long tenantId) {
        Pos pos = posRedis.getPosPickUpInfo(posCode, tenantId);
        if (ObjectUtils.isEmpty(pos)) {
            return null;
        }
        // 查询地区值集
        Map<String,String> map = new HashMap<>();
        RegionQueryLovInnerDTO regionQueryLovInnerDTO = new RegionQueryLovInnerDTO();
        regionQueryLovInnerDTO.setTenantId(tenantId);
        List<Region> regionList = lovAdapterService.queryRegion(tenantId, regionQueryLovInnerDTO);
        if (!regionList.isEmpty()) {
            for (Region region : regionList) {
                map.put(region.getRegionCode(), region.getRegionName());
            }
            pos.setRegionName(map.get(pos.getRegionCode()));
            pos.setCityName(map.get(pos.getCityCode()));
            pos.setDistrictName(map.get(pos.getDistrictCode()));
        }
        return PosConverter.doToPickUpInfoCoObject(pos);
    }

    @Override
    public List<PosStoreInfoCO> getStoreInfoList(StoreQueryDTO storeQueryDTO, Long tenantId) {
        List<Pos> posInfoList = posRedis.getStoreInfoList(storeQueryDTO, tenantId);
        if (CollectionUtils.isEmpty(posInfoList)) {
            return null;
        }
        // 查询地区值集
        Map<String,String> map = new HashMap<>();
        RegionQueryLovInnerDTO regionQueryLovInnerDTO = new RegionQueryLovInnerDTO();
        regionQueryLovInnerDTO.setTenantId(tenantId);
        List<Region> regionList = lovAdapterService.queryRegion(tenantId, regionQueryLovInnerDTO);
        if (!regionList.isEmpty()) {
            for (Region region : regionList) {
                map.put(region.getRegionCode(), region.getRegionName());
            }
            for (Pos pos : posInfoList) {
                pos.setRegionName(map.get(pos.getRegionCode()));
                pos.setCityName(map.get(pos.getCityCode()));
                pos.setDistrictName(map.get(pos.getDistrictCode()));
            }
        }
        return PosConverter.doToCoListObjects(posInfoList);
    }
}
