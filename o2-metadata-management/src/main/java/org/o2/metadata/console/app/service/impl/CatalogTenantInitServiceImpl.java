package org.o2.metadata.console.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.console.app.service.CatalogTenantInitService;
import org.o2.metadata.console.infra.constant.TenantInitConstants;
import org.o2.metadata.console.infra.entity.Catalog;
import org.o2.metadata.console.infra.entity.CatalogVersion;
import org.o2.metadata.console.infra.repository.CatalogRepository;
import org.o2.metadata.console.infra.repository.CatalogVersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
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
    @Transactional(rollbackFor = Exception.class)
    public void tenantInitialize(Long sourceTenantId, Long targetTenantId) {
        log.info("initializeCatalogAndVersion start, tenantId[{}]", targetTenantId);
        // 1. 查询平台租户（默认OW-1）
        final List<Catalog> platformCatalogs = selectCatalog(sourceTenantId, TenantInitConstants.CatalogBasis.CATALOG_CODE);

        if (CollectionUtils.isEmpty(platformCatalogs)) {
            log.info("platformCatalogs is empty.");
            return;
        }

        // 2. 查询目标租户是否存在数据
        final List<Catalog> targetCatalogs = selectCatalog(targetTenantId, TenantInitConstants.CatalogBasis.CATALOG_CODE);
        handleData(targetCatalogs, platformCatalogs, sourceTenantId, targetTenantId);
        log.info("initializeCatalogAndVersion finish, tenantId[{}]", targetTenantId);
    }

    @Override
    public void tenantInitializeBusiness(long sourceTenantId, Long targetTenantId) {
        log.info("Business :initializeCatalogAndVersion start, tenantId[{}]", targetTenantId);
        // 1. 查询平台租户（默认OW-1）
        final List<Catalog> platformCatalogs = selectCatalog(sourceTenantId, TenantInitConstants.CatalogBusiness.CATALOG_CODE);

        if (CollectionUtils.isEmpty(platformCatalogs)) {
            log.info(" Business : Catalogs is empty.");
            return;
        }

        // 2. 查询目标租户是否存在数据
        final List<Catalog> targetCatalogs = selectCatalog(targetTenantId, TenantInitConstants.CatalogBusiness.CATALOG_CODE);
        handleData(targetCatalogs, platformCatalogs, sourceTenantId, targetTenantId);
        log.info("Business :  initializeCatalogAndVersion finish, tenantId[{}]", targetTenantId);
    }

    /**
     * 查询目录
     *
     * @param tenantId     租户ID
     * @param catalogCodes 目录编码
     * @return 目录
     */
    private List<Catalog> selectCatalog(Long tenantId, List<String> catalogCodes) {
        return catalogRepository.selectByCondition(Condition.builder(Catalog.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(Catalog.FIELD_TENANT_ID, tenantId)
                        .andIn(Catalog.FIELD_CATALOG_CODE, catalogCodes))
                .build());
    }

    /**
     * 查询目录版本
     *
     * @param tenantId   租户id
     * @param catalogIds 目录ID
     * @return 目录版本
     */
    private List<CatalogVersion> selectCatalogVersion(Long tenantId, List<Long> catalogIds) {
        return catalogVersionRepository.selectByCondition(Condition.builder(Catalog.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(Catalog.FIELD_TENANT_ID, tenantId)
                        .andIn(Catalog.FIELD_CATALOG_ID, catalogIds))
                .build());
    }

    /**
     * 更新& 插入租户数据
     *
     * @param oldList        目标租户已存在的
     * @param initList       目标租户 需要初始化的数据
     * @param sourceTenantId 源租户
     * @param targetTenantId 目标租户
     */
    private void handleData(List<Catalog> oldList, List<Catalog> initList, Long sourceTenantId, Long targetTenantId) {
        // 3.更新&插入目录
        List<Catalog> addList = new ArrayList<>(16);
        List<Catalog> updateList = new ArrayList<>(16);
        for (Catalog init : initList) {
            String initCode = init.getCatalogCode();
            boolean addFlag = true;
            if (CollectionUtils.isNotEmpty(oldList)) {
                addList.add(init);
                continue;
            }
            for (Catalog old : oldList) {
                String oldCode = old.getCatalogCode();
                if (initCode.equals(oldCode)) {
                    init.setCatalogId(old.getCatalogId());
                    init.setTenantId(targetTenantId);
                    init.setObjectVersionNumber(old.getObjectVersionNumber());
                    updateList.add(init);
                    addFlag = false;
                    break;
                }
            }
            if (addFlag) {
                addList.add(init);
            }
        }
        for (Catalog catalog : addList) {
            catalog.setTenantId(targetTenantId);
            catalog.setCatalogId(null);
        }
        //  4.更新&插入目录版本
        List<Long> catalogIds = initList.stream().map(Catalog::getCatalogId).collect(Collectors.toList());
        //  4.1查询源租户
        List<CatalogVersion> sourceCatalogVersions = selectCatalogVersion(sourceTenantId, catalogIds);
        // 4.2查询目标租户
        List<CatalogVersion> targetCatalogVersions = selectCatalogVersion(targetTenantId, catalogIds);
        List<CatalogVersion> addVersionList = new ArrayList<>(16);
        List<CatalogVersion> updateVersionList = new ArrayList<>(16);
        for (CatalogVersion init : sourceCatalogVersions) {
            String initKey = init.getCatalogVersionCode() + "-" + init.getCatalogId();
            boolean addFlag = true;
            if (CollectionUtils.isNotEmpty(targetCatalogVersions)) {
                addVersionList.add(init);
                continue;
            }
            for (CatalogVersion old : targetCatalogVersions) {
                String oldKey = old.getCatalogVersionCode() + "-" + old.getCatalogId();
                if (initKey.equals(oldKey)) {
                    init.setTenantId(targetTenantId);
                    init.setCatalogVersionId(old.getCatalogVersionId());
                    init.setObjectVersionNumber(old.getObjectVersionNumber());
                    updateVersionList.add(init);
                    addFlag = false;
                    break;
                }
            }
            if (addFlag) {
                addVersionList.add(init);
            }
        }

        // 5. 更新
        catalogRepository.batchUpdateByPrimaryKey(updateList);
        catalogRepository.batchInsertSelective(addList);
        catalogVersionRepository.batchUpdateByPrimaryKeySelective(updateVersionList);
        catalogVersionRepository.batchInsert(addVersionList);
    }
}
