package org.o2.feignclient.metadata.infra.feign.fallback;

import org.o2.feignclient.metadata.infra.feign.OnlineShopRelWarehouseRemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *
 *
 *
 * @author yipeng.zhu@hand-china.com 2021-07-13
 **/
public class OnlineShopRelWarehouseRemoteServiceImpl implements OnlineShopRelWarehouseRemoteService {
    private static final Logger logger = LoggerFactory.getLogger(OnlineShopRelWarehouseRemoteServiceImpl.class);
    @Override
    public ResponseEntity<String> listOnlineShopRelWarehouses(String onlineShopCode, Long tenantId) {
        logger.error("Error listOnlineShopRelWarehouses, params[tenantId = {}, code = {}]", tenantId, onlineShopCode);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
