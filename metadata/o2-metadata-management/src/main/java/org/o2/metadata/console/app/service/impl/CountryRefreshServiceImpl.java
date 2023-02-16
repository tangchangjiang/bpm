package org.o2.metadata.console.app.service.impl;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.o2.core.helper.JsonHelper;
import org.o2.file.config.properties.O2FileProperties;
import org.o2.file.helper.O2FileHelper;
import org.o2.metadata.console.api.dto.CountryRefreshDTO;
import org.o2.metadata.console.app.bo.CountryRefreshBO;
import org.o2.metadata.console.app.service.CountryRefreshService;
import org.o2.metadata.console.app.service.lang.MultiLangService;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.constant.O2LovConstants;
import org.o2.metadata.console.infra.lovadapter.repository.HzeroLovQueryRepository;
import org.o2.metadata.console.infra.redis.RegionRedis;
import org.o2.metadata.console.infra.strategy.BusinessTypeStrategyDispatcher;
import org.o2.metadata.domain.staticresource.domain.StaticResourceConfigDO;
import org.o2.metadata.domain.staticresource.domain.StaticResourceSaveDO;
import org.o2.metadata.domain.staticresource.service.StaticResourceBusinessService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 国家刷新service
 *
 * @author rui.ling@hand-china.com 2023/02/15
 */
@Slf4j
@Service
public class CountryRefreshServiceImpl implements CountryRefreshService {

    private final HzeroLovQueryRepository hzeroLovQueryRepository;
    private final MultiLangService multiLangService;
    private final RegionRedis regionRedis;
    private final O2FileProperties fileProperties;

    public CountryRefreshServiceImpl(HzeroLovQueryRepository hzeroLovQueryRepository,
                                     MultiLangService multiLangService,
                                     RegionRedis regionRedis,
                                     O2FileProperties fileProperties) {
        this.hzeroLovQueryRepository = hzeroLovQueryRepository;
        this.multiLangService = multiLangService;
        this.regionRedis = regionRedis;
        this.fileProperties = fileProperties;
    }

    private static String trimDomainPrefix(String resourceUrl) {
        if (StringUtils.isBlank(resourceUrl)) {
            return "";
        }

        String[] httpSplits = resourceUrl.split(BaseConstants.Symbol.DOUBLE_SLASH);
        if (httpSplits.length < 2) {
            return resourceUrl;
        }

        String domainSuffix = httpSplits[1];
        return domainSuffix.substring(domainSuffix.indexOf(BaseConstants.Symbol.SLASH));
    }

    private static String domainPrefix(String resourceUrl) {
        if (StringUtils.isBlank(resourceUrl)) {
            return "";
        }
        String[] httpSplits = resourceUrl.split(BaseConstants.Symbol.DOUBLE_SLASH);
        if (httpSplits.length < 2) {
            return "";
        }
        return resourceUrl.substring(0, resourceUrl.indexOf(BaseConstants.Symbol.SLASH, resourceUrl.indexOf(BaseConstants.Symbol.SLASH) + 2));
    }

    @Override
    public void refreshCountryInfoFile(CountryRefreshDTO countryRefreshDTO) {
        final Long tenantId = countryRefreshDTO.getTenantId();
        final String lang = countryRefreshDTO.getLang();
        final String businessTypeCode = countryRefreshDTO.getBusinessTypeCode();

        if (log.isDebugEnabled()) {
            log.debug("refresh country info, tenantId:[{}}, lang:[{}], businessType:[{}}", tenantId, lang, businessTypeCode);
        }

        // 根据业务类型+目标class获取处理方法
        StaticResourceBusinessService staticResourceBusinessService = BusinessTypeStrategyDispatcher.getService(businessTypeCode,
                StaticResourceBusinessService.class);

        // 查询静态资源配置
        StaticResourceConfigDO staticResourceConfigDO = staticResourceBusinessService.getStaticResourceConfig(tenantId,
                MetadataConstants.StaticResourceCode.O2MD_COUNTRY);

        // 静态资源路径map
        Map<String, String> resourceUrlMap = buildUrlByConfig(tenantId, lang, staticResourceConfigDO);
        if (MapUtils.isEmpty(resourceUrlMap)) {
            return;
        }

        // 静态资源集合
        List<StaticResourceSaveDO> staticResourceSaveDOList = buildStaticResourceSaveDO(tenantId, staticResourceConfigDO, resourceUrlMap);

        // 保存静态资源
        staticResourceBusinessService.saveStaticResource(tenantId, staticResourceSaveDOList);
    }

    private Map<String, String> buildUrlByConfig(Long tenantId, String lang, StaticResourceConfigDO staticResourceConfigDO) {
        // 静态资源配置
        String uploadFolder = staticResourceConfigDO.getUploadFolder();
        Integer differentLangFlag = staticResourceConfigDO.getDifferentLangFlag();

        // 上传多语言文件
        return multiLangService.staticResourceUpload(tenantId, lang, differentLangFlag,
                language -> uploadFile(tenantId, language, uploadFolder,
                        buildCountryRefreshBO(tenantId, language)));
    }

    /**
     * 构建文件信息
     *
     * @param tenantId 租户ID
     * @param lang     语言
     * @return 国家刷新BO
     */
    private List<CountryRefreshBO> buildCountryRefreshBO(Long tenantId,
                                                         String lang) {
        // 构建查询参数
        Map<String, String> queryParam = Maps.newHashMapWithExpectedSize(BaseConstants.Digital.TWO);
        queryParam.put(O2LovConstants.CountryLov.LANG, lang);
        queryParam.put(O2LovConstants.CountryLov.TENANT_ID, String.valueOf(tenantId));

        // 查询值集-HPFM.COUNTRY
        List<Map<String, Object>> lovValueMapList = hzeroLovQueryRepository.queryLovValueMeaning(tenantId,
                MetadataConstants.CountryLov.LOV, queryParam);
        if (CollectionUtils.isEmpty(lovValueMapList)) {
            queryParam.put(O2LovConstants.CountryLov.TENANT_ID, String.valueOf(BaseConstants.DEFAULT_TENANT_ID));
            lovValueMapList = hzeroLovQueryRepository.queryLovValueMeaning(tenantId,
                    MetadataConstants.CountryLov.LOV, queryParam);
        }
        if (CollectionUtils.isEmpty(lovValueMapList)) {
            return Collections.emptyList();
        }

        List<CountryRefreshBO> countryRefreshBOList = lovValueMapList.stream().map(lovValueMap -> {
            String json = JsonHelper.mapToString(lovValueMap);
            return JsonHelper.stringToObject(json, CountryRefreshBO.class);
        }).collect(Collectors.toList());

        // 查询Redis 地区静态资源路径
        List<String> countryCodeList = countryRefreshBOList.stream().map(CountryRefreshBO::getCountryCode).collect(Collectors.toList());
        Map<String, String> regionUrlMap = regionRedis.queryRegionUrlFromRedis(tenantId, lang, countryCodeList);

        // 组装地区静态资源路径
        countryRefreshBOList.forEach(countryRefreshBO -> {
            String countryCode = countryRefreshBO.getCountryCode();
            countryRefreshBO.setRegionResourceUrl(regionUrlMap.get(countryCode));
        });

        return countryRefreshBOList;
    }

    /**
     * 上传文件
     *
     * @param tenantId             租户ID
     * @param lang                 语言
     * @param uploadFolder         上传路径
     * @param countryRefreshBOList 国家刷新BO
     * @return 文件路径
     */
    private String uploadFile(final Long tenantId,
                              final String lang,
                              final String uploadFolder,
                              final List<CountryRefreshBO> countryRefreshBOList) {
        final String directory = Optional.ofNullable(uploadFolder)
                .orElse(Joiner.on(BaseConstants.Symbol.SLASH).skipNulls()
                        .join(MetadataConstants.Path.FILE, MetadataConstants.Path.COUNTRY, lang));
        final String fileName = MetadataConstants.Path.COUNTRY_FILE_NAME + MetadataConstants.FileSuffix.JSON;

        if (log.isDebugEnabled()) {
            log.debug("country upload directory:[{}], fileName:[{}]", directory, fileName);
        }

        // 上传文件
        return O2FileHelper.uploadFile(tenantId, directory, fileName, MetadataConstants.PublicLov.JSON_TYPE,
                JsonHelper.objectToString(countryRefreshBOList).getBytes());
    }

    /**
     * 构建静态资源保存DO
     *
     * @param tenantId               租户ID
     * @param staticResourceConfigDO 静态资源配置
     * @param resourceUrlMap         资源路径map
     * @return 静态资源保存DO
     */
    private List<StaticResourceSaveDO> buildStaticResourceSaveDO(Long tenantId,
                                                                 StaticResourceConfigDO staticResourceConfigDO,
                                                                 Map<String, String> resourceUrlMap) {
        List<StaticResourceSaveDO> staticResourceSaveDOList = new ArrayList<>(resourceUrlMap.size());
        resourceUrlMap.forEach((lang, resourceUrl) -> staticResourceSaveDOList.add(buildStaticResourceSaveDO(tenantId, lang, resourceUrl, staticResourceConfigDO)));
        return staticResourceSaveDOList;
    }

    /**
     * 构建静态资源保存DO
     *
     * @param tenantId               租户ID
     * @param lang                   语言
     * @param staticResourceConfigDO 静态资源配置
     * @param resourceUrl            资源路径
     * @return 静态资源保存DO
     */
    private StaticResourceSaveDO buildStaticResourceSaveDO(Long tenantId,
                                                           String lang,
                                                           String resourceUrl,
                                                           StaticResourceConfigDO staticResourceConfigDO) {

        String host = domainPrefix(resourceUrl);
        String url = trimDomainPrefix(resourceUrl);

        StaticResourceSaveDO staticResourceSaveDO = new StaticResourceSaveDO();
        staticResourceSaveDO.setResourceCode(staticResourceConfigDO.getResourceCode());
        staticResourceSaveDO.setDescription(staticResourceConfigDO.getDescription());
        staticResourceSaveDO.setResourceLevel(staticResourceConfigDO.getResourceLevel());
        staticResourceSaveDO.setEnableFlag(MetadataConstants.StaticResourceConstants.ENABLE_FLAG);
        staticResourceSaveDO.setLang(lang);
        staticResourceSaveDO.setResourceUrl(url);
        staticResourceSaveDO.setResourceHost(host);
        staticResourceSaveDO.setTenantId(tenantId);
        return staticResourceSaveDO;
    }

}
