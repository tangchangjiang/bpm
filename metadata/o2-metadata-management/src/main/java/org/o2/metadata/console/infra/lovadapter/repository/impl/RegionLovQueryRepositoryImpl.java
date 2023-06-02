package org.o2.metadata.console.infra.lovadapter.repository.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import io.choerodon.mybatis.helper.LanguageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.AopProxy;
import org.hzero.core.base.BaseConstants;
import org.o2.cache.util.CacheHelper;
import org.o2.core.helper.JsonHelper;
import org.o2.metadata.console.api.co.PageCO;
import org.o2.metadata.console.api.dto.RegionQueryLovInnerDTO;
import org.o2.metadata.console.app.bo.RegionNameMatchBO;
import org.o2.metadata.console.infra.constant.MetadataCacheConstants;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.constant.O2LovConstants;
import org.o2.metadata.console.infra.entity.Region;
import org.o2.metadata.console.infra.lovadapter.repository.HzeroLovQueryRepository;
import org.o2.metadata.console.infra.lovadapter.repository.RegionLovQueryRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 地址值集
 *
 * @author yipeng.zhu@hand-china.com 2021-09-22
 **/
@Repository
@Slf4j
public class RegionLovQueryRepositoryImpl implements RegionLovQueryRepository, AopProxy<RegionLovQueryRepositoryImpl> {
    private HzeroLovQueryRepository hzeroLovQueryRepository;

    private ObjectMapper objectMapper;

    public RegionLovQueryRepositoryImpl(HzeroLovQueryRepository hzeroLovQueryRepository, ObjectMapper objectMapper) {
        this.hzeroLovQueryRepository = hzeroLovQueryRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<Region> queryRegion(Long tenantId, RegionQueryLovInnerDTO innerDTO) {
        return queryRegionCondition(tenantId, innerDTO);
    }

    /**
     * @param tenantId 租户ID
     * @param page     page 页码
     * @param size     大小
     * @param innerDTO 查询参数
     * @return 分页
     */
    @Override
    public PageCO<Region> queryRegionPage(Long tenantId, Integer page, Integer size, RegionQueryLovInnerDTO innerDTO) {
        List<Region> regionList = queryRegionCondition(tenantId, innerDTO);
        if (regionList.isEmpty()) {
            return new PageCO<>();
        }
        page = page < 0 ? 0 : page;
        List<Region> collect = regionList.stream().skip((page) * Long.parseLong(String.valueOf(size))).limit(size).collect(Collectors.toList());
        return new PageCO<>(collect, page, size, regionList.size());
    }

    @Override
    public List<Region> fuzzyMatching(Long tenantId, String countryCode, String lang, List<RegionNameMatchBO> queryList) {
        if (StringUtils.isEmpty(countryCode)) {
            countryCode = O2LovConstants.RegionLov.DEFAULT_COUNTRY_CODE;
        }
        if (StringUtils.isEmpty(lang)) {
            lang = O2LovConstants.RegionLov.DEFAULT_LANG;
        }
        log.info("address query -> queryRegionCache start, time:{}", System.currentTimeMillis());
        List<Region> regionList = this.queryRegionCache(tenantId, countryCode, lang, null);
        log.info("address query -> queryRegionCache end, time:{}", System.currentTimeMillis());
        if (regionList.isEmpty() || queryList.isEmpty()) {
            return new ArrayList<>();
        }
        Iterator<Region> iteratorRegion = regionList.iterator();
        // 匹配结果
        List<Region> result = new ArrayList<>();
        while (iteratorRegion.hasNext()) {
            if (queryList.isEmpty()) {
                return result;
            }
            Region region = iteratorRegion.next();
            String name = region.getRegionName();
            int levelNumber = region.getLevelNumber();
            // 更据名称模糊匹配
            for (RegionNameMatchBO bo : queryList) {
                // 全模糊匹配 %名称%
                boolean nameFlag = name.contains(bo.getRegionName());
                boolean levelFlag = Objects.equals(bo.getLevelNumber(), levelNumber);
                if (nameFlag && levelFlag) {
                    // 匹配的数据
                    Region entry = new Region();
                    entry.setRegionName(name);
                    entry.setExternalCode(bo.getExternalCode());
                    entry.setExternalName(bo.getExternalName());
                    entry.setRegionCode(region.getRegionCode());
                    entry.setLevelNumber(region.getLevelNumber());
                    entry.setParentRegionCode(region.getParentRegionCode());
                    result.add(entry);
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 查询地区值集缓存
     *
     * @param tenantId    租户ID
     * @param countryCode 国家编码
     * @return list 地区信息
     */
    public List<Region> queryRegionCache(Long tenantId, String countryCode, String lang, Map<String, String> paramMap) {
        Map<String, String> queryParam = Maps.newHashMapWithExpectedSize(2);
        queryParam.put(O2LovConstants.RegionLov.COUNTRY_CODE, countryCode);
        queryParam.put(O2LovConstants.RegionLov.ADDRESS_TYPE, O2LovConstants.RegionLov.DEFAULT_DATA);
        queryParam.put(O2LovConstants.RegionLov.LANG, lang);
        queryParam.put(O2LovConstants.RegionLov.TENANT_ID, String.valueOf(tenantId));
        if (MapUtils.isNotEmpty(paramMap)) {
            queryParam.putAll(paramMap);
        }
        return CacheHelper.getCache(
                MetadataCacheConstants.CacheName.O2_LOV,
                MetadataCacheConstants.KeyPrefix.getHzeroRegionPrefix(countryCode, lang),
                tenantId, queryParam,
                this::getRegionByCountryAndLang,
                false
        );
    }

    /**
     * 通过城市编码和语言查询地区值集
     *
     * @param tenantId   租户Id
     * @param queryParam 查询参数
     * @return 地区值集
     */
    protected List<Region> getRegionByCountryAndLang(Long tenantId, Map<String, String> queryParam) {
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
     *
     * @param queryLov 查询条件
     * @return 地区
     */
    private List<Region> queryRegionCondition(Long tenantId, RegionQueryLovInnerDTO queryLov) {
        String countryCode = queryLov.getCountryCode();
        String lang = queryLov.getLang();
        if (StringUtils.isEmpty(lang)) {
            lang = LanguageHelper.language();
        }
        log.info("address query -> queryRegionCache start, time:{}", System.currentTimeMillis());
        // 设置sql值集查询入参map
        Map<String, String> paramMap = new HashMap<>();
        if (StringUtils.isNotBlank(queryLov.getRegionCode())) {
            paramMap.put(O2LovConstants.RegionLov.REGION_CODE_LIST, queryLov.getRegionCode());
        }
        if (StringUtils.isNotBlank(queryLov.getParentRegionCode())) {
            paramMap.put(O2LovConstants.RegionLov.PARENT_REGION_CODES, queryLov.getParentRegionCode());
        }
        // 查询地区sql值集时，限制入参中地区编码的数量：如果超过设置数量，则不作为入参，选择捞取H0的所有数据进行匹配的方式；避免入参过大，请求失败
        if (CollectionUtils.isNotEmpty(queryLov.getRegionCodes()) && queryLov.getRegionCodes().size() < MetadataConstants.QueryDataNum.REGION_QUERY_NUM) {
            paramMap.put(O2LovConstants.RegionLov.REGION_CODE_LIST, String.join(BaseConstants.Symbol.COMMA, queryLov.getRegionCodes()));
        }
        if (CollectionUtils.isNotEmpty(queryLov.getParentRegionCodes()) && queryLov.getParentRegionCodes().size() < MetadataConstants.QueryDataNum.REGION_QUERY_NUM) {
            paramMap.put(O2LovConstants.RegionLov.PARENT_REGION_CODES, String.join(BaseConstants.Symbol.COMMA, queryLov.getParentRegionCodes()));
        }
        // 不包含地区的编码
        List<String> notInRegionCodes = queryLov.getNotInRegionCodes();
        if (CollectionUtils.isNotEmpty(notInRegionCodes)) {
            paramMap.put(O2LovConstants.RegionLov.NOT_IN_REGION_CODE, String.join(BaseConstants.Symbol.COMMA, notInRegionCodes));
        }
        if (null != queryLov.getLevelNumber()) {
            paramMap.put(O2LovConstants.RegionLov.LEVEL_NUMBER, String.valueOf(queryLov.getLevelNumber()));
        }
        paramMap.put(O2LovConstants.RegionLov.LEVEL_PATH, queryLov.getLevelPath());
        paramMap.put(O2LovConstants.RegionLov.REGION_NAME, queryLov.getRegionName());
        List<Region> regionList = this.queryRegionCache(tenantId, countryCode, lang, paramMap);
        // 父地区
        List<String> parentRegionCodes = queryLov.getParentRegionCodes();
        if (CollectionUtils.isNotEmpty(parentRegionCodes) && StringUtils.isBlank(paramMap.get(O2LovConstants.RegionLov.PARENT_REGION_CODES))) {
            regionList = regionList.stream().filter(region -> parentRegionCodes.contains(region.getParentRegionCode())).collect(Collectors.toList());
        }
        // 地区匹配，如果paramMap中regionCodeList参数为空，则是直接将地区编码作为入参去查询值集进行匹配，不需要再次过滤
        List<String> regionCodes = queryLov.getRegionCodes();
        if (CollectionUtils.isNotEmpty(regionCodes) && StringUtils.isBlank(paramMap.get(O2LovConstants.RegionLov.REGION_CODE_LIST))) {
            regionList = regionList.stream().filter(region -> regionCodes.contains(region.getRegionCode())).collect(Collectors.toList());
        }
        log.info("address query -> queryRegionCache end, time:{}", System.currentTimeMillis());

        return regionList;
    }
}
