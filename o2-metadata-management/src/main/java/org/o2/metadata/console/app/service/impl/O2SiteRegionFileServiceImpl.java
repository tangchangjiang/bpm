package org.o2.metadata.console.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.hzero.boot.file.FileClient;
import org.hzero.core.base.BaseConstants;
import org.o2.core.file.FileStorageProperties;
import org.o2.metadata.console.api.vo.RegionCacheVO;
import org.o2.metadata.console.app.service.O2SiteRegionFileService;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.mapper.RegionMapper;
import org.springframework.stereotype.Service;


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
    private RegionMapper regionMapper;
    private FileStorageProperties fileStorageProperties;
    private FileClient fileClient;

    public O2SiteRegionFileServiceImpl(RegionMapper regionMapper,
                                       FileStorageProperties fileStorageProperties,
                                       FileClient fileClient) {
        this.regionMapper = regionMapper;
        this.fileStorageProperties = fileStorageProperties;
        this.fileClient = fileClient;
    }

    @Override
    public String createRegionStaticFile(final RegionCacheVO regionCacheVO) {
        log.info("static params are : {},{}", regionCacheVO.getTenantId(), regionCacheVO.getCountryCode());
        final String countryCode = regionCacheVO.getCountryCode();
        regionCacheVO.setLang(MetadataConstants.Path.ZH_CN);
        final List<RegionCacheVO> zhList = regionMapper.selectRegionList(regionCacheVO);
        this.staticFile(zhList, MetadataConstants.Path.ZH_CN, regionCacheVO.getTenantId(), countryCode);

        regionCacheVO.setLang(MetadataConstants.Path.EN_US);
        final List<RegionCacheVO> enList = regionMapper.selectRegionList(regionCacheVO);
        // FXIME：区分语言 OSS url
        return this.staticFile(enList, MetadataConstants.Path.EN_US, regionCacheVO.getTenantId(), countryCode);
    }

    /**
     * 上传静态文件
     *
     * @param list 静态文件数据实体
     */
    // TODO：文件上传需要分布式锁
    private String staticFile(final List<RegionCacheVO> list,
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
}
