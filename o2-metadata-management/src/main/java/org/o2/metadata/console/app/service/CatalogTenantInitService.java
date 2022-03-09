package org.o2.metadata.console.app.service;

import org.o2.initialize.domain.context.TenantInitContext;
import org.o2.metadata.console.infra.entity.Catalog;

import java.util.List;

/**
 * 目录&目录版本租户初始化
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/17 16:43
 */
public interface CatalogTenantInitService {
    /**
     * 租户初始化
     *
     * @param context job参数
     */
    void tenantInitialize(TenantInitContext context);

    /**
     * 租户业务数据初始化
     * @param context job参数
     */
    void tenantInitializeBusiness(TenantInitContext context);

    /**
     * 更新& 插入租户数据
     *
     * @param oldList        目标租户已存在的
     * @param initList       目标租户 需要初始化的数据
     * @param sourceTenantId 源租户
     * @param targetTenantId 目标租户
     */
    void handleCatalog(List<Catalog> oldList, List<Catalog> initList, Long sourceTenantId, Long targetTenantId);

    /**
     * 更新& 插入租户数据
     *
     * @param context job参数
     */
    void handleCatalogVersion(TenantInitContext context);
}
