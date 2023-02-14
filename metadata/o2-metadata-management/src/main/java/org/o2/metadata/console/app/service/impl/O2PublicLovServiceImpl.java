package org.o2.metadata.console.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.base.BaseConstants;
import org.o2.file.helper.O2FileHelper;
import org.o2.metadata.console.api.vo.PublicLovVO;
import org.o2.metadata.console.app.service.LanguageService;
import org.o2.metadata.console.app.service.O2PublicLovService;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.lovadapter.repository.HzeroLovQueryRepository;
import org.o2.metadata.console.infra.strategy.BusinessTypeStrategyDispatcher;
import org.o2.metadata.domain.staticresource.domain.StaticResourceConfigDO;
import org.o2.metadata.domain.staticresource.domain.StaticResourceSaveDO;
import org.o2.metadata.domain.staticresource.service.StaticResourceBusinessService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * O2MD.PUBLIC_LOV  静态文件
 *
 * @author: kang.yang@hand-china.com 2021-08-20 14:17
 **/
@Service
@Slf4j
public class O2PublicLovServiceImpl implements O2PublicLovService {

    private final HzeroLovQueryRepository hzeroLovQueryRepository;
    private final LanguageService languageService;

    public O2PublicLovServiceImpl(HzeroLovQueryRepository hzeroLovQueryRepository,
                                  LanguageService languageService) {
        this.hzeroLovQueryRepository = hzeroLovQueryRepository;
        this.languageService = languageService;
    }

    @Override
    public void createPublicLovFile(PublicLovVO publicLovVO, String resourceOwner, String businessTypeCode) {
        final Long tenantId = publicLovVO.getTenantId();
        final String lovCode = publicLovVO.getLovCode();
        final String lang = publicLovVO.getLang();
        log.info("O2MD.PUBLIC_LOV:static params are : {},{}", tenantId, lovCode);

        // 根据业务类型+目标class获取处理方法
        StaticResourceBusinessService staticResourceBusinessService = BusinessTypeStrategyDispatcher.getService(businessTypeCode,
                StaticResourceBusinessService.class);
        // 查询静态资源配置信息
        final StaticResourceConfigDO staticResourceConfigDO = staticResourceBusinessService.getStaticResourceConfig(tenantId,
                MetadataConstants.StaticResourceCode.O2MD_IDP_LOV);

        // 使用map存储resourceUrl,key为langCode、value为resourceUrl
        Map<String, String> resourceUrlMap = buildUrlByConfig(tenantId, lang, lovCode, staticResourceConfigDO);

        if (MapUtils.isEmpty(resourceUrlMap)) {
            return;
        }

        //  更新静态文件资源表
        List<StaticResourceSaveDO> staticResourceSaveDOList = buildStaticResourceSaveDTO(tenantId, resourceUrlMap, resourceOwner,
                staticResourceConfigDO);
        staticResourceBusinessService.saveStaticResource(tenantId, staticResourceSaveDOList);
    }

    private Map<String, String> buildUrlByConfig(Long tenantId,
                                                 String lang,
                                                 String lovCode,
                                                 StaticResourceConfigDO staticResourceConfigDO) {
        // 查询值集-O2MD.PUBLIC_LOV
        List<LovValueDTO> publicLovValueDTOList = hzeroLovQueryRepository.queryLovValue(tenantId, lovCode);
        if (CollectionUtils.isEmpty(publicLovValueDTOList)) {
            return Collections.emptyMap();
        }

        // 使用map存储resourceUrl,key为langCode、value为resourceUrl
        Map<String, String> resourceUrlMap = new HashMap<>(4);

        // 静态资源配置
        String uploadFolder = staticResourceConfigDO.getUploadFolder();
        Integer differentLangFlag = staticResourceConfigDO.getDifferentLangFlag();

        // resourceUrl-入参语言
        resourceUrlMap.put(lang, this.staticFile(queryLovValue(tenantId, lang, publicLovValueDTOList),
                uploadFolder, tenantId, lang));

        // 未开启多语言-返回
        if (!MetadataConstants.StaticResourceConstants.CONFIG_DIFFERENT_LANG_FLAG.equals(differentLangFlag)) {
            return resourceUrlMap;
        }

        // 查询所有语言
        List<String> allLanguageList = languageService.queryAllLanguages(tenantId);
        if (CollectionUtils.isEmpty(allLanguageList)) {
            return resourceUrlMap;
        }

        // 其他语言-过滤入参语言
        List<String> otherLanguageList = allLanguageList.stream().filter(language -> !lang.equals(language)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(otherLanguageList)) {
            return resourceUrlMap;
        }

        // resourceUrl-其他语言
        if (CollectionUtils.isEmpty(otherLanguageList)) {
            resourceUrlMap.put(lang, this.staticFile(queryLovValue(tenantId, lang, publicLovValueDTOList),
                    uploadFolder, tenantId, MetadataConstants.Path.ZH_CN));
        } else {
            otherLanguageList.forEach(language -> resourceUrlMap.put(language,
                    this.staticFile(queryLovValue(tenantId, language, publicLovValueDTOList), uploadFolder, tenantId, language)));
        }

        return resourceUrlMap;
    }

    /**
     * 查询值集值
     *
     * @param tenantId              租户ID
     * @param lang                  语言
     * @param publicLovValueDTOList 值集
     * @return json
     */
    private JSONObject queryLovValue(Long tenantId, String lang, List<LovValueDTO> publicLovValueDTOList) {
        JSONObject data = new JSONObject();
        if (CollectionUtils.isNotEmpty(publicLovValueDTOList)) {
            for (LovValueDTO lovValueDTO : publicLovValueDTOList) {
                List<LovValueDTO> lovValueDTOList = hzeroLovQueryRepository.queryLovValue(tenantId, lang, lovValueDTO.getValue());
                if (CollectionUtils.isNotEmpty(lovValueDTOList)) {
                    data.put(lovValueDTO.getValue(), lovValueDTOList);
                }
            }
        }
        return data;
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
                                lang));
        log.info("O2MD.PUBLIC_LOV directory url {}", directory);
        final String fileName = MetadataConstants.Path.LOV_FILE_NAME + MetadataConstants.FileSuffix.JSON;
        return O2FileHelper.uploadFile(tenantId, directory, fileName, MetadataConstants.PublicLov.JSON_TYPE, jsonString.getBytes());
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
        if (MetadataConstants.StaticResourceConstants.CONFIG_DIFFERENT_LANG_FLAG
                .equals(staticResourceConfigDO.getDifferentLangFlag())) {
            staticResourceSaveDO.setLang(languageCode);
        }
        staticResourceSaveDO.setResourceUrl(url);
        staticResourceSaveDO.setResourceHost(host);
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
