package org.o2.business.process.management.app.service;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/8/15 16:57
 */
public interface ProcessTenantInitService {

    /**
     * 租户初始化
     *
     * @param sourceTenantId 源租户
     * @param targetTenantId 目标租户
     */
    void tenantInitialize(Long sourceTenantId, Long targetTenantId);
}
