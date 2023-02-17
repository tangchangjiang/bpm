package org.o2.metadata.console.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.file.helper.O2FileHelper;
import org.o2.metadata.console.api.dto.RegionQueryLovInnerDTO;
import org.o2.metadata.console.api.vo.RegionCacheVO;
import org.o2.metadata.console.app.bo.RegionCacheBO;
import org.o2.metadata.console.app.service.O2SiteRegionFileService;
import org.o2.metadata.console.app.service.lang.MultiLangService;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.convertor.RegionConverter;
import org.o2.metadata.console.infra.entity.Region;
import org.o2.metadata.console.infra.redis.RegionRedis;
import org.o2.metadata.console.infra.repository.RegionRepository;
import org.o2.metadata.console.infra.strategy.BusinessTypeStrategyDispatcher;
import org.o2.metadata.domain.staticresource.domain.StaticResourceConfigDO;
import org.o2.metadata.domain.staticresource.domain.StaticResourceSaveDO;
import org.o2.metadata.domain.staticresource.service.StaticResourceBusinessService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 地区静态文件
 *
 * @author yipeng.zhu@hand-china.com 2020-05-20 11:09
 **/
@Service
@Slf4j
public class O2SiteRegionFileServiceImpl implements O2SiteRegionFileService {

    private final RegionRepository regionRepository;
    private final MultiLangService multiLangService;
    private final RegionRedis regionRedis;

    public O2SiteRegionFileServiceImpl(RegionRepository regionRepository,
                                       MultiLangService multiLangService,
                                       RegionRedis regionRedis) {
        this.regionRepository = regionRepository;
        this.multiLangService = multiLangService;
        this.regionRedis = regionRedis;
    }

    @Override
    public void createRegionStaticFile(final RegionCacheVO regionCacheVO, final String resourceOwner, final String businessTypeCode) {
        final Long tenantId = regionCacheVO.getTenantId();
        final String countryCode = regionCacheVO.getCountryCode();
        final String lang = regionCacheVO.getLang();
        log.info("static params are : {},{}", tenantId, countryCode);

        // 根据业务类型+目标class获取处理方法
        StaticResourceBusinessService staticResourceBusinessService = BusinessTypeStrategyDispatcher.getService(businessTypeCode,
                StaticResourceBusinessService.class);
        // 查询静态资源配置信息
        final StaticResourceConfigDO staticResourceConfigDO = staticResourceBusinessService.getStaticResourceConfig(tenantId,
                MetadataConstants.StaticResourceCode.O2MD_REGION);

        // 使用map存储resourceUrl,key为langCode、value为resourceUrl
        Map<String, String> resourceUrlMap = buildUrlByConfig(tenantId, lang, countryCode, staticResourceConfigDO);
        if (MapUtils.isEmpty(resourceUrlMap)){
            return;
        }

        //  更新静态文件资源表
        List<StaticResourceSaveDO> saveDTOList = buildStaticResourceSaveDTO(tenantId, resourceUrlMap, resourceOwner, staticResourceConfigDO);
        staticResourceBusinessService.saveStaticResource(tenantId, saveDTOList);

        // 保存Redis
        regionRedis.saveRegionUrlToRedis(tenantId, countryCode, resourceUrlMap);
    }

    private Map<String, String> buildUrlByConfig(Long tenantId, String lang, String countryCode, StaticResourceConfigDO staticResourceConfigDO) {
        // 静态资源配置
        String uploadFolder = staticResourceConfigDO.getUploadFolder();
        Integer differentLangFlag = staticResourceConfigDO.getDifferentLangFlag();
        return multiLangService.staticResourceUpload(tenantId, lang, differentLangFlag, language -> staticFile(buildRegionCache(tenantId, language, countryCode), uploadFolder, language, tenantId, countryCode));
    }

    private List<RegionCacheBO> buildRegionCache(Long tenantId, String lang, String countryCode) {
        RegionQueryLovInnerDTO dto = new RegionQueryLovInnerDTO();
        dto.setTenantId(tenantId);
        dto.setCountryCode(countryCode);
        dto.setLang(lang);
        final List<Region> regionList = regionRepository.listRegionLov(dto, BaseConstants.DEFAULT_TENANT_ID);

        return RegionConverter.poToBoListObjects(regionList);
    }

    /**
     * 上传静态文件
     *
     * @param list 静态文件数据实体
     */
    private String staticFile(final List<RegionCacheBO> list,
                              final String uploadFolder,
                              final String lang,
                              final Long tenantId,
                              final String countryCode) {
        if (CollectionUtils.isEmpty(list)){
            return null;
        }

        final String jsonString = JSON.toJSONString(list);
        // 上传路径全小写，多语言用中划线
        final String directory = Optional.ofNullable(uploadFolder)
                .orElse(Joiner.on(BaseConstants.Symbol.SLASH).skipNulls()
                        .join(MetadataConstants.Path.FILE,
                                MetadataConstants.Path.REGION,
                                lang));

        log.info("directory url {}", directory);
        final String fileName = MetadataConstants.Path.FILE_NAME + "-" + countryCode.toLowerCase() + MetadataConstants.FileSuffix.JSON;
        return O2FileHelper.uploadFile(tenantId,
                directory, fileName, MetadataConstants.O2SiteRegionFile.JSON_TYPE, jsonString.getBytes());
    }

    private List<StaticResourceSaveDO> buildStaticResourceSaveDTO(Long tenantId,
                                                                  Map<String, String> resourceUrlMap,
                                                                  String resourceOwner,
                                                                  StaticResourceConfigDO staticResourceConfigDO) {
        List<StaticResourceSaveDO> staticResourceSaveDOList = new ArrayList<>();
        for (Map.Entry<String, String> entry : resourceUrlMap.entrySet()) {
            staticResourceSaveDOList.add(fillCommonFields(tenantId, entry.getValue(), entry.getKey(), resourceOwner, staticResourceConfigDO));
        }
        return staticResourceSaveDOList;
    }

    private StaticResourceSaveDO fillCommonFields(Long tenantId,
                                                  String resourceUrl,
                                                  String languageCode,
                                                  String resourceOwner,
                                                  StaticResourceConfigDO staticResourceConfigDO) {
        String host = domainPrefix(resourceUrl);
        String url = trimDomainPrefix(resourceUrl);

        StaticResourceSaveDO staticResourceSaveDO = new StaticResourceSaveDO();
        staticResourceSaveDO.setResourceCode(staticResourceConfigDO.getResourceCode());
        staticResourceSaveDO.setDescription(staticResourceConfigDO.getDescription());
        staticResourceSaveDO.setResourceLevel(staticResourceConfigDO.getResourceLevel());
        staticResourceSaveDO.setEnableFlag(MetadataConstants.StaticResourceConstants.ENABLE_FLAG);
        if (!MetadataConstants.StaticResourceConstants.LEVEL_PUBLIC
                .equals(staticResourceSaveDO.getResourceLevel())) {
            staticResourceSaveDO.setResourceOwner(resourceOwner);
        }
        staticResourceSaveDO.setResourceHost(host);
        staticResourceSaveDO.setResourceUrl(url);
        if (MetadataConstants.StaticResourceConstants.CONFIG_DIFFERENT_LANG_FLAG
                .equals(staticResourceConfigDO.getDifferentLangFlag())) {
            staticResourceSaveDO.setLang(languageCode);
        }
        staticResourceSaveDO.setTenantId(tenantId);
        return staticResourceSaveDO;
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
}
