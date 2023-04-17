package org.o2.metadata.management.client.infra.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.o2.metadata.management.client.domain.dto.SystemParameterQueryInnerDTO;
import org.o2.metadata.management.client.infra.feign.SysParameterRemoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
@Component("sysParameterRemoteManagementService")
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
    public ResponseEntity<String> updateSysParameter(SystemParameterQueryInnerDTO systemParameterQueryInnerDTO, Long organizationId) {
        log.error("Error updateSysParameter, params[tenantId = {}, systemParameterDTO = {}]", organizationId, systemParameterQueryInnerDTO);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> getSizeSystemParameter(Long organizationId, String paramCode) {
        log.error("Error getSizeSystemParameter, params[tenantId = {}, paramCode = {}]", organizationId, paramCode);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
