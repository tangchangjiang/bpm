package org.o2.feignclient.metadata.infra.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.o2.feignclient.metadata.domain.dto.StaticResourceQueryDTO;
import org.o2.feignclient.metadata.domain.dto.StaticResourceSaveDTO;
import org.o2.feignclient.metadata.infra.feign.StaticResourceRemoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


import java.util.List;


/**
 * description
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/07/30 15:36
 */
@Component
@Slf4j
public class StaticResourceRemoteServiceImpl implements StaticResourceRemoteService {

    @Override
    public ResponseEntity<String> queryResourceCodeUrlMap(StaticResourceQueryDTO staticResourceQueryDTO) {
        log.error("Error queryResourceCodeUrlMap, params[resourceCodeList = {}, tenantId = {}]", staticResourceQueryDTO.getResourceCodeList(), staticResourceQueryDTO.getTenantId());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> saveResource(List<StaticResourceSaveDTO> staticResourceSaveDTOList) {
        for (StaticResourceSaveDTO dto : staticResourceSaveDTOList) {
            log.error("Error saveResource, params[resourceCode = {}, resourceUrl = {}, tenantId = {}]",
                    dto.getResourceCode(), dto.getResourceUrl(), dto.getTenantId());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}