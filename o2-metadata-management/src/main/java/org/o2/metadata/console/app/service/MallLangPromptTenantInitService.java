package org.o2.metadata.console.app.service;

/**
 * 多语言文件租户初始化
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/17 14:38
 */
public interface MallLangPromptTenantInitService {
    /**
     * 租户初始化
     *
     * @param sourceTenantId
     * @param targetTenantId 租户Id
     */
    void tenantInitialize(long sourceTenantId, Long targetTenantId);
}
