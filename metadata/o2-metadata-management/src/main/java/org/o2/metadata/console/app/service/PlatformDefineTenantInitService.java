package org.o2.metadata.console.app.service;

/**
 * 平台定义租户初始化
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/17 16:11
 */
public interface PlatformDefineTenantInitService {
    /**
     * d
     * @param sourceTenantId 源租户Id
     * @param targetTenantId 租户Id
     */
    void tenantInitialize(Long sourceTenantId, Long targetTenantId);
}