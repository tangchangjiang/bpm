package org.o2.metadata.console.app.service.impl;

import io.choerodon.core.exception.CommonException;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.console.api.dto.CatalogRelVersionQueryDTO;
import org.o2.metadata.console.api.dto.CatalogVersionQueryInnerDTO;
import org.o2.metadata.console.app.service.CatalogVersionService;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.entity.Catalog;
import org.o2.metadata.console.infra.entity.CatalogVersion;
import org.o2.metadata.console.infra.repository.CatalogRepository;
import org.o2.metadata.console.infra.repository.CatalogVersionRepository;
import org.springframework.stereotype.Service;

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
        CatalogVersion query = catalogVersionRepository.selectOne(CatalogVersion.builder().tenantId(tenantId).catalogVersionCode(catalogVersion.getCatalogVersionCode()).build());
        if (null != query) {
            throw new CommonException(MetadataConstants.ErrorCode.O2MD_CATALOG_VERSION_UNIQUE);
        }
        catalogVersionRepository.insertSelective(catalogVersion);
    }

    @Override
    public Map<String, String> listCatalogVersions(CatalogVersionQueryInnerDTO catalogVersionQueryInnerDTO, Long organizationId) {
        Map<String,String> map = new HashMap<>(16);
        if (null == catalogVersionQueryInnerDTO) {
            return map;
        }
        if (CollectionUtils.isNotEmpty(catalogVersionQueryInnerDTO.getCatalogCodes())) {
            List<Catalog> catalogList = catalogRepository.batchSelectByCodes(catalogVersionQueryInnerDTO.getCatalogCodes(),organizationId);
            if (CollectionUtils.isNotEmpty(catalogList)) {
                for (Catalog catalog : catalogList) {
                    map.put(catalog.getCatalogCode(), catalog.getCatalogName());
                }
            }
        }

        if (CollectionUtils.isNotEmpty(catalogVersionQueryInnerDTO.getCatalogCodeVersionCodes())) {
            List<CatalogVersion> catalogVersions =catalogVersionRepository.batchSelectByCodes(catalogVersionQueryInnerDTO.getCatalogCodeVersionCodes(),organizationId);
            if (CollectionUtils.isNotEmpty(catalogVersions)) {
                for (CatalogVersion catalogVersion : catalogVersions) {
                    map.put(catalogVersion.getCatalogVersionCode(), catalogVersion.getCatalogVersionName());
                }
            }
        }

        return map;
    }

    @Override
    public List<CatalogVersion> catalogRelVersion(CatalogRelVersionQueryDTO queryDTO) {
        return catalogVersionRepository.catalogRelVersion(queryDTO);
    }
}
