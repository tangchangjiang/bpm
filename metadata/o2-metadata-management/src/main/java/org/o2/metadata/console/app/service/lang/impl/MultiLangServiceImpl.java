package org.o2.metadata.console.app.service.lang.impl;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.o2.metadata.console.app.service.lang.LanguageService;
import org.o2.metadata.console.app.service.lang.MultiLangOperation;
import org.o2.metadata.console.app.service.lang.MultiLangService;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 多语言service
 *
 * @author rui.ling@hand-china.com 2023/02/14
 */
@Slf4j
@Service
public class MultiLangServiceImpl implements MultiLangService {
    private final LanguageService languageService;

    public MultiLangServiceImpl(LanguageService languageService) {
        this.languageService = languageService;
    }

    @Override
    public Map<String, String> staticResourceUpload(Long tenantId,
                                                    String defaultLang,
                                                    Integer differentLangFlag,
                                                    MultiLangOperation<String> multiLangOperation) {
        // 资源路径map
        Map<String, String> resourceUrlMap = Maps.newHashMapWithExpectedSize(BaseConstants.Digital.FOUR);

        // 获取资源路径url-默认语言
        resourceUrlMap.put(defaultLang, multiLangOperation.execute(defaultLang));

        // 未开启多语言-返回
        if (!MetadataConstants.StaticResourceConstants.CONFIG_DIFFERENT_LANG_FLAG.equals(differentLangFlag)) {
            return resourceUrlMap;
        }

        // 查询所有语言
        List<String> allLanguageList = languageService.queryAllLanguages(tenantId);
        if (CollectionUtils.isEmpty(allLanguageList)) {
            return resourceUrlMap;
        }

        // 其他语言-过滤默认语言
        List<String> otherLanguageList = allLanguageList.stream().filter(language -> !defaultLang.equals(language)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(otherLanguageList)) {
            return resourceUrlMap;
        }

        // 获取资源路径url-其他语言
        otherLanguageList.forEach(otherLang -> resourceUrlMap.put(otherLang, multiLangOperation.execute(otherLang)));

        // 过滤上传失败key
        resourceUrlMap.forEach((lang, resourceUrl) -> {
            if (StringUtils.isBlank(resourceUrl)) {
                resourceUrlMap.remove(lang);
            }
        });

        return resourceUrlMap;
    }

    @Override
    public <T> Map<String, T> batchQueryByLang(Long tenantId,
                                               List<String> languageList,
                                               MultiLangOperation<T> multiLangOperation) {
        if (CollectionUtils.isEmpty(languageList)) {
            languageList = languageService.queryAllLanguages(tenantId);
        }

        if (CollectionUtils.isEmpty(languageList)) {
            return Collections.emptyMap();
        }

        // lang->result
        Map<String, T> lovListByLang = Maps.newHashMapWithExpectedSize(languageList.size());
        languageList.forEach(lang -> {
            T lovValueDTOList = multiLangOperation.execute(lang);
            lovListByLang.put(lang, lovValueDTOList);
        });
        return lovListByLang;
    }
}
