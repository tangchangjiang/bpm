package org.o2.metadata.client.infra.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.o2.metadata.client.infra.feign.PosRemoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author chao.yang05@hand-china.com 2022/4/13
 */
@Component
@Slf4j
public class PosRemoteServiceImpl implements PosRemoteService {
    @Override
    public ResponseEntity<String> getPosPickUpInfo(String posCode, Long tenantId) {
        log.error("Error getPosPickUpInfo, params[posCode = {},tenantId = {}]", posCode,tenantId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
