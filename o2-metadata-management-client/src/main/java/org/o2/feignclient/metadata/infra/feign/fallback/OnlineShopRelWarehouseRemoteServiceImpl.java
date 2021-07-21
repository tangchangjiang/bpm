package org.o2.feignclient.metadata.infra.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.o2.feignclient.metadata.infra.feign.OnlineShopRelWarehouseRemoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 *
 *
 *
 * @author yipeng.zhu@hand-china.com 2021-07-13
 **/
@Component
@Slf4j
public class OnlineShopRelWarehouseRemoteServiceImpl implements OnlineShopRelWarehouseRemoteService {
    @Override
    public ResponseEntity<String> listOnlineShopRelWarehouses(String onlineShopCode, Long tenantId) {
        log.error("Error listOnlineShopRelWarehouses, params[tenantId = {}, code = {}]", tenantId, onlineShopCode);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
