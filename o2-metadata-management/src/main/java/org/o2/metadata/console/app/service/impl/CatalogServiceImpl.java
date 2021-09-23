package org.o2.metadata.console.app.service.impl;

import org.hzero.export.vo.ExportParam;
import org.o2.metadata.console.app.service.CatalogService;
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
}
