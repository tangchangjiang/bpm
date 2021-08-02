package org.o2.feignclient.metadata.infra.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.o2.feignclient.metadata.domain.dto.PosAddressDTO;
import org.o2.feignclient.metadata.infra.feign.PosRemoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 *
 * 服务点
 * @author yipeng.zhu@hand-china.com 2021-08-02
 **/
@Component
@Slf4j
public class PosRemoteServiceImpl implements PosRemoteService {
    @Override
    public ResponseEntity<String> listPosAddress(PosAddressDTO posAddressDTO, Long organizationId) {
        log.error("Error listPosAddress, params[posAddressDTO = {}, organizationId = {}]", posAddressDTO,organizationId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
