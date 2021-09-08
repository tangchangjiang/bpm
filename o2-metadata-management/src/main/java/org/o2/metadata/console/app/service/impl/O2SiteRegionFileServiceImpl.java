package org.o2.metadata.console.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.file.FileClient;
import org.hzero.core.base.BaseConstants;
import org.o2.core.file.FileStorageProperties;
import org.o2.metadata.console.api.dto.RegionQueryLovDTO;
import org.o2.metadata.console.api.dto.StaticResourceConfigDTO;
import org.o2.metadata.console.api.dto.StaticResourceSaveDTO;
import org.o2.metadata.console.api.vo.RegionCacheVO;
import org.o2.metadata.console.app.bo.RegionCacheBO;
import org.o2.metadata.console.app.service.O2SiteRegionFileService;
import org.o2.metadata.console.app.service.StaticResourceConfigService;
import org.o2.metadata.console.app.service.StaticResourceInternalService;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.convertor.RegionConverter;
import org.o2.metadata.console.infra.entity.Region;
import org.o2.metadata.console.infra.entity.StaticResourceConfig;
import org.o2.metadata.console.infra.repository.RegionRepository;
import org.springframework.stereotype.Service;


import java.util.*;


/**
 * 地区静态文件
 *
 * @author: yipeng.zhu@hand-china.com 2020-05-20 11:09
 **/
@Service
@Slf4j
public class O2SiteRegionFileServiceImpl implements O2SiteRegionFileService {


    private final FileStorageProperties fileStorageProperties;
    private final RegionRepository regionRepository;
    private final FileClient fileClient;
    private final StaticResourceInternalService staticResourceInternalService;
    private final StaticResourceConfigService staticResourceConfigService;


    public O2SiteRegionFileServiceImpl(FileStorageProperties fileStorageProperties,
                                       RegionRepository regionRepository,
                                       FileClient fileClient,
                                       StaticResourceInternalService staticResourceInternalService,
                                       StaticResourceConfigService staticResourceConfigService) {
        this.fileStorageProperties = fileStorageProperties;
        this.regionRepository = regionRepository;
        this.fileClient = fileClient;
        this.staticResourceInternalService = staticResourceInternalService;
        this.staticResourceConfigService=staticResourceConfigService;
    }

    @Override
    public void createRegionStaticFile(final RegionCacheVO regionCacheVO, final String resourceOwner) {
        final Long tenantId = regionCacheVO.getTenantId();
        final String countryCode = regionCacheVO.getCountryCode();
        log.info("static params are : {},{}", tenantId, countryCode);

        // 查询静态资源配置信息
        StaticResourceConfigDTO staticResourceConfigDTO=new StaticResourceConfigDTO();
        staticResourceConfigDTO.setResourceCode(MetadataConstants.StaticResourceCode.O2MD_REGION);
        staticResourceConfigDTO.setTenantId(tenantId);

        final List<StaticResourceConfig> staticResourceConfigList=staticResourceConfigService.listStaticResourceConfig(staticResourceConfigDTO);
        final StaticResourceConfig staticResourceConfig=staticResourceConfigList.get(0);
        String uploadFolder=staticResourceConfig.getUploadFolder();

        // 使用map存储resourceUrl,key为langCode、value为resourceUrl
        Map<String,String> resourceUrlMap=new HashMap<>(4);

        // 查询地区sql值集并存入map
        RegionQueryLovDTO dto = new RegionQueryLovDTO();
        dto.setTenantId(tenantId);
        dto.setCountryCode(countryCode);

        dto.setLang(MetadataConstants.Path.ZH_CN);
        final List<Region> zhList = regionRepository.listRegionLov(dto,tenantId);
        resourceUrlMap.put(dto.getLang(),this.staticFile(RegionConverter.poToBoListObjects(zhList), uploadFolder, dto.getLang(), tenantId, countryCode));

        if(staticResourceConfig.getDifferentLangFlag()
                .equals(MetadataConstants.StaticResourceConstants.CONFIG_DIFFERENT_LANG_FLAG)){
            dto.setLang(MetadataConstants.Path.EN_US);
            final List<Region> enList = regionRepository.listRegionLov(dto,tenantId);
            resourceUrlMap.put(dto.getLang(),this.staticFile(RegionConverter.poToBoListObjects(enList), uploadFolder, dto.getLang(), tenantId, countryCode));
        }

        //  更新静态文件资源表
        List<StaticResourceSaveDTO> saveDTOList = buildStaticResourceSaveDTO(tenantId,resourceUrlMap,resourceOwner,staticResourceConfig);
        for (StaticResourceSaveDTO saveDTO : saveDTOList) {
            staticResourceInternalService.saveResource(saveDTO);
        }
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
                        .join(fileStorageProperties.getStoragePath(),
                                MetadataConstants.Path.FILE,
                                MetadataConstants.Path.REGION,
                                lang).toLowerCase());

        log.info("directory url {}", directory);
        final String fileName = MetadataConstants.Path.FILE_NAME + "-" + countryCode.toLowerCase() + MetadataConstants.FileSuffix.JSON;
        String resultUrl = fileClient.uploadFile(tenantId, fileStorageProperties.getBucketCode(),
                directory, fileName, MetadataConstants.O2SiteRegionFile.JSON_TYPE,
                fileStorageProperties.getStorageCode(), jsonString.getBytes());
        log.info("resultUrl url {},{},{}", resultUrl, fileStorageProperties.getBucketCode(), fileStorageProperties.getStorageCode());
        return resultUrl;
    }

    private List<StaticResourceSaveDTO> buildStaticResourceSaveDTO(Long tenantId,
                                                                   Map<String,String> resourceUrlMap,
                                                                   String resourceOwner,
                                                                   StaticResourceConfig staticResourceConfig) {
        List<StaticResourceSaveDTO> saveDTOList = new ArrayList<>();
        for(Map.Entry<String,String> entry:resourceUrlMap.entrySet()){
            saveDTOList.add(fillCommonFields(tenantId,entry.getValue(),entry.getKey(),resourceOwner,staticResourceConfig));
        }
        return saveDTOList;
    }

    private StaticResourceSaveDTO fillCommonFields(Long tenantId,
                                                   String resourceUrl,
                                                   String languageCode,
                                                   String resourceOwner,
                                                   StaticResourceConfig staticResourceConfig) {
        String domainAndUrl=trimHttpPrefix(resourceUrl);
        int indexOfSlash=domainAndUrl.indexOf(BaseConstants.Symbol.SLASH);

        StaticResourceSaveDTO saveDTO = new StaticResourceSaveDTO();
        saveDTO.setResourceCode(staticResourceConfig.getResourceCode());
        saveDTO.setDescription(staticResourceConfig.getDescription());
        saveDTO.setResourceLevel(staticResourceConfig.getResourceLevel());
        saveDTO.setEnableFlag(MetadataConstants.StaticResourceConstants.ENABLE_FLAG);
        if(!MetadataConstants.StaticResourceConstants.LEVEL_PUBLIC
                .equals(saveDTO.getResourceLevel())){
            saveDTO.setResourceOwner(resourceOwner);
        }
        saveDTO.setResourceHost(MetadataConstants.StaticResourceConstants.HOST_PREFIX
                +domainAndUrl.substring(0,indexOfSlash));
        saveDTO.setResourceUrl(domainAndUrl.substring(indexOfSlash));
        if(MetadataConstants.StaticResourceConstants.CONFIG_DIFFERENT_LANG_FLAG
                .equals(staticResourceConfig.getDifferentLangFlag())){
            saveDTO.setLang(languageCode);
        }
        saveDTO.setTenantId(tenantId);
        return saveDTO;
    }

    /**
     * 裁剪掉http前缀
     *
     * @param resourceUrl resourceUrl
     * @return result
     */
    private static String trimHttpPrefix(String resourceUrl) {
        if (StringUtils.isBlank(resourceUrl)) {
            return "";
        }

        String[] httpSplits = resourceUrl.split(BaseConstants.Symbol.DOUBLE_SLASH);
        if (httpSplits.length < 2) {
            return resourceUrl;
        }

        return httpSplits[1];
    }
}
