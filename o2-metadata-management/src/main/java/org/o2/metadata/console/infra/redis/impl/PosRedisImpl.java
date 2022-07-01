package org.o2.metadata.console.infra.redis.impl;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.o2.core.helper.JsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.console.api.dto.RegionQueryLovInnerDTO;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.constant.PosConstants;
import org.o2.metadata.console.infra.entity.PosInfo;
import org.o2.metadata.console.infra.entity.Region;
import org.o2.metadata.console.infra.redis.PosRedis;
import org.o2.metadata.console.infra.repository.PosRepository;
import org.o2.metadata.console.infra.repository.RegionRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 服务店
 *
 * @author chao.yang05@hand-china.com 2022/4/13
 */
@Component
@Slf4j
public class PosRedisImpl implements PosRedis {

    private final PosRepository posRepository;
    private final RedisCacheClient redisCacheClient;
    private final RegionRepository regionRepository;

    public PosRedisImpl(final PosRepository posRepository,
                        final RedisCacheClient redisCacheClient,
                        final RegionRepository regionRepository) {
        this.posRepository = posRepository;
        this.redisCacheClient = redisCacheClient;
        this.regionRepository = regionRepository;
    }

    @Override
    public void syncPosToRedis(List<String> posCodes, Long tenantId) {
        List<PosInfo> posInfos = posRepository.listPosInfoByCode(null, posCodes, MetadataConstants.PosType.STORE, tenantId);
        List<PosInfo> pickUpInfoList = posInfos.stream()
                .filter(pos -> pos.getLongitude() != null && pos.getLatitude() != null)
                .collect(Collectors.toList());
        if(CollectionUtils.isEmpty(pickUpInfoList)) {
            return;
        }
        // 设置服务点省市区名称
        setPosAddress(pickUpInfoList, tenantId);
        // 同步门店信息至Redis
        redisCacheClient.executePipelined(new SessionCallback<Object>() {
            @SuppressWarnings({"unchecked"})
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String posAllStoreKey = PosConstants.RedisKey.getPosAllStoreKey(tenantId);
                String posDetailKey = PosConstants.RedisKey.getPosDetailKey(tenantId);
                Map<String, String> storeMap = new HashMap<>();
                Set<String> posCodeSet = pickUpInfoList.stream().map(PosInfo::getPosCode).collect(Collectors.toSet());
                operations.opsForSet().add(posAllStoreKey, posCodeSet.toArray());
                for (PosInfo pos : pickUpInfoList) {
                    storeMap.put(pos.getPosCode(), JsonHelper.objectToString(pos));
                    if (StringUtils.isNotBlank(pos.getRegionCode()) && StringUtils.isNotBlank(pos.getCityCode())) {
                        String cityStoreKey = PosConstants.RedisKey.getPosCityStoreKey(tenantId, pos.getRegionCode(), pos.getCityCode());
                        operations.opsForSet().add(cityStoreKey, pos.getPosCode());
                    }
                    if (StringUtils.isNotBlank(pos.getDistrictCode())) {
                        String districtStoreKey = PosConstants.RedisKey.getPosDistrictStoreKey(tenantId, pos.getRegionCode(), pos.getCityCode(), pos.getDistrictCode());
                        operations.opsForSet().add(districtStoreKey, pos.getPosCode());
                    }
                }
                operations.opsForHash().putAll(posDetailKey, storeMap);
                return null;
            }
        });

    }

    @Override
    public void updatePosDetail(List<Long> posIds, List<String> posCodes, Long tenantId) {
        List<PosInfo> posInfos = posRepository.listPosInfoByCode(posIds, null, MetadataConstants.PosType.STORE, tenantId);
        List<PosInfo> pickUpInfoList = posInfos.stream()
                .filter(pos -> pos.getLongitude() != null && pos.getLatitude() != null)
                .collect(Collectors.toList());
        if(CollectionUtils.isEmpty(pickUpInfoList)) {
            return;
        }
        // 设置门店地址名称
        setPosAddress(pickUpInfoList, tenantId);
        Map<String, String> posMap = new HashMap<>();
        for (PosInfo posInfo : posInfos) {
            posMap.put(posInfo.getPosCode(), JsonHelper.objectToString(posInfo));
        }
        String posDetailKey = PosConstants.RedisKey.getPosDetailKey(tenantId);
        redisCacheClient.opsForHash().putAll(posDetailKey, posMap);
    }

    /**
     * 设置服务点地址名称
     *
     * @param pickUpInfoList 服务点
     * @param tenantId 租户Id
     */
    private void setPosAddress(List<PosInfo> pickUpInfoList, Long tenantId) {
        List<String> regionCodes = new ArrayList<>();
        for (PosInfo posInfo : pickUpInfoList) {
            regionCodes.add(posInfo.getCityCode());
            regionCodes.add(posInfo.getDistrictCode());
            regionCodes.add(posInfo.getRegionCode());
        }
        RegionQueryLovInnerDTO dto = new RegionQueryLovInnerDTO();
        dto.setRegionCodes(regionCodes);
        List<Region> regionList = regionRepository.listRegionLov(dto,tenantId);
        Map<String,Region> regionMap = Maps.newHashMapWithExpectedSize(regionList.size());
        for (Region region : regionList) {
            regionMap.put(region.getRegionCode(),region);
        }
        for (PosInfo posInfo : pickUpInfoList) {
            //市
            String cityCode = posInfo.getCityCode();
            Region city = regionMap.get(cityCode);
            if (null != city) {
                posInfo.setCityName(city.getRegionName());
                posInfo.setCountryName(city.getCountryName());
                posInfo.setCountryCode(city.getCountryCode());
            }
            //区
            String districtCode = posInfo.getDistrictCode();
            Region district = regionMap.get(districtCode);
            if (null != district) {
                posInfo.setDistrictName(district.getRegionName());
            }
            //省
            String regionCode = posInfo.getRegionCode();
            Region region = regionMap.get(regionCode);
            if (null != region) {
                posInfo.setRegionName(region.getRegionName());
            }
        }
    }
}
