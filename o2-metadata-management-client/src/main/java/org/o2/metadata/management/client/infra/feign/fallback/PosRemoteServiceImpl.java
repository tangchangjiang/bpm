package org.o2.metadata.management.client.infra.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.o2.metadata.management.client.domain.dto.PosAddressQueryInnerDTO;
import org.o2.metadata.management.client.infra.feign.PosRemoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * 服务点
 * @author yipeng.zhu@hand-china.com 2021-08-02
 **/
@Component
@Slf4j
public class PosRemoteServiceImpl implements PosRemoteService {
    @Override
    public ResponseEntity<String> listPosAddress(PosAddressQueryInnerDTO posAddressQueryInnerDTO, Long organizationId) {
        log.error("Error listPosAddress, params[posAddressDTO = {}, organizationId = {}]", posAddressQueryInnerDTO,organizationId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> listPoseName(Long organizationId, List<String> posCodes) {
        log.error("Error listPoseName, params[posCodes = {}, organizationId = {}]", posCodes,organizationId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
