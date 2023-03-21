package org.o2.metadata.management.client.infra.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.o2.metadata.management.client.domain.dto.CarrierDeliveryRangeDTO;
import org.o2.metadata.management.client.domain.dto.CarrierMappingQueryInnerDTO;
import org.o2.metadata.management.client.domain.dto.CarrierQueryInnerDTO;
import org.o2.metadata.management.client.infra.feign.CarrierRemoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

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
    public ResponseEntity<String> listCarriers(CarrierQueryInnerDTO carrierQueryInnerDTO, Long organizationId) {
        log.error("Error listCarriers, params[tenantId = {}, carrierDTO = {}]", organizationId, carrierQueryInnerDTO);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> listCarrierMappings(CarrierMappingQueryInnerDTO carrierMappingQueryInnerDTO, Long organizationId) {
        log.error("Error listCarriers, params[tenantId = {}, carrierDTO = {}]", organizationId, carrierMappingQueryInnerDTO);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> importList(Long organizationId) {
        log.error("Error importList, params[tenantId = {}]", organizationId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> checkDeliveryRange(Long organizationId, CarrierDeliveryRangeDTO carrierDeliveryRangeDTO) {
        log.error("Error checkDeliveryRange, params[tenantId = {},carrierDeliveryRangeDTO={}", organizationId, carrierDeliveryRangeDTO);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
