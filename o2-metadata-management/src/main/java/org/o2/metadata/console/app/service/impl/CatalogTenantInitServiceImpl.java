package org.o2.metadata.console.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.initialize.domain.context.TenantInitContext;
import org.o2.metadata.console.app.service.CatalogTenantInitService;
import org.o2.metadata.console.infra.constant.TenantInitConstants;
import org.o2.metadata.console.infra.entity.Catalog;
import org.o2.metadata.console.infra.entity.CatalogVersion;
import org.o2.metadata.console.infra.repository.CatalogRepository;
import org.o2.metadata.console.infra.repository.CatalogVersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    public void tenantInitialize(TenantInitContext context) {
        log.info("initializeCatalogAndVersion start, tenantId[{}]", context.getTargetTenantId());
        // 1. 查询平台租户（默认OW-1）
        final List<Catalog> platformCatalogs = selectCatalog(context.getSourceTenantId(), Arrays.asList(context.getParamMap().get(TenantInitConstants.InitBaseParam.BASE_CATALOG).split(",")));
        if (CollectionUtils.isEmpty(platformCatalogs)) {
            log.info("platformCatalogs is empty.");
            return;
        }

        // 2. 查询目标租户是否存在数据
        final List<Catalog> targetCatalogs = selectCatalog(context.getTargetTenantId(), Arrays.asList(context.getParamMap().get(TenantInitConstants.InitBaseParam.BASE_CATALOG).split(",")));
        // 3. 操作目录
        handleCatalog(targetCatalogs, platformCatalogs, context.getSourceTenantId(), context.getTargetTenantId());
        // 4. 操作目录版本
        handleCatalogVersion(context);
        log.info("initializeCatalogAndVersion finish, tenantId[{}]", context.getTargetTenantId());
    }

    @Override
    public void tenantInitializeBusiness(TenantInitContext context) {
        log.info("Business :initializeCatalogAndVersion start, tenantId[{}]", context.getTargetTenantId());
        // 1. 查询平台租户（默认OW-1）
        final List<Catalog> platformCatalogs = selectCatalog(context.getSourceTenantId(), Arrays.asList(context.getParamMap().get(TenantInitConstants.InitBusinessParam.BUSINESS_CATALOG).split(",")));

        if (CollectionUtils.isEmpty(platformCatalogs)) {
            log.info(" Business : Catalogs is empty.");
            return;
        }

        // 2. 查询目标租户是否存在数据
        final List<Catalog> targetCatalogs = selectCatalog(context.getTargetTenantId(), Arrays.asList(context.getParamMap().get(TenantInitConstants.InitBusinessParam.BUSINESS_CATALOG).split(",")));
        // 3. 操作目录
        handleCatalog(targetCatalogs, platformCatalogs, context.getSourceTenantId(), context.getTargetTenantId());
        // 4. 操作目录版本
        handleCatalogVersion(context);
        log.info("Business :  initializeCatalogAndVersion finish, tenantId[{}]", context.getTargetTenantId());
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
     * @return 目录版本
     */
    private List<CatalogVersion> selectCatalogVersion(Long tenantId, TenantInitContext context) {
        CatalogVersion query = new CatalogVersion();
        query.setTenantId(tenantId);
        query.setCatalogCodes(Arrays.asList(context.getParamMap().get(TenantInitConstants.InitBusinessParam.BUSINESS_CATALOG).split(",")));
        return catalogVersionRepository.listByCondition(query);
    }

    /**
     * 更新& 插入租户数据
     *
     * @param oldList        目标租户已存在的
     * @param initList       目标租户 需要初始化的数据
     * @param sourceTenantId 源租户
     * @param targetTenantId 目标租户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleCatalog(List<Catalog> oldList, List<Catalog> initList, Long sourceTenantId, Long targetTenantId) {
        // 3.更新&插入目录
        List<Catalog> addList = new ArrayList<>(16);
        List<Catalog> updateList = new ArrayList<>(16);
        for (Catalog init : initList) {
            String initCode = init.getCatalogCode();
            boolean addFlag = true;
            if (CollectionUtils.isEmpty(oldList)) {
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
        // 5. 更新
        catalogRepository.batchUpdateByPrimaryKey(updateList);
        catalogRepository.batchInsertSelective(addList);

    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleCatalogVersion(TenantInitContext context) {
        // 查询目标租户的目标版本
        List<Catalog> catalogs = selectCatalog(context.getTargetTenantId(),Arrays.asList(context.getParamMap().get(TenantInitConstants.InitBusinessParam.BUSINESS_CATALOG).split(",")));
        Map<String,Long> catalogMap = new HashMap<>(catalogs.size());
        for (Catalog catalog : catalogs) {
            catalogMap.put(catalog.getCatalogCode(),catalog.getCatalogId());
        }
        // 查询源租户
        List<CatalogVersion> sourceCatalogVersions = selectCatalogVersion(context.getSourceTenantId(), context);
        // 查询目标租户(已存在的）
        List<CatalogVersion> targetCatalogVersions = selectCatalogVersion(context.getTargetTenantId(), context);

        List<CatalogVersion> addVersionList = new ArrayList<>(16);
        List<CatalogVersion> updateVersionList = new ArrayList<>(16);
        for (CatalogVersion init : sourceCatalogVersions) {
            String initKey = init.getCatalogVersionCode();
            Long catalogId = catalogMap.get(init.getCatalogCode());
            if (catalogId == null) {
                log.error("sourceTenantId: {},catalogCode {} ；The catalog  is empty ",context.getTargetTenantId(),init.getCatalogCodes());
                continue;
            }
            init.setCatalogId(catalogId);
            boolean addFlag = true;
            if (CollectionUtils.isEmpty(targetCatalogVersions)) {
                addVersionList.add(init);
                continue;
            }
            for (CatalogVersion old : targetCatalogVersions) {
                String oldKey = old.getCatalogVersionCode();
                if (initKey.equals(oldKey)) {
                    init.setTenantId(context.getTargetTenantId());
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
        for (CatalogVersion catalogVersion : addVersionList) {
            catalogVersion.setCatalogVersionId(null);
            catalogVersion.setTenantId(context.getTargetTenantId());
        }
        catalogVersionRepository.batchUpdateByPrimaryKeySelective(updateVersionList);
        catalogVersionRepository.batchInsert(addVersionList);
    }
}
