package org.o2.metadata.console.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.console.infra.entity.Catalog;
import org.o2.metadata.console.infra.repository.CatalogRepository;
import org.o2.metadata.console.api.vo.CatalogVO;
import org.o2.metadata.console.infra.mapper.CatalogMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * 版本 资源库实现
 *
 * @author jiu.yang@hand-china.com 2019-12-02 15:33:52
 */
@Component
public class CatalogRepositoryImpl extends BaseRepositoryImpl<Catalog> implements CatalogRepository {


    private final CatalogMapper catalogMapper;

    public CatalogRepositoryImpl(CatalogMapper catalogMapper) {
        this.catalogMapper = catalogMapper;
    }

    /**
     * 更据版本目录主键集合批量查询ExcelDTO
     * @param catalogIds 版本主键集合
     * @param tenantId 租户ID
     * @return the return
     * @throws RuntimeException exception description
     */
    @Override
    public List<CatalogVO> batchFindByIds(final Set<Long> catalogIds,final Long tenantId) {

        return catalogMapper.batchFindByIds(catalogIds,tenantId);
    }

    @Override
    public List<Catalog> listCatalog(Catalog catalog) {
        return catalogMapper.listCatalog(catalog);
    }
}
