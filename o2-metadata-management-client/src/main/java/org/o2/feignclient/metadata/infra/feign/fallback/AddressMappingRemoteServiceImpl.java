package org.o2.feignclient.metadata.infra.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.o2.feignclient.metadata.domain.dto.AddressMappingQueryInnerDTO;
import org.o2.feignclient.metadata.infra.feign.AddressMappingRemoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * 地址匹配
 *
 * @author yipeng.zhu@hand-china.com 2021-08-06
 **/
@Component
@Slf4j
public class AddressMappingRemoteServiceImpl implements AddressMappingRemoteService {

    @Override
    public ResponseEntity<String> listAddressMappings(List<AddressMappingQueryInnerDTO> addressMappingQueryInnerDTOList, Long organizationId) {
        log.error("Error listAddressMappings, params[tenantId = {}, addressMappingQueryIntDTO = {}]", organizationId, addressMappingQueryInnerDTOList);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> listNeighboringRegions(Long organizationId) {
        log.error("Error listNeighboringRegions, params[tenantId = {}]", organizationId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }
}
