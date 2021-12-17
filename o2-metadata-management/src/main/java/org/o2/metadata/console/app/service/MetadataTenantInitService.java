package org.o2.metadata.console.app.service;

import java.util.List;

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
     * @param tenantList 目标租户集合
     */
    void tenantInitialize(List<String> tenantList);
}
