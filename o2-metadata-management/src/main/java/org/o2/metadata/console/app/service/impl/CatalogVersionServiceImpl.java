package org.o2.metadata.console.app.service.impl;

import io.choerodon.core.exception.CommonException;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.console.api.dto.CatalogVersionDTO;
import org.o2.metadata.console.app.service.CatalogVersionService;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.entity.Catalog;
import org.o2.metadata.console.infra.entity.CatalogVersion;
import org.o2.metadata.console.infra.repository.CatalogRepository;
import org.o2.metadata.console.infra.repository.CatalogVersionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if (MetadataConstants.ActiveFlag.FORBIDDEN.equals(catalog.getActiveFlag())) {
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
        if (MetadataConstants.ActiveFlag.FORBIDDEN.equals(catalog.getActiveFlag())) {
            catalogVersion.setActiveFlag(MetadataConstants.ActiveFlag.FORBIDDEN);
        }
        catalogVersionRepository.insertSelective(catalogVersion);
    }

    @Override
    public Map<String, String> batchSelectNameByCode(List<CatalogVersionDTO> list, Long organizationId) {
        Map<String,String> map = new HashMap<>(16);
        if (list.isEmpty()) {
            return map;
        }
        List<String> catalogCodes = new ArrayList<>(list.size());
        List<String> catalogVersionCodes = new ArrayList<>(16);
        for (CatalogVersionDTO catalog : list) {
            catalogCodes.add(catalog.getCatalogCode());
            catalogVersionCodes.addAll(catalog.getCatalogCodeVersionList());
        }
        List<Catalog> catalogList = catalogRepository.batchSelectByCodes(catalogCodes,organizationId);
        if (CollectionUtils.isNotEmpty(catalogList)) {
            for (Catalog catalog : catalogList) {
                map.put(catalog.getCatalogCode(), catalog.getCatalogName());
            }
        }
        List<CatalogVersion> catalogVersions =catalogVersionRepository.batchSelectByCodes(catalogVersionCodes,organizationId);
        if (CollectionUtils.isNotEmpty(catalogVersions)) {
            for (CatalogVersion catalogVersion : catalogVersions) {
                map.put(catalogVersion.getCatalogCode(), catalogVersion.getCatalogVersionName());
            }
        }
        return map;
    }


}
