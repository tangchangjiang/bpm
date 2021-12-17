package org.o2.metadata.client.infra.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.o2.metadata.client.domain.dto.FreightDTO;
import org.o2.metadata.client.infra.feign.FreightRemoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 *
 * 运费
 *
 * @author yipeng.zhu@hand-china.com 2021-07-14
 **/
@Component
@Slf4j
public class FreightServiceRemoteServiceImpl implements FreightRemoteService {

    @Override
    public ResponseEntity<String> getFreightTemplate(FreightDTO freight, Long organizationId) {
        log.error("Error getFreightTemplate, params[freight = {}, organizationId = {}]", freight, organizationId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> getDefaultTemplate(Long organizationId) {
        log.error("Error getDefaultTemplate, params[organizationId = {}]", organizationId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
