package org.o2.metadata.console.app.service.lang;

import java.util.Map;

/**
 * 多语言service
 *
 * @author rui.ling@hand-china.com 2023/02/14
 */
public interface MultiLangService {

    /**
     * 静态资源上传-多语言
     *
     * @param tenantId           租户ID
     * @param defaultLang        默认语言
     * @param differentLangFlag  多语言标识
     * @param multiLangOperation 多语言操作
     */
    Map<String, String> staticResourceUpload(Long tenantId,
                                             String defaultLang,
                                             Integer differentLangFlag,
                                             MultiLangOperation<String> multiLangOperation);
}
