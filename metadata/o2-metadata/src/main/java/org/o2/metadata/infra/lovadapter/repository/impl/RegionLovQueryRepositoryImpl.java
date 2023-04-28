package org.o2.metadata.infra.lovadapter.repository.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.AopProxy;
import org.hzero.core.base.BaseConstants;
import org.o2.cache.util.CacheHelper;
import org.o2.core.helper.JsonHelper;
import org.o2.metadata.api.dto.RegionQueryLovInnerDTO;
import org.o2.metadata.infra.constants.MetadataCacheConstants;
import org.o2.metadata.infra.constants.O2LovConstants;
import org.o2.metadata.infra.entity.Region;
import org.o2.metadata.infra.lovadapter.repository.HzeroLovQueryRepository;
import org.o2.metadata.infra.lovadapter.repository.RegionLovQueryRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<Region> queryRegionCache(Long tenantId, String countryCode, String lang, Map<String, String> paramMap) {
        Map<String, String> queryParam = Maps.newHashMapWithExpectedSize(2);
        queryParam.put(O2LovConstants.RegionLov.COUNTRY_CODE, countryCode);
        queryParam.put(O2LovConstants.RegionLov.ADDRESS_TYPE, O2LovConstants.RegionLov.DEFAULT_DATA);
        queryParam.put(O2LovConstants.RegionLov.LANG, lang);
        queryParam.putAll(paramMap);

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
        Map<String, String> paramMap = new HashMap<>();
        if (StringUtils.isNotBlank(queryLov.getRegionCode())) {
            paramMap.put(O2LovConstants.RegionLov.REGION_CODE_LIST, queryLov.getRegionCode());
        }
        if (CollectionUtils.isNotEmpty(queryLov.getRegionCodes())) {
            paramMap.put(O2LovConstants.RegionLov.REGION_CODE_LIST, String.join(BaseConstants.Symbol.COMMA, queryLov.getRegionCodes()));
        }
        if (StringUtils.isNotBlank(queryLov.getParentRegionCode())) {
            paramMap.put(O2LovConstants.RegionLov.PARENT_REGION_CODES, queryLov.getParentRegionCode());
        }
        if (CollectionUtils.isNotEmpty(queryLov.getParentRegionCodes())) {
            paramMap.put(O2LovConstants.RegionLov.PARENT_REGION_CODES, String.join(BaseConstants.Symbol.COMMA, queryLov.getRegionCodes()));
        }
        // 不包含地区的编码
        List<String> notInRegionCodes = queryLov.getNotInRegionCodes();
        if (CollectionUtils.isNotEmpty(notInRegionCodes)) {
            paramMap.put(O2LovConstants.RegionLov.NOT_IN_REGION_CODE, String.join(BaseConstants.Symbol.COMMA, notInRegionCodes));
        }
        if (null != queryLov.getLevelNumber()) {
            paramMap.put(O2LovConstants.RegionLov.LEVEL_NUMBER, String.valueOf(queryLov.getLevelNumber()));
        }
        paramMap.put(O2LovConstants.RegionLov.REGION_NAME, queryLov.getRegionName());
        List<Region> regionList = this.queryRegionCache(tenantId, countryCode, lang, paramMap);
        return regionList;
    }
}
