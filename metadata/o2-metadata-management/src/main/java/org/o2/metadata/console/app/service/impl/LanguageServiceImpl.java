package org.o2.metadata.console.app.service.impl;

import org.o2.metadata.console.app.service.LanguageService;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageServiceImpl implements LanguageService {

    @Override
    public List<String> queryAllLanguages(Long tenantId) {
        return MetadataConstants.Lang.ALL_LANGUAGE;
    }
}
