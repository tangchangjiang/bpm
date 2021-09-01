package org.o2.metadata.console.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.file.FileClient;
import org.hzero.core.base.BaseConstants;
import org.o2.core.file.FileStorageProperties;
import org.o2.metadata.console.api.dto.RegionQueryLovDTO;
import org.o2.metadata.console.api.dto.StaticResourceSaveDTO;
import org.o2.metadata.console.api.vo.RegionCacheVO;
import org.o2.metadata.console.app.bo.RegionCacheBO;
import org.o2.metadata.console.app.service.O2SiteRegionFileService;
import org.o2.metadata.console.app.service.StaticResourceInternalService;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.convertor.RegionConverter;
import org.o2.metadata.console.infra.entity.Region;
import org.o2.metadata.console.infra.mapper.RegionMapper;
import org.o2.metadata.console.infra.repository.RegionRepository;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;


/**
 * 地区静态文件
 *
 * @author: yipeng.zhu@hand-china.com 2020-05-20 11:09
 **/
@Service
@Slf4j
public class O2SiteRegionFileServiceImpl implements O2SiteRegionFileService {
    private static final String JSON_TYPE = "application/json";
    private static final String ZH_CN = "zh_CN";
    private static final String EN_US = "en_US";

    private final FileStorageProperties fileStorageProperties;
    private final RegionRepository regionRepository;
    private final FileClient fileClient;
    private final StaticResourceInternalService staticResourceInternalService;


    public O2SiteRegionFileServiceImpl(FileStorageProperties fileStorageProperties,
                                       RegionRepository regionRepository,
                                       FileClient fileClient,
                                       StaticResourceInternalService staticResourceInternalService) {
        this.fileStorageProperties = fileStorageProperties;
        this.regionRepository = regionRepository;
        this.fileClient = fileClient;
        this.staticResourceInternalService = staticResourceInternalService;
    }

    @Override
    public void createRegionStaticFile(final RegionCacheVO regionCacheVO) {
        final Long tenantId = regionCacheVO.getTenantId();
        log.info("static params are : {},{}", tenantId, regionCacheVO.getCountryCode());
        final String countryCode = regionCacheVO.getCountryCode();
        regionCacheVO.setLang(MetadataConstants.Path.ZH_CN);

        RegionQueryLovDTO dto = new RegionQueryLovDTO();
        dto.setLang(MetadataConstants.Path.ZH_CN);
        dto.setTenantId(tenantId);
        dto.setCountryCode(countryCode);

        final List<Region> zhList = regionRepository.listRegionLov(dto,tenantId);
        String zhResourceUrl = this.staticFile(RegionConverter.poToBoListObjects(zhList), MetadataConstants.Path.ZH_CN, tenantId, countryCode);

        dto.setLang(MetadataConstants.Path.EN_US);
        final List<Region> enList = regionRepository.listRegionLov(dto,tenantId);
        String enResourceUrl = this.staticFile(RegionConverter.poToBoListObjects(enList), MetadataConstants.Path.EN_US, tenantId, countryCode);

        //  更新静态文件资源表
        List<StaticResourceSaveDTO> saveDTOList = buildStaticResourceSaveDTO(tenantId, zhResourceUrl, enResourceUrl);
        for (StaticResourceSaveDTO saveDTO : saveDTOList) {
            staticResourceInternalService.saveResource(saveDTO);
        }
    }

    /**
     * 上传静态文件
     *
     * @param list 静态文件数据实体
     */
    // TODO：文件上传需要分布式锁
    private String staticFile(final List<RegionCacheBO> list,
                              final String lang,
                              final Long tenantId,
                              final String countryCode) {
        final String jsonString = JSON.toJSONString(list);
        // 上传路径全小写，多语言用中划线
        final String directory = Joiner.on(BaseConstants.Symbol.SLASH).skipNulls().join(
                fileStorageProperties.getStoragePath(),
                MetadataConstants.Path.FILE,
                MetadataConstants.Path.REGION,
                lang).toLowerCase();

        log.info("directory url {}", directory);
        final String fileName = MetadataConstants.Path.FILE_NAME + "-" + countryCode.toLowerCase() + MetadataConstants.FileSuffix.JSON;
        String resultUrl = fileClient.uploadFile(tenantId, fileStorageProperties.getBucketCode(),
                directory, fileName, JSON_TYPE,
                fileStorageProperties.getStorageCode(), jsonString.getBytes());
        log.info("resultUrl url {},{},{}", resultUrl, fileStorageProperties.getBucketCode(), fileStorageProperties.getStorageCode());
        return resultUrl;
    }

    private List<StaticResourceSaveDTO> buildStaticResourceSaveDTO(Long tenantId, String zhResourceUrl, String enResourceUrl) {
        List<StaticResourceSaveDTO> saveDTOList = new ArrayList<>();
        StaticResourceSaveDTO zhSaveDTO = fillCommonFields(tenantId, zhResourceUrl, ZH_CN);
        saveDTOList.add(zhSaveDTO);
        StaticResourceSaveDTO enSaveDTO = fillCommonFields(tenantId, enResourceUrl, EN_US);
        saveDTOList.add(enSaveDTO);
        return saveDTOList;
    }

    private StaticResourceSaveDTO fillCommonFields(Long tenantId, String resourceUrl, String languageCode) {
        String domainAndUrl=trimHttpPrefix(resourceUrl);
        int indexOfSlash=domainAndUrl.indexOf(BaseConstants.Symbol.SLASH);

        StaticResourceSaveDTO saveDTO = new StaticResourceSaveDTO();
        saveDTO.setTenantId(tenantId);
        saveDTO.setResourceCode(MetadataConstants.StaticResourceCode.buildMetadataRegionCode());
        saveDTO.setSourceModuleCode(MetadataConstants.StaticResourceSourceModuleCode.METADATA);
        saveDTO.setDescription(MetadataConstants.StaticResourceCode.O2MD_REGION_DESCRIPTION);
        saveDTO.setResourceUrl(domainAndUrl.substring(indexOfSlash));
        saveDTO.setDomain(domainAndUrl.substring(0,indexOfSlash));
        saveDTO.setLang(languageCode);
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
