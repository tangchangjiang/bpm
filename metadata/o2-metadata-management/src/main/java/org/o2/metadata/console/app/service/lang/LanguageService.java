package org.o2.metadata.console.app.service.lang;

import java.util.List;

/**
 * 语言service
 * @author rui.ling@hand-china.com 2023/02/14
 */
public interface LanguageService {
    /**
     * 查询所有语言
     * @param tenantId 租户ID
     * @return languages
     */
    List<String> queryAllLanguages(Long tenantId);
}
