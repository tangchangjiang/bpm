package org.o2.feignclient.metadata.infra.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.o2.feignclient.metadata.domain.dto.FreightDTO;
import org.o2.feignclient.metadata.infra.feign.FreightRemoteService;
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
    public ResponseEntity<String> getFreightTemplate(FreightDTO freight) {
        log.error("Error getFreightTemplate, params[freight = {}]", freight);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}