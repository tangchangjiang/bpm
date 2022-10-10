package org.o2.metadata.console.app.service;

import org.hzero.export.vo.ExportParam;
import org.o2.metadata.console.api.vo.CatalogVO;
import org.o2.metadata.console.infra.entity.Catalog;

import java.util.List;

/**
 * 版本应用服务
 *
 * @author jiu.yang@hand-china.com 2019-12-02 15:33:52
 */
public interface CatalogService {

    /**
     * 版本Excel导出
     * @param exportParam 版本主键字符拼接
     * @param tenantId 租户ID
     * @return the return
     * @throws RuntimeException exception description
     */
    List<CatalogVO> export(final  ExportParam exportParam, final Long tenantId );
    /**
     * 修改目录版本
     * @param catalog 目录
     */
    void update(final Catalog catalog);

    /**
     * 插入目录版本
     * @param catalog 目录
     */
    void insertSelective(Catalog catalog);
}