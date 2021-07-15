package org.o2.metadata.console.app.service;

import org.o2.metadata.console.infra.entity.CatalogVersion;

/**
 * 版本目录应用服务
 *
 * @author jiu.yang@hand-china.com 2019-12-02 15:33:52
 */
public interface CatalogVersionService {
    /**
     * 更新
     * @date 2020-05-26
     * @param catalogVersion 目录版本
     */
    void update(final CatalogVersion catalogVersion);
    /**
     * 更新
     * @date 2020-06-10
     * @param catalogVersion 目录版本
     */
    void insert(final CatalogVersion catalogVersion);

}
