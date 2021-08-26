package org.o2.metadata.console.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.file.FileClient;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.base.BaseConstants;
import org.o2.core.file.FileStorageProperties;
import org.o2.lov.app.service.HzeroLovQueryService;
import org.o2.metadata.console.api.dto.StaticResourceSaveDTO;
import org.o2.metadata.console.api.vo.PublicLovVO;
import org.o2.metadata.console.app.service.O2PublicLovService;
import org.o2.metadata.console.app.service.StaticResourceInternalService;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

import io.choerodon.core.oauth.DetailsHelper;

/**
 * O2MD.PUBLIC_LOV  静态文件
 *
 * @author: kang.yang@hand-china.com 2021-08-20 14:17
 **/
@Service
@Slf4j
public class O2PublicLovServiceImpl implements O2PublicLovService {
    private static final String JSON_TYPE = "application/json";

    private final HzeroLovQueryService hzeroLovQueryService;
    private final FileStorageProperties fileStorageProperties;
    private final FileClient fileClient;
    private final StaticResourceInternalService staticResourceInternalService;

    public O2PublicLovServiceImpl(HzeroLovQueryService hzeroLovQueryService, FileStorageProperties fileStorageProperties, FileClient fileClient, StaticResourceInternalService staticResourceInternalService) {
        this.hzeroLovQueryService = hzeroLovQueryService;
        this.fileStorageProperties = fileStorageProperties;
        this.fileClient = fileClient;
        this.staticResourceInternalService = staticResourceInternalService;
    }


    @Override
    public void createPublicLovFile(PublicLovVO publicLovVO) {
        final Long tenantId = publicLovVO.getTenantId();
        if (StringUtils.isBlank(publicLovVO.getLovCode())) {
            publicLovVO.setLovCode(MetadataConstants.StaticResourceCode.O2MD_PUB_LOV);
        }
        log.info("O2MD.PUBLIC_LOV:static params are : {},{}", tenantId, publicLovVO.getLovCode());

        List<LovValueDTO> publicLovValueDTOList = hzeroLovQueryService.queryLovValue(tenantId, publicLovVO.getLovCode());
        JSONObject data = new JSONObject();
        if (CollectionUtils.isNotEmpty(publicLovValueDTOList)) {
            for (LovValueDTO lovValueDTO : publicLovValueDTOList) {
                if (MetadataConstants.StaticResourceCode.O2MD_PUB_LOV.equals(publicLovVO.getLovCode())) {
                    //O2MD.PUBLIC_LOV
                    List<LovValueDTO> lovValueDTOList = hzeroLovQueryService.queryLovValue(tenantId, lovValueDTO.getValue());
                    if (CollectionUtils.isNotEmpty(lovValueDTOList)) {
                        data.put(lovValueDTO.getValue(), lovValueDTOList);
                    }
                } else {
                    data.put(publicLovVO.getLovCode(), publicLovValueDTOList);
                }
            }
            //1.hzero-boot-file的client上传 拿到OSS的url 2.更新到静态资源表
            String resourceUrl = this.staticFile(data, tenantId, DetailsHelper.getUserDetails().getLanguage());

            //  更新静态文件资源表
            List<StaticResourceSaveDTO> saveDTOList = buildStaticResourceSaveDTO(tenantId, resourceUrl, publicLovVO.getLovCode());
            for (StaticResourceSaveDTO saveDTO : saveDTOList) {
                staticResourceInternalService.saveResource(saveDTO);
            }
        }
    }

    /**
     * 上传o2-public-lov.json 静态文件  o2price_active_config
     *
     * @param jsonObject 静态文件数据实体
     */
    private String staticFile(final JSONObject jsonObject, final Long tenantId, final String lang) {
        final String jsonString = JSON.toJSONString(jsonObject);

        // 上传路径全小写，多语言用中划线
        final String directory = Joiner.on(BaseConstants.Symbol.SLASH).skipNulls().join(
                fileStorageProperties.getStoragePath(),
                MetadataConstants.Path.FILE,
                MetadataConstants.Path.LOV,
                lang).toLowerCase();
        log.info("O2MD.PUBLIC_LOV directory url {}", directory);
        final String fileName = MetadataConstants.Path.LOV_FILE_NAME + MetadataConstants.FileSuffix.JSON;
        String resultUrl = fileClient.uploadFile(tenantId, fileStorageProperties.getBucketCode(),
                directory, fileName, JSON_TYPE,
                fileStorageProperties.getStorageCode(), jsonString.getBytes());
        log.info("O2MD.PUBLIC_LOV: resultUrl url {},{},{}", resultUrl, fileStorageProperties.getBucketCode(), fileStorageProperties.getStorageCode());
        return resultUrl;
    }

    private List<StaticResourceSaveDTO> buildStaticResourceSaveDTO(Long tenantId, String resourceUrl, String lovCode) {
        List<StaticResourceSaveDTO> saveDTOList = new ArrayList<>();
        StaticResourceSaveDTO saveDTO = fillCommonFields(tenantId, resourceUrl, DetailsHelper.getUserDetails().getLanguage(), lovCode);
        saveDTOList.add(saveDTO);
        return saveDTOList;
    }

    private StaticResourceSaveDTO fillCommonFields(Long tenantId, String resourceUrl, String languageCode, String lovCode) {
        String trimResourceUrl = getTrimDomainPrefix(resourceUrl);
        StaticResourceSaveDTO saveDTO = new StaticResourceSaveDTO();
        saveDTO.setTenantId(tenantId);
        saveDTO.setResourceCode(lovCode);
        saveDTO.setSourceModuleCode(MetadataConstants.StaticResourceSourceModuleCode.METADATA);
        saveDTO.setDescription(lovCode + MetadataConstants.StaticResourceCode.LOV_DESCRIPTION);
        saveDTO.setResourceUrl(trimResourceUrl);
        saveDTO.setLang(languageCode);
        return saveDTO;
    }

    /**
     * 裁剪掉域名 + 端口
     *
     * @param resourceUrl resourceUrl
     * @return result
     */
    private static String getTrimDomainPrefix(String resourceUrl) {
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
}
