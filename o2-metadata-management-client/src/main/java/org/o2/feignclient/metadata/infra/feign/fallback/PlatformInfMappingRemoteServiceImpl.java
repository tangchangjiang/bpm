package org.o2.feignclient.metadata.infra.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.o2.feignclient.metadata.domain.dto.PlatformInfMappingDTO;
import org.o2.feignclient.metadata.infra.feign.PlatformInfMappingRemoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * description 平台信息匹配
 *
 * @author zhilin.ren@hand-china.com 2021/08/02 22:14
 */
@Component
@Slf4j
public class PlatformInfMappingRemoteServiceImpl implements PlatformInfMappingRemoteService {

    @Override
    public ResponseEntity<String> getPlatformMapping(Long organizationId, List<PlatformInfMappingDTO> platformInfMapping) {
        log.error("request error params[tenantId = {}]", organizationId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> getPlatformInfMapping(Long organizationId, String platformCode, String infTypeCode) {
        log.error("request error params[tenantId = {},platformCode = {},infTypeCode = {}]", organizationId,platformCode,infTypeCode);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
