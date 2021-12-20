package org.o2.metadata.console.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.console.app.service.CatalogTenantInitService;
import org.o2.metadata.console.infra.entity.Catalog;
import org.o2.metadata.console.infra.entity.CatalogVersion;
import org.o2.metadata.console.infra.entity.OnlineShop;
import org.o2.metadata.console.infra.repository.CatalogRepository;
import org.o2.metadata.console.infra.repository.CatalogVersionRepository;
import org.springframework.stereotype.Service;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * description
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/17 16:45
 */
@Slf4j
@Service
public class CatalogTenantInitServiceImpl implements CatalogTenantInitService {

    private final CatalogRepository catalogRepository;

    private final CatalogVersionRepository catalogVersionRepository;

    public CatalogTenantInitServiceImpl(CatalogRepository catalogRepository, CatalogVersionRepository catalogVersionRepository) {
        this.catalogRepository = catalogRepository;
        this.catalogVersionRepository = catalogVersionRepository;
    }

    @Override
    public void tenantInitialize(long sourceTenantId, Long targetTenantId) {
        log.info("initializeCatalogAndVersion start, tenantId[{}]", targetTenantId);
        // 1. 查询平台租户（默认OW-1）
        final List<Catalog> platformCatalogs = catalogRepository.selectByCondition(Condition.builder(Catalog.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(Catalog.FIELD_TENANT_ID, sourceTenantId)
                        .andIn(Catalog.FIELD_CATALOG_CODE, Arrays.asList("MASTER", "OW")))
                .build());

        if (CollectionUtils.isEmpty(platformCatalogs)) {
            log.info("platformCatalogs is empty.");
            return;
        }

        // 2. 查询目标租户是否存在数据
        final List<Catalog> targetCatalogs = catalogRepository.selectByCondition(Condition.builder(Catalog.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(OnlineShop.FIELD_TENANT_ID, targetTenantId)
                        .andIn(Catalog.FIELD_CATALOG_CODE, Arrays.asList("MASTER", "OW")))
                .build());

        if (CollectionUtils.isNotEmpty(targetCatalogs)) {
            // 2.1 删除目标租户数据
            catalogRepository.batchDeleteByPrimaryKey(targetCatalogs);

            // 2.2 关联目录版本
            final List<Long> catalogIds = targetCatalogs.stream().map(Catalog::getCatalogId).collect(Collectors.toList());
            final List<CatalogVersion> targetCatalogVersions = catalogVersionRepository.selectByCondition(Condition.builder(Catalog.class)
                    .andWhere(Sqls.custom()
                            .andEqualTo(Catalog.FIELD_TENANT_ID, targetTenantId)
                            .andIn(Catalog.FIELD_CATALOG_ID, catalogIds))
                    .build());
            if (CollectionUtils.isNotEmpty(targetCatalogVersions)) {
                catalogVersionRepository.batchDeleteByPrimaryKey(targetCatalogVersions);
            }
        }

        // 3. 插入平台数据到目标租户
        platformCatalogs.forEach(catalog -> {
            catalog.setCatalogId(null);
            catalog.setTenantId(targetTenantId);

            // 查询关联表
            final List<CatalogVersion> catalogVersions = catalogVersionRepository.selectByCondition(Condition.builder(Catalog.class)
                    .andWhere(Sqls.custom()
                            .andEqualTo(Catalog.FIELD_TENANT_ID, sourceTenantId)
                            .andEqualTo(Catalog.FIELD_CATALOG_ID, catalog.getCatalogId()))
                    .build());

            // 插入，catalog携带插入后主键
            catalogRepository.insert(catalog);

            catalogVersions.forEach(catalogVersion -> {
                catalogVersion.setCatalogVersionId(null);
                catalogVersion.setCatalogId(catalog.getCatalogId());
                catalogVersion.setTenantId(targetTenantId);
            });

            catalogVersionRepository.batchInsert(catalogVersions);
        });
    }
}
