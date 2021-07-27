package org.o2.feignclient.metadata.infra.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.o2.feignclient.metadata.domain.dto.SystemParameterDTO;
import org.o2.feignclient.metadata.infra.feign.SysParameterRemoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
@Component
@Slf4j
public class SysParameterRemoteServiceImpl implements SysParameterRemoteService {


    @Override
    public ResponseEntity<String> getSystemParameter(Long organizationId, String paramCode) {
        log.error("Error getSystemParameter, params[tenantId = {}, code = {}]", organizationId, paramCode);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> listSystemParameters(Long organizationId, List<String> paramCodes) {
        log.error("Error listSystemParameters, params[tenantId = {}, code = {}]", organizationId, paramCodes);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> updateSysParameter(SystemParameterDTO systemParameterDTO, Long organizationId) {
        log.error("Error updateSysParameter, params[tenantId = {}, systemParameterDTO = {}]", organizationId, systemParameterDTO);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
