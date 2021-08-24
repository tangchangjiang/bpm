package org.o2.feignclient.metadata.infra.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.o2.feignclient.metadata.infra.feign.CarrierRemoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 *
 *
 *
 * @author yipeng.zhu@hand-china.com 2021-08-20
 **/
@Component
@Slf4j
public class CarrierRemoteServiceImpl implements CarrierRemoteService {
    @Override
    public ResponseEntity<String> listCarriers(Long organizationId) {
        log.error("Error listCarriers, params[organizationId= {}]", organizationId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}