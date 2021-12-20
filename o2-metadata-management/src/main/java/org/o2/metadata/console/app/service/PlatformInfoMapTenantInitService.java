package org.o2.metadata.console.app.service;

/**
 * description
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/17 18:04
 */
public interface PlatformInfoMapTenantInitService {
    /**
     * 租户初始化
     *
     * @param tenantId 租户
     */
    void tenantInitialize(Long tenantId);
}
