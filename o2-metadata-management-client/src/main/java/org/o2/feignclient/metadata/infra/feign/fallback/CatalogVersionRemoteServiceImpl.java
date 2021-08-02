package org.o2.feignclient.metadata.infra.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.o2.feignclient.metadata.domain.dto.CatalogVersionDTO;
import org.o2.feignclient.metadata.infra.feign.CatalogVersionRemoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 *
 * 目录版本
 *
 * @author yipeng.zhu@hand-china.com 2021-07-30
 **/
@Component
@Slf4j
public class CatalogVersionRemoteServiceImpl implements CatalogVersionRemoteService {

    @Override
    public ResponseEntity<String> listCatalogVersions(CatalogVersionDTO catalogVersionDTO, Long organizationId) {
        log.error("Error batchSelectNameByCode, params[catalogVersionDTO = {}, organizationId = {}]", catalogVersionDTO,organizationId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
