package org.o2.feignclient.metadata.infra.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.o2.feignclient.metadata.domain.dto.PlatformQueryInnerDTO;
import org.o2.feignclient.metadata.infra.feign.PlatformRemoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * description 平台信息匹配
 *
 * @author zhilin.ren@hand-china.com 2021/08/02 22:14
 */
@Component
@Slf4j
public class PlatformRemoteServiceImpl implements PlatformRemoteService {

    @Override
    public ResponseEntity<String> listPlatforms(PlatformQueryInnerDTO platformQueryInnerDTO, Long organizationId) {
        log.error("Error listPlatforms, params[tenantId = {}, platformDTO = {}]",organizationId, platformQueryInnerDTO);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
