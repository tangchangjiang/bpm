package org.o2.metadata.console.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.o2.cms.management.client.O2CmsManagementClient;
import org.o2.cms.management.client.domain.co.StaticResourceConfigCO;
import org.o2.cms.management.client.domain.dto.StaticResourceConfigDTO;
import org.o2.cms.management.client.domain.dto.StaticResourceSaveDTO;
import org.o2.file.helper.O2FileHelper;
import org.o2.metadata.console.api.dto.RegionQueryLovInnerDTO;
import org.o2.metadata.console.api.vo.RegionCacheVO;
import org.o2.metadata.console.app.bo.RegionCacheBO;
import org.o2.metadata.console.app.service.O2SiteRegionFileService;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.convertor.RegionConverter;
import org.o2.metadata.console.infra.entity.Region;
import org.o2.metadata.console.infra.repository.RegionRepository;
import org.springframework.stereotype.Service;


import java.util.*;


/**
 * 地区静态文件
 *
 * @author yipeng.zhu@hand-china.com 2020-05-20 11:09
 **/
@Service
@Slf4j
public class O2SiteRegionFileServiceImpl implements O2SiteRegionFileService {


    private final RegionRepository regionRepository;
    private final O2CmsManagementClient cmsManagementClient;


    public O2SiteRegionFileServiceImpl(RegionRepository regionRepository,
                                       O2CmsManagementClient cmsManagementClient) {
        this.regionRepository = regionRepository;
        this.cmsManagementClient = cmsManagementClient;
    }

    @Override
    public void createRegionStaticFile(final RegionCacheVO regionCacheVO, final String resourceOwner) {
        final Long tenantId = regionCacheVO.getTenantId();
        final String countryCode = regionCacheVO.getCountryCode();
        log.info("static params are : {},{}", tenantId, countryCode);

        // 查询静态资源配置信息
        final StaticResourceConfigCO staticResourceConfigCO = cmsManagementClient.getStaticResourceConfig(tenantId,MetadataConstants.StaticResourceCode.O2MD_REGION);
        String uploadFolder = staticResourceConfigCO.getUploadFolder();

        // 使用map存储resourceUrl,key为langCode、value为resourceUrl
        Map<String, String> resourceUrlMap = new HashMap<>(4);

        // 查询地区sql值集并存入map
        RegionQueryLovInnerDTO dto = new RegionQueryLovInnerDTO();
        dto.setTenantId(tenantId);
        dto.setCountryCode(countryCode);

        dto.setLang(MetadataConstants.Path.ZH_CN);
        final List<Region> zhList = regionRepository.listRegionLov(dto, BaseConstants.DEFAULT_TENANT_ID);
        if (CollectionUtils.isEmpty(zhList)) {
            log.error("Can't find any region !");
            return;
        }

        resourceUrlMap.put(dto.getLang(), this.staticFile(RegionConverter.poToBoListObjects(zhList), uploadFolder, dto.getLang(), tenantId, countryCode));

        if (staticResourceConfigCO.getDifferentLangFlag()
                .equals(MetadataConstants.StaticResourceConstants.CONFIG_DIFFERENT_LANG_FLAG)) {
            dto.setLang(MetadataConstants.Path.EN_US);
            final List<Region> enList = regionRepository.listRegionLov(dto, tenantId);
            resourceUrlMap.put(dto.getLang(), this.staticFile(RegionConverter.poToBoListObjects(enList), uploadFolder, dto.getLang(), tenantId, countryCode));
        }

        //  更新静态文件资源表
        List<StaticResourceSaveDTO> saveDTOList = buildStaticResourceSaveDTO(tenantId, resourceUrlMap, resourceOwner, staticResourceConfigCO);
        cmsManagementClient.saveResource(tenantId,saveDTOList);
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
        final String jsonString = JSON.toJSONString(list);
        // 上传路径全小写，多语言用中划线
        final String directory = Optional.ofNullable(uploadFolder)
                .orElse(Joiner.on(BaseConstants.Symbol.SLASH).skipNulls()
                        .join(MetadataConstants.Path.FILE,
                                MetadataConstants.Path.REGION,
                                lang).toLowerCase());

        log.info("directory url {}", directory);
        final String fileName = MetadataConstants.Path.FILE_NAME + "-" + countryCode.toLowerCase() + MetadataConstants.FileSuffix.JSON;
        return O2FileHelper.uploadFile(tenantId,
                directory, fileName, MetadataConstants.O2SiteRegionFile.JSON_TYPE, jsonString.getBytes());
    }

    private List<StaticResourceSaveDTO> buildStaticResourceSaveDTO(Long tenantId,
                                                                   Map<String, String> resourceUrlMap,
                                                                   String resourceOwner,
                                                                   StaticResourceConfigCO staticResourceConfigCO) {
        List<StaticResourceSaveDTO> saveDTOList = new ArrayList<>();
        for (Map.Entry<String, String> entry : resourceUrlMap.entrySet()) {
            saveDTOList.add(fillCommonFields(tenantId, entry.getValue(), entry.getKey(), resourceOwner, staticResourceConfigCO));
        }
        return saveDTOList;
    }

    private StaticResourceSaveDTO fillCommonFields(Long tenantId,
                                                   String resourceUrl,
                                                   String languageCode,
                                                   String resourceOwner,
                                                   StaticResourceConfigCO staticResourceConfigCO) {
        String host = domainPrefix(resourceUrl);
        String url = trimDomainPrefix(resourceUrl);

        StaticResourceSaveDTO saveDTO = new StaticResourceSaveDTO();
        saveDTO.setResourceCode(staticResourceConfigCO.getResourceCode());
        saveDTO.setDescription(staticResourceConfigCO.getDescription());
        saveDTO.setResourceLevel(staticResourceConfigCO.getResourceLevel());
        saveDTO.setEnableFlag(MetadataConstants.StaticResourceConstants.ENABLE_FLAG);
        if (!MetadataConstants.StaticResourceConstants.LEVEL_PUBLIC
                .equals(saveDTO.getResourceLevel())) {
            saveDTO.setResourceOwner(resourceOwner);
        }
        saveDTO.setResourceHost(host);
        saveDTO.setResourceUrl(url);
        if (MetadataConstants.StaticResourceConstants.CONFIG_DIFFERENT_LANG_FLAG
                .equals(staticResourceConfigCO.getDifferentLangFlag())) {
            saveDTO.setLang(languageCode);
        }
        saveDTO.setTenantId(tenantId);
        return saveDTO;
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
