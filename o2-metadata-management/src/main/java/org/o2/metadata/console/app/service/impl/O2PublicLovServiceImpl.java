package org.o2.metadata.console.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.base.BaseConstants;
import org.o2.cms.management.client.O2CmsManagementClient;
import org.o2.cms.management.client.domain.co.StaticResourceConfigCO;
import org.o2.cms.management.client.domain.dto.StaticResourceConfigDTO;
import org.o2.cms.management.client.domain.dto.StaticResourceSaveDTO;
import org.o2.file.helper.O2FileHelper;
import org.o2.metadata.console.api.vo.PublicLovVO;
import org.o2.metadata.console.app.service.O2PublicLovService;
import org.o2.metadata.console.infra.constant.MetadataConstants;
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
    private final O2CmsManagementClient cmsManagementClient;

    public O2PublicLovServiceImpl(HzeroLovQueryRepository hzeroLovQueryRepository,
                                  O2CmsManagementClient cmsManagementClient) {
        this.hzeroLovQueryRepository = hzeroLovQueryRepository;
        this.cmsManagementClient = cmsManagementClient;
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

        // 查询静态资源配置信息
        final StaticResourceConfigCO staticResourceConfigCO=cmsManagementClient.getStaticResourceConfig(tenantId,MetadataConstants.StaticResourceCode.O2MD_IDP_LOV);
        String uploadFolder=staticResourceConfigCO.getUploadFolder();

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
                    .equals(staticResourceConfigCO.getDifferentLangFlag())){
                resourceUrlMap.put(MetadataConstants.Path.EN_US,this.staticFile(data, uploadFolder,tenantId, MetadataConstants.Path.EN_US));
            }

            //  更新静态文件资源表
            List<StaticResourceSaveDTO> saveDTOList = buildStaticResourceSaveDTO(tenantId, resourceUrlMap,resourceOwner,staticResourceConfigCO);
            cmsManagementClient.saveResource(tenantId,saveDTOList);
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
                        .skipNulls().join(
                                MetadataConstants.Path.FILE,
                                MetadataConstants.Path.LOV,
                                lang).toLowerCase());
        log.info("O2MD.PUBLIC_LOV directory url {}", directory);
        final String fileName = MetadataConstants.Path.LOV_FILE_NAME + MetadataConstants.FileSuffix.JSON;
        return O2FileHelper.uploadFile(tenantId,
                directory, fileName, MetadataConstants.PublicLov.JSON_TYPE, jsonString.getBytes());
    }

    private List<StaticResourceSaveDTO> buildStaticResourceSaveDTO(Long tenantId,
                                                                   Map<String,String> resourceUrlMap,
                                                                   String resourceOwner,
                                                                   StaticResourceConfigCO staticResourceConfigCO) {
        List<StaticResourceSaveDTO> saveDTOList = new ArrayList<>();
        for(Map.Entry<String,String> entry:resourceUrlMap.entrySet()){
            saveDTOList.add(fillCommonFields(tenantId, entry.getValue(), entry.getKey(),resourceOwner,staticResourceConfigCO));
        }
        return saveDTOList;
    }

    private StaticResourceSaveDTO fillCommonFields(Long tenantId,
                                                   String resourceUrl,
                                                   String languageCode,
                                                   String resourceOwner,
                                                   StaticResourceConfigCO staticResourceConfigCO) {
        String host=domainPrefix(resourceUrl);
        String url=trimDomainPrefix(resourceUrl);

        StaticResourceSaveDTO saveDTO = new StaticResourceSaveDTO();
        saveDTO.setResourceCode(staticResourceConfigCO.getResourceCode());
        saveDTO.setDescription(staticResourceConfigCO.getDescription());
        saveDTO.setResourceLevel(staticResourceConfigCO.getResourceLevel());
        saveDTO.setEnableFlag(MetadataConstants.StaticResourceConstants.ENABLE_FLAG);
        if(!MetadataConstants.StaticResourceConstants.LEVEL_PUBLIC
                .equals(saveDTO.getResourceLevel())){
            saveDTO.setResourceOwner(resourceOwner);
        }
        if(MetadataConstants.StaticResourceConstants.CONFIG_DIFFERENT_LANG_FLAG
                .equals(staticResourceConfigCO.getDifferentLangFlag())){
            saveDTO.setLang(languageCode);
        }
        saveDTO.setResourceUrl(url);
        saveDTO.setResourceHost(host);
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
