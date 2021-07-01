package org.o2.metadata.console.app.service.impl;

import org.hzero.export.vo.ExportParam;
import org.o2.metadata.console.app.service.CatalogService;
import org.o2.metadata.console.infra.constant.O2MdConsoleConstants;
import org.o2.metadata.console.domain.entity.Catalog;
import org.o2.metadata.console.domain.entity.CatalogVersion;
import org.o2.metadata.console.domain.repository.CatalogVersionRepository;
import org.o2.metadata.console.api.vo.CatalogVO;
import org.o2.metadata.console.domain.repository.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * 版本应用服务默认实现
 *
 * @author jiu.yang@hand-china.com 2019-12-02 15:33:52
 */
@Service
public class CatalogServiceImpl implements CatalogService {


    @Autowired
    private CatalogRepository catalogRepository;
    @Autowired
    private CatalogVersionRepository catalogVersionRepository;

    /**
     * 版本Excel导出
     * @param exportParam 版本主键字符拼接
     * @param tenantId 租户ID
     * @return the return
     * @throws RuntimeException exception description
     */
    @Override
    public List<CatalogVO> export(final ExportParam exportParam,final Long tenantId) {
        Set<Long> catalogBatchIdList = exportParam.getIds();
        return catalogRepository.batchFindByIds(catalogBatchIdList,tenantId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Catalog catalog) {

        if (O2MdConsoleConstants.ACTIVE_FLAG.FORBIDDEN.equals(catalog.getActiveFlag())) {
            List<CatalogVersion> versions = catalogVersionRepository.select(CatalogVersion.builder()
                    .catalogId(catalog.getCatalogId()).tenantId(catalog.getTenantId()).build());
            for (CatalogVersion version : versions) {
                version.setActiveFlag(O2MdConsoleConstants.ACTIVE_FLAG.FORBIDDEN);
            }
            catalogVersionRepository.batchUpdateByPrimaryKeySelective(versions);
        }
        catalogRepository.updateByPrimaryKeySelective(catalog);
    }
}
