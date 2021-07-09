package org.o2.metadata.console.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.console.api.vo.CatalogVO;
import org.o2.metadata.console.infra.entity.Catalog;

import java.util.List;
import java.util.Set;

/**
 * 版本资源库
 *
 * @author jiu.yang@hand-china.com 2019-12-02 15:33:52
 */
public interface CatalogRepository extends BaseRepository<Catalog> {

    /**
     * 更据版本目录主键集合批量查询ExcelDTO
     * @param catalogIds 版本主键集合
     * @param tenantId 租户ID
     * @return the return
     * @throws RuntimeException exception description
     */
    List<CatalogVO> batchFindByIds(final Set<Long> catalogIds, final Long tenantId);
    /**
     * 查询目录
     * @author yipeng.zhu@hand-china.com
     * @date 2020-01-03
     * @param catalog 目录查询条件
     * @return  目录列表
     */
    List<Catalog> listCatalog(Catalog catalog);
}
