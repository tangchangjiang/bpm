package org.o2.feignclient.metadata.infra.feign.fallback;

import org.o2.feignclient.metadata.infra.feign.WarehouseRemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
@Component
public class WarehouseRemoteServiceImpl implements WarehouseRemoteService {
    private static final Logger logger = LoggerFactory.getLogger(WarehouseRemoteServiceImpl.class);


    @Override
    public ResponseEntity<String> saveWarehouse(Long organizationId, String warehouseCode, Map<String, Object> hashMap) {
        logger.error("Error saveWarehouse, params[tenantId = {}, warehouseCode = {},hashMap = {}]", organizationId, warehouseCode, hashMap);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> updateWarehouse(Long organizationId, String warehouseCode, Map<String, Object> hashMap) {
        logger.error("Error updateWarehouse, params[tenantId = {}, warehouseCode = {},hashMap = {}]", organizationId, warehouseCode, hashMap);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }


    @Override
    public ResponseEntity<String> getWarehouse(Long organizationId, String warehouseCode) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
