package org.o2.feignclient.metadata.infra.feign;

import io.swagger.annotations.ApiParam;
import org.o2.feignclient.metadata.domain.dto.CatalogVersionDTO;
import org.o2.feignclient.metadata.infra.constants.O2Service;
import org.o2.feignclient.metadata.infra.feign.fallback.CatalogVersionRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 *
 * 目录版本
 *
 * @author yipeng.zhu@hand-china.com 2021-07-30
 **/
@FeignClient(
        value = O2Service.Metadata.NAME,
        path = "/v1",
        fallback = CatalogVersionRemoteServiceImpl.class
)
public interface CatalogVersionRemoteService {
    /**
     * 批量查询目录版本
     * @param catalogVersionList 目录版本集合
     * @param organizationId 租户ID
     * @return  map
     */
    @GetMapping("/{organizationId}/catalogVersion-internal/select-name")
    ResponseEntity<String> batchSelectNameByCode(@RequestParam List<CatalogVersionDTO> catalogVersionList,
                                               @PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId);
}