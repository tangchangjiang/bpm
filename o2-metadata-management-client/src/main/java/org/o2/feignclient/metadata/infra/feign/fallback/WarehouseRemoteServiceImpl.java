package org.o2.feignclient.metadata.infra.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.o2.feignclient.metadata.infra.feign.WarehouseRemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 *
 * 仓库
 *
 * @author yipeng.zhu@hand-china.com 2021-07-13
 **/
@Component
@Slf4j
public class WarehouseRemoteServiceImpl implements WarehouseRemoteService {
    private static final Logger logger = LoggerFactory.getLogger(WarehouseRemoteServiceImpl.class);
    @Override
    public ResponseEntity<String> getWarehouse(Long tenantId, String warehouseCode) {
        logger.error("Error getWarehouse, params[tenantId = {}, warehouseCode = {}]", tenantId, warehouseCode);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
