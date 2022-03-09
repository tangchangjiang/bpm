package org.o2.metadata.console.app.service;

import org.o2.initialize.domain.context.TenantInitContext;

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
     * @param context 租户
     */
    void tenantInitialize(TenantInitContext context);
}
