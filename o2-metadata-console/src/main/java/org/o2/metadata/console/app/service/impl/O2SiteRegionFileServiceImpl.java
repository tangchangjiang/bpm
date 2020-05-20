package org.o2.metadata.console.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import io.choerodon.core.oauth.DetailsHelper;
import lombok.extern.slf4j.Slf4j;
import org.hzero.boot.file.FileClient;
import org.hzero.core.base.BaseConstants;
import org.o2.core.file.FileStorageProperties;
import org.o2.metadata.console.app.service.O2SiteRegionFileService;
import org.o2.metadata.console.infra.constant.O2MdConsoleConstants;
import org.o2.metadata.core.domain.entity.Region;
import org.o2.metadata.core.domain.vo.RegionCacheVO;
import org.o2.metadata.core.infra.mapper.RegionMapper;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 *
 * 地区静态文件
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
    public void createRegionStaticFile() {
        final Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        log.info("static params are : {}", tenantId);
        RegionCacheVO region = new RegionCacheVO();
        region.setTenantId(tenantId);
        region.setLang(O2MdConsoleConstants.Path.ZH_CN);
        List<RegionCacheVO> zhList = regionMapper.selectRegionList(region);
        this.staticFile(zhList,O2MdConsoleConstants.Path.ZH_CN,tenantId);

        region.setLang(O2MdConsoleConstants.Path.EN_US);
        List<RegionCacheVO> enList = regionMapper.selectRegionList(region);
        this.staticFile(enList,O2MdConsoleConstants.Path.EN_US,tenantId);

    }

    /**
     * 上传静态文件
     *
     * @param list 静态文件数据实体
     */
    private void staticFile(List<RegionCacheVO> list,String lang,Long tenantId) {
        final String jsonString = JSON.toJSONString(list);
        // 上传路径全小写，多语言用中划线
        final String directory = Joiner.on(BaseConstants.Symbol.SLASH).skipNulls().join(
                fileStorageProperties.getStoragePath(), O2MdConsoleConstants.Path.O2_RESOURCES,
                O2MdConsoleConstants.Path.METADATA,O2MdConsoleConstants.Path.FILE,O2MdConsoleConstants.Path.REGION,
                lang,tenantId).toLowerCase();
        String resultUrl = fileClient.uploadFile(tenantId, fileStorageProperties.getBucketCode(),
                directory, O2MdConsoleConstants.Path.FILE_NAME + O2MdConsoleConstants.FileSuffix.JSON, JSON_TYPE,
                fileStorageProperties.getStorageCode(), jsonString.getBytes());
        log.info("createProductStaticFileJob url {},{},{}", resultUrl, directory, fileStorageProperties.getStoragePath());
    }
}
