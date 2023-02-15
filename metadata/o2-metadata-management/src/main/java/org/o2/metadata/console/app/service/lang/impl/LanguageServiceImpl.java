package org.o2.metadata.console.app.service.lang.impl;

import org.o2.metadata.console.app.service.lang.LanguageService;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 语言service
 * @author rui.ling@hand-china.com 2023/02/14
 */
@Service
public class LanguageServiceImpl implements LanguageService {

    @Override
    public List<String> queryAllLanguages(Long tenantId) {
        return MetadataConstants.Lang.ALL_LANGUAGE;
    }
}
