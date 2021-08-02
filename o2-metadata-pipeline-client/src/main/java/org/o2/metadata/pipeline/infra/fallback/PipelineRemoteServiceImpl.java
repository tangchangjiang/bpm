package org.o2.metadata.pipeline.infra.fallback;

import lombok.extern.slf4j.Slf4j;
import org.o2.metadata.pipeline.infra.PipelineRemoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * 流程器远程服务实现
 *
 * @author miao.chen01@hand-china.com 2021-07-23
 */
@Slf4j
@Service
public class PipelineRemoteServiceImpl implements PipelineRemoteService {

    @Override
    public ResponseEntity<String> getPipelineByCode(Long organizationId, String code) {
        log.error("Error getPipelineByCode, params[organizationId = {}, code = {}]", organizationId, code);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
