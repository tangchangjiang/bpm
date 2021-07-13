package org.o2.metadata.console.app.service.impl;

import io.choerodon.core.exception.CommonException;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.console.app.service.CatalogVersionService;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.entity.Catalog;
import org.o2.metadata.console.infra.entity.CatalogVersion;
import org.o2.metadata.console.infra.repository.CatalogRepository;
import org.o2.metadata.console.infra.repository.CatalogVersionRepository;
import org.springframework.stereotype.Service;

/**
 * 版本目录应用服务默认实现
 *
 * @author jiu.yang@hand-china.com 2019-12-02 15:33:52
 */
@Service
public class CatalogVersionServiceImpl implements CatalogVersionService {
    private final CatalogVersionRepository catalogVersionRepository;
    private final CatalogRepository catalogRepository;

    public CatalogVersionServiceImpl(CatalogVersionRepository catalogVersionRepository, CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
        this.catalogVersionRepository = catalogVersionRepository;
    }

    @Override
    public void update(final  CatalogVersion catalogVersion) {
        Long catalogId = catalogVersion.getCatalogId();
        Long tenantId = catalogVersion.getTenantId();
        Catalog catalog =  catalogRepository.selectOne(Catalog.builder().tenantId(tenantId).catalogId(catalogId).build());
        if (MetadataConstants.ACTIVE_FLAG.FORBIDDEN.equals(catalog.getActiveFlag())) {
            throw new CommonException(MetadataConstants.ErrorCode.O2MD_ERROR_CATALOG_FORBIDDEN);
        }
        SecurityTokenHelper.validToken(catalogVersion);
        catalogVersionRepository.updateByPrimaryKeySelective(catalogVersion);
    }

    @Override
    public void insert(CatalogVersion catalogVersion) {
        Long catalogId = catalogVersion.getCatalogId();
        Long tenantId = catalogVersion.getTenantId();
        Catalog catalog =  catalogRepository.selectOne(Catalog.builder().tenantId(tenantId).catalogId(catalogId).build());
        if (MetadataConstants.ACTIVE_FLAG.FORBIDDEN.equals(catalog.getActiveFlag())) {
            catalogVersion.setActiveFlag(MetadataConstants.ACTIVE_FLAG.FORBIDDEN);
        }
        catalogVersionRepository.insertSelective(catalogVersion);
    }
}
