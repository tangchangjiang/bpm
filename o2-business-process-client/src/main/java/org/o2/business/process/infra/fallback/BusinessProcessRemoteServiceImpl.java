package org.o2.business.process.infra.fallback;

import lombok.extern.slf4j.Slf4j;
import org.o2.business.process.infra.BusinessProcessRemoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * 流程器远程服务实现
 *
 * @author miao.chen01@hand-china.com 2021-07-23
 */
@Slf4j
@Service
public class BusinessProcessRemoteServiceImpl implements BusinessProcessRemoteService {

    @Override
    public ResponseEntity<String> getPipelineByCode(Long organizationId, String code) {
        log.error("Error getPipelineByCode, params[organizationId = {}, code = {}]", organizationId, code);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> getProcessLastModifiedTime(Long organizationId, String processCode) {
        log.error("Error getProcessLastModifiedTime, params[organizationId = {}, code = {}]", organizationId, processCode);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
