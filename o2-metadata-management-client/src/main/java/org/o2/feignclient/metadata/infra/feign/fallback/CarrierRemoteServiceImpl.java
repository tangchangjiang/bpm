package org.o2.feignclient.metadata.infra.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.o2.feignclient.metadata.domain.dto.CarrierDTO;
import org.o2.feignclient.metadata.infra.feign.CarrierRemoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * 承运商
 *
 * @author yipeng.zhu@hand-china.com 2021-08-01
 **/
@Component
@Slf4j
public class CarrierRemoteServiceImpl implements CarrierRemoteService {
    @Override
    public ResponseEntity<String> listCarriers(List<CarrierDTO> carrierList, Long organizationId) {
        log.error("Error listCarriers, params[tenantId = {}, carrierList = {}]", organizationId, carrierList);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
