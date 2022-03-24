package org.o2.metadata.console.app.service;

/**
 * 多租户初始化Service
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/17 10:44
 */
public interface MetadataTenantInitService {
    /**
     * 租户初始化
     *
     * @param sourceTenantId 源租户Id
     * @param targetTenantId     目标租户
     */
    void tenantInitialize(Long sourceTenantId, Long targetTenantId);

}
