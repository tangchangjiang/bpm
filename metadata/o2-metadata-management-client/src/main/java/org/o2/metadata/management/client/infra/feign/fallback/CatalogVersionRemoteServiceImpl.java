package org.o2.metadata.management.client.infra.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.o2.metadata.management.client.domain.dto.CatalogQueryInnerDTO;
import org.o2.metadata.management.client.domain.dto.CatalogVersionQueryInnerDTO;
import org.o2.metadata.management.client.infra.feign.CatalogVersionRemoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 目录版本
 *
 * @author yipeng.zhu@hand-china.com 2021-07-30
 **/
@Component
@Slf4j
public class CatalogVersionRemoteServiceImpl implements CatalogVersionRemoteService {

    @Override
    public ResponseEntity<String> listCatalogVersions(CatalogVersionQueryInnerDTO catalogVersionQueryInnerDTO, Long organizationId) {
        log.error("Error batchSelectNameByCode, params[catalogVersionDTO = {}, organizationId = {}]", catalogVersionQueryInnerDTO, organizationId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> listCatalogAndVersion(CatalogQueryInnerDTO queryInner, Long organizationId) {
        log.error("Error batchSelectNameByCode, params[catalogVersionDTO = {}, organizationId = {}]", queryInner, organizationId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
