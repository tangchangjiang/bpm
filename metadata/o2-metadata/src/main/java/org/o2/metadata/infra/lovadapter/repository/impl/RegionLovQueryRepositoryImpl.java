package org.o2.metadata.infra.lovadapter.repository.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.AopProxy;
import org.o2.cache.util.CacheHelper;
import org.o2.core.helper.JsonHelper;
import org.o2.metadata.api.dto.RegionQueryLovInnerDTO;
import org.o2.metadata.infra.constants.MetadataCacheConstants;
import org.o2.metadata.infra.constants.O2LovConstants;
import org.o2.metadata.infra.entity.Region;
import org.o2.metadata.infra.lovadapter.repository.HzeroLovQueryRepository;
import org.o2.metadata.infra.lovadapter.repository.RegionLovQueryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * 地址值集
 *
 * @author yipeng.zhu@hand-china.com 2021-09-22
 **/
@Repository
@Slf4j
public class RegionLovQueryRepositoryImpl implements RegionLovQueryRepository, AopProxy<RegionLovQueryRepositoryImpl> {
    private final HzeroLovQueryRepository hzeroLovQueryRepository;

    private final ObjectMapper objectMapper;

    public RegionLovQueryRepositoryImpl(HzeroLovQueryRepository hzeroLovQueryRepository, ObjectMapper objectMapper) {
        this.hzeroLovQueryRepository = hzeroLovQueryRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<Region> queryRegion(Long tenantId, RegionQueryLovInnerDTO innerDTO) {
       return  queryRegionCondition(tenantId, innerDTO);
    }

    /**
     * 查询地区值集缓存
     * @param tenantId 租户ID
     * @param countryCode 国家编码
     * @return list 地区信息
     */
    public List<Region> queryRegionCache(Long tenantId, String countryCode, String lang) {
        Map<String, String> queryParam = Maps.newHashMapWithExpectedSize(2);
        queryParam.put(O2LovConstants.RegionLov.COUNTRY_CODE, countryCode);
        queryParam.put(O2LovConstants.RegionLov.ADDRESS_TYPE, O2LovConstants.RegionLov.DEFAULT_DATA);
        queryParam.put(O2LovConstants.RegionLov.LANG, lang);

        return CacheHelper.getCache(
                MetadataCacheConstants.CacheName.O2_LOV,
                MetadataCacheConstants.CacheKey.getRegionPrefix(countryCode, lang),
                tenantId, queryParam,
                this::getRegionByCountryCodeAndLang,
                false
        );
    }

    /**
     * 根据城市编码和语言查询地区值集
     *
     * @param tenantId   租户Id
     * @param queryParam 查询参数
     * @return 地区值集
     */
    protected List<Region> getRegionByCountryCodeAndLang(Long tenantId, Map<String, String> queryParam) {
        List<Map<String, Object>> maps = hzeroLovQueryRepository.queryLovValueMeaning(tenantId, O2LovConstants.AddressType.CODE, queryParam);

        List<Region> list = null;
        try {
            list = this.objectMapper.readValue(JsonHelper.objectToString(maps), new TypeReference<List<Region>>() {
            });
        } catch (Exception e) {
            log.error("region translation data error.");
        }
        return list;
    }

    /**
     * 条件查询地址
     * @param queryLov 查询条件
     * @return  地区
     */
    private List<Region> queryRegionCondition(Long tenantId, RegionQueryLovInnerDTO queryLov) {
        String countryCode = queryLov.getCountryCode();
        if (StringUtils.isEmpty(countryCode)) {
            countryCode = O2LovConstants.RegionLov.DEFAULT_COUNTRY_CODE;
        }
        String lang = queryLov.getLang();
        if (StringUtils.isEmpty(lang)) {
            lang = O2LovConstants.RegionLov.DEFAULT_LANG;
        }
        List<Region> regionList = this.queryRegionCache(tenantId, countryCode, lang);
        String regionName = queryLov.getRegionName();
        // 地区名称
        if (StringUtils.isNotEmpty(regionName)) {
            regionList = regionList.stream().filter(region ->  regionName.equals(region.getRegionName())).collect(Collectors.toList());
        }
        // 地址编码
        String regionCode = queryLov.getRegionCode();
        if (StringUtils.isNotEmpty(regionCode)) {
            regionList = regionList.stream().filter(region ->  regionCode.equals(region.getRegionCode())).collect(Collectors.toList());
        }
        // 父地区
        List<String> parentRegionCodes = queryLov.getParentRegionCodes();
        if (CollectionUtils.isNotEmpty(parentRegionCodes)) {
            regionList = regionList.stream().filter(region ->  parentRegionCodes.contains(region.getRegionCode())).collect(Collectors.toList());
        }
        // 不包含地区的编码
        List<String> notInRegionCodes = queryLov.getNotInRegionCodes();
        if (CollectionUtils.isNotEmpty(notInRegionCodes)) {
            regionList = regionList.stream().filter(region ->  !notInRegionCodes.contains(region.getRegionCode())).collect(Collectors.toList());
        }
        // 级别
        Integer levelNumber = queryLov.getLevelNumber();
        if (null != levelNumber) {
            regionList = regionList.stream().filter(region ->  levelNumber.equals(region.getLevelNumber())).collect(Collectors.toList());
        }
        List<String> regionCodes = queryLov.getRegionCodes();
        if (CollectionUtils.isNotEmpty(regionCodes)) {
            regionList = regionList.stream().filter(region ->  regionCodes.contains(region.getRegionCode())).collect(Collectors.toList());

        }
        // 地区的上一级为
        String parentRegionCode = queryLov.getParentRegionCode();
        if (StringUtils.isNotEmpty(parentRegionCode)) {
            regionList = regionList.stream().filter(region ->  parentRegionCode.equals(region.getParentRegionCode())).collect(Collectors.toList());
        }
        return regionList;
    }
}
