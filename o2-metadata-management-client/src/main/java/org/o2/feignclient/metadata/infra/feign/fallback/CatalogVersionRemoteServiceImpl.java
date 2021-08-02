package org.o2.feignclient.metadata.infra.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.o2.feignclient.metadata.domain.dto.CatalogVersionDTO;
import org.o2.feignclient.metadata.infra.feign.CatalogVersionRemoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

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
    public ResponseEntity<String> batchSelectNameByCode(List<CatalogVersionDTO> catalogVersionList, Long organizationId) {
        log.error("Error batchSelectNameByCode, params[catalogVersionList = {}, organizationId = {}]", catalogVersionList,organizationId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
