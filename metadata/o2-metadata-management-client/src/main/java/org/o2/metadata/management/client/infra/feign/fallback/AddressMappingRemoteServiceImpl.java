package org.o2.metadata.management.client.infra.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.o2.metadata.management.client.domain.dto.AddressMappingQueryInnerDTO;
import org.o2.metadata.management.client.infra.feign.AddressMappingRemoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 地址匹配
 *
 * @author yipeng.zhu@hand-china.com 2021-08-06
 **/
@Component
@Slf4j
public class AddressMappingRemoteServiceImpl implements AddressMappingRemoteService {

    @Override
    public ResponseEntity<String> listAllAddressMappings(AddressMappingQueryInnerDTO queryInnerDTO, Long organizationId) {
        log.error("Error listAllAddressMappings, params[tenantId = {}]", organizationId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> listNeighboringRegions(Long organizationId) {
        log.error("Error listNeighboringRegions, params[tenantId = {}]", organizationId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }
}
