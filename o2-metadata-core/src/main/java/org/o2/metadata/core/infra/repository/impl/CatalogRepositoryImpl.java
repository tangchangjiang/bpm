package org.o2.metadata.core.infra.repository.impl;

import org.o2.metadata.core.api.dto.CatalogDTO;
import org.o2.metadata.core.domain.entity.Catalog;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.core.domain.repository.CatalogRepository;
import org.o2.metadata.core.infra.mapper.CatalogMapper;
import org.springframework.beans.factory.annotation.Autowired;
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


    @Autowired
    private CatalogMapper catalogMapper;

    /**
     * 更据版本目录主键集合批量查询ExcelDTO
     * @param catalogIds 版本主键集合
     * @return the return
     * @throws RuntimeException exception description
     */
    @Override
    public List<CatalogDTO> batchFindByIds(Set<Long> catalogIds) {

        return catalogMapper.batchFindByIds(catalogIds);
    }
}
