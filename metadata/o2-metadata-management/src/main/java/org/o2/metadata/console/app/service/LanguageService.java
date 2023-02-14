package org.o2.metadata.console.app.service;

import java.util.List;

public interface LanguageService {
    /**
     * 查询所有语言
     * @param tenantId 租户ID
     * @return languages
     */
    List<String> queryAllLanguages(Long tenantId);
}
