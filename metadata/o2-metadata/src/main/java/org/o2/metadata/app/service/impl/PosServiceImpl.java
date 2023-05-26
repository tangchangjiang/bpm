package org.o2.metadata.app.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.o2.core.helper.UserHelper;
import org.o2.metadata.api.co.PosStoreInfoCO;
import org.o2.metadata.api.dto.RegionQueryLovInnerDTO;
import org.o2.metadata.api.dto.StoreQueryDTO;
import org.o2.metadata.app.service.LovAdapterService;
import org.o2.metadata.app.service.PosService;
import org.o2.metadata.infra.constants.PosConstants;
import org.o2.metadata.infra.convertor.PosConverter;
import org.o2.metadata.infra.entity.Pos;
import org.o2.metadata.infra.entity.Region;
import org.o2.metadata.infra.redis.PosRedis;
import org.o2.multi.language.infra.util.O2RedisMultiLanguageHelper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author chao.yang05@hand-china.com 2022/4/14
 */
@Service
public class PosServiceImpl implements PosService {

    private final PosRedis posRedis;
    private final LovAdapterService lovAdapterService;
    private final O2RedisMultiLanguageHelper o2RedisMultiLanguageHelper;

    public PosServiceImpl(PosRedis posRedis,
                          LovAdapterService lovAdapterService,
                          O2RedisMultiLanguageHelper o2RedisMultiLanguageHelper) {
        this.posRedis = posRedis;
        this.lovAdapterService = lovAdapterService;
        this.o2RedisMultiLanguageHelper = o2RedisMultiLanguageHelper;
    }

    @Override
    public PosStoreInfoCO getPosPickUpInfo(String posCode, Long tenantId) {
        Pos pos = posRedis.getPosPickUpInfo(posCode, tenantId);
        if (ObjectUtils.isEmpty(pos)) {
            return null;
        }
        fillPos(tenantId, Collections.singletonList(pos));
        return PosConverter.doToCoObject(pos);
    }

    @Override
    public List<PosStoreInfoCO> getStoreInfoList(StoreQueryDTO storeQueryDTO, Long tenantId) {
        List<Pos> posInfoList = posRedis.getStoreInfoList(storeQueryDTO, tenantId);
        List<PosStoreInfoCO> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(posInfoList)) {
            return result;
        }
        fillPos(tenantId, posInfoList);
        return PosConverter.doToCoListObjects(posInfoList);
    }

    /**
     * 填充服务点信息
     *
     * @param tenantId 租户Id
     * @param posList  服务点信息
     */
    protected void fillPos(Long tenantId, List<Pos> posList) {
        if (CollectionUtils.isEmpty(posList)) {
            return;
        }
        List<String> regionCodes = new ArrayList<>();
        for (Pos pos : posList) {
            o2RedisMultiLanguageHelper.getMultiLang(pos, PosConstants.RedisKey.getPosDetailMultiKey(tenantId, pos.getPosCode()));
            regionCodes.add(pos.getRegionCode());
            regionCodes.add(pos.getCityCode());
            regionCodes.add(pos.getDistrictCode());
        }
        RegionQueryLovInnerDTO innerDTO = new RegionQueryLovInnerDTO();
        innerDTO.setRegionCodes(regionCodes);
        innerDTO.setCountryCode(posList.get(0).getCountryCode());
        innerDTO.setLang(UserHelper.getLang());
        List<Region> regions = lovAdapterService.queryRegion(tenantId, innerDTO);
        Map<String, Region> regionMap = regions.stream().collect(Collectors.toMap(Region::getRegionCode, Function.identity(), (s1, s2) -> s2));
        for (Pos pos : posList) {
            Region region = regionMap.getOrDefault(pos.getRegionCode(), new Region());
            Region city = regionMap.getOrDefault(pos.getCityCode(), new Region());
            Region district = regionMap.getOrDefault(pos.getDistrictCode(), new Region());
            pos.setRegionName(region.getRegionName());
            pos.setCityName(city.getRegionName());
            pos.setDistrictName(district.getRegionName());
        }
    }
}
