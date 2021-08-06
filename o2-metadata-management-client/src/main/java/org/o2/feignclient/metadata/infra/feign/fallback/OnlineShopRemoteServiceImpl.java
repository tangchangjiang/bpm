package org.o2.feignclient.metadata.infra.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.o2.feignclient.metadata.infra.feign.OnlineShopRemoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * description
 *
 * @author zhilin.ren@hand-china.com 2021/08/05 16:48
 */
@Service
@Slf4j
public class OnlineShopRemoteServiceImpl implements OnlineShopRemoteService {

    @Override
    public ResponseEntity<String> getOnlineShopCode(Long organizationId, String platformCode, String shopName) {
        log.error("Error listOnlineShopRelWarehouses, params[tenantId = {}, platformCode = {},shopName= {}]",organizationId, platformCode,shopName);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
