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
    public ResponseEntity<String> saveExpressQuantity(Long organizationId, String warehouseCode, String expressQuantity) {
        logger.error("Error saveExpressQuantity, params[tenantId = {}, warehouseCode = {},expressQuantity = {}]", organizationId, warehouseCode, expressQuantity);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> savePickUpQuantity(Long organizationId, String warehouseCode, String pickUpQuantity) {
        logger.error("Error savePickUpQuantity, params[tenantId = {}, warehouseCode = {},pickUpQuantity = {}]", organizationId, warehouseCode, pickUpQuantity);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> updateExpressValue(Long organizationId, String warehouseCode, String increment) {
        logger.error("Error updateExpressValue, params[tenantId = {}, warehouseCode = {},increment = {}]", organizationId, warehouseCode, increment);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> updatePickUpValue(Long organizationId, String warehouseCode, String increment) {
        logger.error("Error updatePickUpValue, params[tenantId = {}, warehouseCode = {},increment = {}]", organizationId, warehouseCode, increment);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> getExpressLimit(Long organizationId, String warehouseCode) {
        logger.error("Error getExpressLimit, params[tenantId = {}, warehouseCode = {}]", new Object[]{organizationId, warehouseCode});
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> getPickUpLimit(Long organizationId, String warehouseCode) {
        logger.error("Error getPickUpLimit, params[tenantId = {}, warehouseCode = {}]", new Object[]{organizationId, warehouseCode});
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> getExpressValue(Long organizationId, String warehouseCode) {
        logger.error("Error getExpressValue, params[tenantId = {}, warehouseCode = {}]", new Object[]{organizationId, warehouseCode});
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> getPickUpValue(Long organizationId, String warehouseCode) {
        logger.error("Error getPickUpValue, params[tenantId = {}, warehouseCode = {}]", new Object[]{organizationId, warehouseCode});
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> warehouseCacheKey(Long organizationId, String warehouseCode) {
        logger.error("Error warehouseCacheKey, params[tenantId = {}, warehouseCode = {}]", new Object[]{organizationId, warehouseCode});
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> warehouseLimitCacheKey(Long organizationId, String limit) {
        logger.error("Error warehouseLimitCacheKey, params[tenantId = {}, limit = {}]", new Object[]{organizationId, limit});
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> isWarehouseExpressLimit(Long organizationId, String warehouseCode) {
        logger.error("Error isWarehouseExpressLimit, params[tenantId = {}, warehouseCode = {}]", new Object[]{organizationId, warehouseCode});
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> isWarehousePickUpLimit(Long organizationId, String warehouseCode) {
        logger.error("Error isWarehousePickUpLimit, params[tenantId = {}, warehouseCode = {}]", new Object[]{organizationId, warehouseCode});
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> expressLimitWarehouseCollection(Long organizationId) {
        logger.error("Error expressLimitWarehouseCollection, params[tenantId = {}]", new Object[]{organizationId});
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> pickUpLimitWarehouseCollection(Long organizationId) {
        logger.error("Error pickUpLimitWarehouseCollection, params[tenantId = {}]", new Object[]{organizationId});
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> resetWarehouseExpressLimit(Long organizationId, String warehouseCode) {
        logger.error("Error resetWarehouseExpressLimit, params[tenantId = {}, warehouseCode = {}]", new Object[]{organizationId, warehouseCode});
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> resetWarehousePickUpLimit(Long organizationId, String warehouseCode) {
        logger.error("Error resetWarehousePickUpLimit, params[tenantId = {}, warehouseCode = {}]", new Object[]{organizationId, warehouseCode});
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> getWarehouse(Long organizationId, String warehouseCode) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
