package org.o2.metadata.client.infra.feign.fallback;

import org.o2.metadata.client.infra.feign.SysParameterRemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
@Component
public class SysParameterRemoteServiceImpl implements SysParameterRemoteService {
    private static final Logger logger = LoggerFactory.getLogger(SysParameterRemoteServiceImpl.class);

    @Override
    public ResponseEntity<String> getSystemParameter(Long organizationId, String paramCode) {
        logger.error("Error saveSysParameter, params[tenantId = {}, code = {}]", organizationId, paramCode);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> listSystemParameters(List<String> paramCodes, Long organizationId) {
        logger.error("Error listSystemParameters, params[tenantId = {}, paramCodes = {}]", organizationId, paramCodes);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
