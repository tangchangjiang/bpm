package org.o2.metadata.client.infra.feign.fallback;

import org.o2.metadata.client.infra.feign.WarehouseRemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
@Component("warehouseRemoteService")
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
    public ResponseEntity<String> listWarehouses(Long organizationId, List<String> warehouseCodes) {
        logger.error("Error listWarehouses, params[tenantId = {}, warehouseCodes = {}]", organizationId, warehouseCodes);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> updateExpressValue(Long organizationId, String warehouseCode, String increment) {
        logger.error("Error updateExpressValue, params[tenantId = {}, warehouseCodes = {}, increment = {}]", organizationId, warehouseCode, increment);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> listWarehousePickupLimit(Long organizationId, List<String> warehouseCodes) {
        logger.error("Error listWarehousePickupLimit, params[tenantId = {}, warehouseCodes = {}]", organizationId, warehouseCodes);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> listWarehousesByPosCode(Long organizationId, List<String> posCodes) {
        logger.error("Error listWarehousesByPosCode, params[tenantId = {}, posCodes = {}]", organizationId, posCodes);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
