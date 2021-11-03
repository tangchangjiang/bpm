package org.o2.feignclient;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.feignclient.metadata.domain.co.CatalogCO;
import org.o2.feignclient.metadata.domain.dto.CatalogQueryInnerDTO;
import org.o2.feignclient.metadata.domain.dto.CatalogVersionQueryInnerDTO;
import org.o2.feignclient.metadata.infra.feign.CatalogVersionRemoteService;

import java.util.List;
import java.util.Map;

/**
 *
 * 目录&目录版本
 *
 * @author yipeng.zhu@hand-china.com 2021-09-23
 **/
public class CatalogClient {
    private final CatalogVersionRemoteService catalogVersionRemoteService;

    public CatalogClient(CatalogVersionRemoteService catalogVersionRemoteService) {
        this.catalogVersionRemoteService = catalogVersionRemoteService;
    }

    /**
     * 批量查询目录版本
     * @param catalogVersionQueryInnerDTO 目录版本集合
     * @param tenantId 租户ID
     * @return map key:编码 value:名称
     */
    public Map<String, String> listCatalogVersions(CatalogVersionQueryInnerDTO catalogVersionQueryInnerDTO, Long tenantId) {
        return ResponseUtils.getResponse(catalogVersionRemoteService.listCatalogVersions(catalogVersionQueryInnerDTO, tenantId), new TypeReference<Map<String, String>>() {
        });
    }

    /**
     * 查询目录 父子结构
     * @param queryInner 查询条件
     * @param tenantId 租户ID
     * @return map key:编码 value:名称
     */
    public List<CatalogCO> listCatalogAndVersion(CatalogQueryInnerDTO queryInner, Long tenantId) {
        return ResponseUtils.getResponse(catalogVersionRemoteService.listCatalogAndVersion(queryInner, tenantId), new TypeReference<List<CatalogCO>>() {
        });
    }
}
