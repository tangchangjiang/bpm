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
import org.o2.metadata.console.api.dto.StaticResourceConfigDTO;
import org.o2.metadata.console.api.dto.StaticResourceSaveDTO;
import org.o2.metadata.console.api.vo.PublicLovVO;
import org.o2.metadata.console.app.service.O2PublicLovService;
import org.o2.metadata.console.app.service.StaticResourceConfigService;
import org.o2.metadata.console.app.service.StaticResourceInternalService;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.entity.StaticResourceConfig;
import org.o2.metadata.console.infra.lovadapter.repository.HzeroLovQueryRepository;
import org.springframework.stereotype.Service;


import java.util.*;


/**
 * O2MD.PUBLIC_LOV  静态文件
 *
 * @author: kang.yang@hand-china.com 2021-08-20 14:17
 **/
@Service
@Slf4j
public class O2PublicLovServiceImpl implements O2PublicLovService {

    private final HzeroLovQueryRepository hzeroLovQueryRepository;
    private final FileStorageProperties fileStorageProperties;
    private final FileClient fileClient;
    private final StaticResourceInternalService staticResourceInternalService;
    private final StaticResourceConfigService staticResourceConfigService;

    public O2PublicLovServiceImpl(HzeroLovQueryRepository hzeroLovQueryRepository,
                                  FileStorageProperties fileStorageProperties,
                                  FileClient fileClient,
                                  StaticResourceInternalService staticResourceInternalService,
                                  StaticResourceConfigService staticResourceConfigService) {
        this.hzeroLovQueryRepository = hzeroLovQueryRepository;
        this.fileStorageProperties = fileStorageProperties;
        this.fileClient = fileClient;
        this.staticResourceInternalService = staticResourceInternalService;
        this.staticResourceConfigService=staticResourceConfigService;
    }


    @Override
    public void createPublicLovFile(PublicLovVO publicLovVO, String resourceOwner) {
        final Long tenantId = publicLovVO.getTenantId();
        if (StringUtils.isBlank(publicLovVO.getLovCode())) {
            // 设置PUB_LOV默认值集编码
            publicLovVO.setLovCode(MetadataConstants.PublicLov.PUB_LOV_CODE);
        }
        final String lovCode=publicLovVO.getLovCode();
        log.info("O2MD.PUBLIC_LOV:static params are : {},{}", tenantId, lovCode);

        StaticResourceConfigDTO staticResourceConfigDTO=new StaticResourceConfigDTO();
        staticResourceConfigDTO.setResourceCode(MetadataConstants.StaticResourceCode.O2MD_IDP_LOV);
        staticResourceConfigDTO.setTenantId(tenantId);

        final List<StaticResourceConfig> staticResourceConfigList=staticResourceConfigService.listStaticResourceConfig(staticResourceConfigDTO);
        final StaticResourceConfig staticResourceConfig=staticResourceConfigList.get(0);
        String uploadFolder=staticResourceConfig.getUploadFolder();

        // 使用map存储resourceUrl,key为langCode、value为resourceUrl
        Map<String,String> resourceUrlMap=new HashMap<>(4);


        List<LovValueDTO> publicLovValueDTOList = hzeroLovQueryRepository.queryLovValue(tenantId, lovCode);
        JSONObject data = new JSONObject();
        if (CollectionUtils.isNotEmpty(publicLovValueDTOList)) {
            for (LovValueDTO lovValueDTO : publicLovValueDTOList) {
                if (MetadataConstants.PublicLov.PUB_LOV_CODE.equals(publicLovVO.getLovCode())) {
                    //O2MD.PUBLIC_LOV
                    List<LovValueDTO> lovValueDTOList = hzeroLovQueryRepository.queryLovValue(tenantId, lovValueDTO.getValue());
                    if (CollectionUtils.isNotEmpty(lovValueDTOList)) {
                        data.put(lovValueDTO.getValue(), lovValueDTOList);
                    }
                } else {
                    data.put(publicLovVO.getLovCode(), publicLovValueDTOList);
                }
            }

            //1.hzero-boot-file的client上传 拿到OSS的url 2.更新到静态资源表
            resourceUrlMap.put(MetadataConstants.Path.ZH_CN,this.staticFile(data, uploadFolder,tenantId, MetadataConstants.Path.ZH_CN));
            if(MetadataConstants.StaticResourceConstants.CONFIG_DIFFERENT_LANG_FLAG
                    .equals(staticResourceConfig.getDifferentLangFlag())){
                resourceUrlMap.put(MetadataConstants.Path.EN_US,this.staticFile(data, uploadFolder,tenantId, MetadataConstants.Path.EN_US));
            }

            //  更新静态文件资源表
            List<StaticResourceSaveDTO> saveDTOList = buildStaticResourceSaveDTO(tenantId, resourceUrlMap,resourceOwner,staticResourceConfig);
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
    private String staticFile(final JSONObject jsonObject,
                              final String uploadFolder,
                              final Long tenantId,
                              final String lang) {
        final String jsonString = JSON.toJSONString(jsonObject);

        // 上传路径全小写，多语言用中划线
        final String directory = Optional.ofNullable(uploadFolder)
                .orElse(Joiner.on(BaseConstants.Symbol.SLASH)
                        .skipNulls().join(fileStorageProperties.getStoragePath(),
                                MetadataConstants.Path.FILE,
                                MetadataConstants.Path.LOV,
                                lang).toLowerCase());
        log.info("O2MD.PUBLIC_LOV directory url {}", directory);
        final String fileName = MetadataConstants.Path.LOV_FILE_NAME + MetadataConstants.FileSuffix.JSON;
        String resultUrl = fileClient.uploadFile(tenantId, fileStorageProperties.getBucketCode(),
                directory, fileName, MetadataConstants.PublicLov.JSON_TYPE,
                fileStorageProperties.getStorageCode(), jsonString.getBytes());
        log.info("O2MD.PUBLIC_LOV: resultUrl url {},{},{}", resultUrl, fileStorageProperties.getBucketCode(), fileStorageProperties.getStorageCode());
        return resultUrl;
    }

    private List<StaticResourceSaveDTO> buildStaticResourceSaveDTO(Long tenantId,
                                                                   Map<String,String> resourceUrlMap,
                                                                   String resourceOwner,
                                                                   StaticResourceConfig staticResourceConfig) {
        List<StaticResourceSaveDTO> saveDTOList = new ArrayList<>();
        for(Map.Entry<String,String> entry:resourceUrlMap.entrySet()){
            saveDTOList.add(fillCommonFields(tenantId, entry.getValue(), entry.getKey(),resourceOwner,staticResourceConfig));
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
        if(MetadataConstants.StaticResourceConstants.CONFIG_DIFFERENT_LANG_FLAG
                .equals(staticResourceConfig.getDifferentLangFlag())){
            saveDTO.setLang(languageCode);
        }
        saveDTO.setResourceUrl(domainAndUrl.substring(indexOfSlash));
        saveDTO.setResourceHost(MetadataConstants.StaticResourceConstants.HOST_PREFIX
                +domainAndUrl.substring(0,indexOfSlash));
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
