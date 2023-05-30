package org.o2.metadata.client.infra.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.o2.metadata.client.domain.dto.FreightDTO;
import org.o2.metadata.client.infra.feign.FreightRemoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 运费
 *
 * @author yipeng.zhu@hand-china.com 2021-07-14
 **/
@Component
@Slf4j
public class FreightRemoteServiceImpl implements FreightRemoteService {

    @Override
    public ResponseEntity<String> getFreightTemplate(FreightDTO freight, Long organizationId) {
        log.error("Error getFreightTemplate, params[freight = {},organizationId= {}]", freight, organizationId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> listFreightTemplates(List<FreightDTO> freightList, Long organizationId) {
        log.error("Error listFreightTemplates, params[freight = {},organizationId= {}]", freightList, organizationId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> getFreightTemplateBatchTenant(Map<Long, FreightDTO> freightMap) {
        log.error("Error getFreightTemplateBatchTenant, params = {}", freightMap);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> listFreightTemplatesBatchTenant(Map<Long, List<FreightDTO>> freightMap) {
        log.error("Error listFreightTemplatesBatchTenant, params = {}", freightMap);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
