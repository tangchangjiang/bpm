package org.o2.metadata.console.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.console.api.dto.CatalogRelVersionQueryDTO;
import org.o2.metadata.console.infra.entity.CatalogVersion;
import org.o2.metadata.console.infra.mapper.CatalogVersionMapper;
import org.o2.metadata.console.infra.repository.CatalogVersionRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 版本目录 资源库实现
 *
 * @author jiu.yang@hand-china.com 2019-12-02 15:33:52
 */
@Component
public class CatalogVersionRepositoryImpl extends BaseRepositoryImpl<CatalogVersion> implements CatalogVersionRepository {
    private  final CatalogVersionMapper catalogVersionMapper;

    public CatalogVersionRepositoryImpl(CatalogVersionMapper catalogVersionMapper) {
        this.catalogVersionMapper = catalogVersionMapper;
    }

    @Override
    public List<CatalogVersion> batchSelectByCodes(List<String> catalogVersionCodes, Long organizationId) {
        return catalogVersionMapper.batchSelectByCodes(catalogVersionCodes,organizationId);
    }

    @Override
    public List<CatalogVersion> catalogRelVersion(CatalogRelVersionQueryDTO queryDTO) {
        return catalogVersionMapper.catalogRelVersion(queryDTO);
    }
}
