package org.o2.metadata.console.app.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.hzero.export.vo.ExportParam;
import org.o2.core.exception.O2CommonException;
import org.o2.metadata.console.app.service.CatalogService;
import org.o2.metadata.console.infra.constant.CatalogConstants;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.entity.Catalog;
import org.o2.metadata.console.infra.entity.CatalogVersion;
import org.o2.metadata.console.infra.repository.CatalogVersionRepository;
import org.o2.metadata.console.api.vo.CatalogVO;
import org.o2.metadata.console.infra.repository.CatalogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

/**
 * 版本应用服务默认实现
 *
 * @author jiu.yang@hand-china.com 2019-12-02 15:33:52
 */
@Service
public class CatalogServiceImpl implements CatalogService {


    private CatalogRepository catalogRepository;
    private CatalogVersionRepository catalogVersionRepository;

    public CatalogServiceImpl(CatalogRepository catalogRepository, CatalogVersionRepository catalogVersionRepository) {
        this.catalogRepository = catalogRepository;
        this.catalogVersionRepository = catalogVersionRepository;
    }

    /**
     * 版本Excel导出
     * @param exportParam 版本主键字符拼接
     * @param tenantId 租户ID
     * @return the return
     * @throws RuntimeException exception description
     */
    @Override
    public List<CatalogVO> export(final ExportParam exportParam,final Long tenantId) {
        return catalogRepository.batchFindByIds(new HashSet<>(2),tenantId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Catalog catalog) {
        Catalog original = catalogRepository.selectByPrimaryKey(catalog);
        if (!original.getCatalogName().equals(catalog.getCatalogName())) {
            validCatalogName(catalog);
        }
        if (!original.getCatalogCode().equals(catalog.getCatalogCode())) {
            throw new O2CommonException(null, CatalogConstants.ErrorCode.O2MD_CATALOG_CODE_NOT_UPDATE, CatalogConstants.ErrorCode.O2MD_CATALOG_CODE_NOT_UPDATE);
        }
        if (MetadataConstants.ActiveFlag.FORBIDDEN.equals(catalog.getActiveFlag())) {
            List<CatalogVersion> versions = catalogVersionRepository.select(CatalogVersion.builder()
                    .catalogId(catalog.getCatalogId()).tenantId(catalog.getTenantId()).build());
            for (CatalogVersion version : versions) {
                version.setActiveFlag(MetadataConstants.ActiveFlag.FORBIDDEN);
            }
            catalogVersionRepository.batchUpdateByPrimaryKeySelective(versions);
        }
        catalogRepository.updateByPrimaryKeySelective(catalog);
    }

    @Override
    public void insertSelective(Catalog catalog) {
        validCatalogName(catalog);
        validCatalogCode(catalog);
        catalogRepository.insert(catalog);
    }

    /**
     * 校验目录名称唯一性
     * @param catalog 目录
     */
    private void validCatalogName(Catalog catalog) {
        List<Catalog> catalogs =catalogRepository.select(Catalog.builder().
                tenantId(catalog.getTenantId()).
                catalogName(catalog.getCatalogName()).build());
        if (CollectionUtils.isNotEmpty(catalogs)) {
            throw new O2CommonException(null, CatalogConstants.ErrorCode.O2MD_CATALOG_NAME_UNIQUE, CatalogConstants.ErrorCode.O2MD_CATALOG_NAME_UNIQUE);
        }
    }
    /**
     * 校验目录名称唯一性
     * @param catalog 目录
     */
    private void validCatalogCode(Catalog catalog) {
        List<Catalog> catalogs =catalogRepository.select(Catalog.builder().
                tenantId(catalog.getTenantId()).
                catalogCode(catalog.getCatalogCode()).build());
        if (CollectionUtils.isNotEmpty(catalogs)) {
            throw new O2CommonException(null, CatalogConstants.ErrorCode.O2MD_CATALOG_CODE_UNIQUE, CatalogConstants.ErrorCode.O2MD_CATALOG_CODE_UNIQUE);
        }
    }
}
