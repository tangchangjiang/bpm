package org.o2.feignclient.metadata.infra.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.o2.feignclient.metadata.domain.dto.WarehousePageQueryInnerDTO;
import org.o2.feignclient.metadata.domain.dto.WarehouseQueryInnerDTO;
import org.o2.feignclient.metadata.infra.feign.WarehouseRemoteService;
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

    @Override
    public ResponseEntity<String> listWarehouses(WarehouseQueryInnerDTO innerDTO, Long organizationId) {
        log.error("Error saveExpressQuantity, params[tenantId = {}, innerDTO = {}]", organizationId, innerDTO);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> saveExpressQuantity(Long organizationId, String warehouseCode, String expressQuantity) {
        log.error("Error saveExpressQuantity, params[tenantId = {}, warehouseCode = {},expressQuantity = {}]", organizationId, warehouseCode, expressQuantity);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> savePickUpQuantity(Long organizationId, String warehouseCode, String pickUpQuantity) {
        log.error("Error savePickUpQuantity, params[tenantId = {}, warehouseCode = {},pickUpQuantity = {}]", organizationId, warehouseCode, pickUpQuantity);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> updateExpressValue(Long organizationId, String warehouseCode, String increment) {
        log.error("Error updateExpressValue, params[tenantId = {}, warehouseCode = {},increment = {}]", organizationId, warehouseCode, increment);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> updatePickUpValue(Long organizationId, String warehouseCode, String increment) {
        log.error("Error updatePickUpValue, params[tenantId = {}, warehouseCode = {},increment = {}]", organizationId, warehouseCode, increment);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> getExpressLimit(Long organizationId, String warehouseCode) {
        log.error("Error getExpressLimit, params[tenantId = {}, warehouseCode = {}]", new Object[]{organizationId, warehouseCode});
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> getPickUpLimit(Long organizationId, String warehouseCode) {
        log.error("Error getPickUpLimit, params[tenantId = {}, warehouseCode = {}]", new Object[]{organizationId, warehouseCode});
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> getExpressValue(Long organizationId, String warehouseCode) {
        log.error("Error getExpressValue, params[tenantId = {}, warehouseCode = {}]", new Object[]{organizationId, warehouseCode});
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> getPickUpValue(Long organizationId, String warehouseCode) {
        log.error("Error getPickUpValue, params[tenantId = {}, warehouseCode = {}]", new Object[]{organizationId, warehouseCode});
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> warehouseCacheKey(Long organizationId, String warehouseCode) {
        log.error("Error warehouseCacheKey, params[tenantId = {}, warehouseCode = {}]", new Object[]{organizationId, warehouseCode});
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> warehouseLimitCacheKey(Long organizationId, String limit) {
        log.error("Error warehouseLimitCacheKey, params[tenantId = {}, limit = {}]", new Object[]{organizationId, limit});
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> isWarehouseExpressLimit(Long organizationId, String warehouseCode) {
        log.error("Error isWarehouseExpressLimit, params[tenantId = {}, warehouseCode = {}]", new Object[]{organizationId, warehouseCode});
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> isWarehousePickUpLimit(Long organizationId, String warehouseCode) {
        log.error("Error isWarehousePickUpLimit, params[tenantId = {}, warehouseCode = {}]", new Object[]{organizationId, warehouseCode});
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> expressLimitWarehouseCollection(Long organizationId) {
        log.error("Error expressLimitWarehouseCollection, params[tenantId = {}]", new Object[]{organizationId});
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> pickUpLimitWarehouseCollection(Long organizationId) {
        log.error("Error pickUpLimitWarehouseCollection, params[tenantId = {}]", new Object[]{organizationId});
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> resetWarehouseExpressLimit(Long organizationId, String warehouseCode) {
        log.error("Error resetWarehouseExpressLimit, params[tenantId = {}, warehouseCode = {}]", new Object[]{organizationId, warehouseCode});
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> resetWarehousePickUpLimit(Long organizationId, String warehouseCode) {
        log.error("Error resetWarehousePickUpLimit, params[tenantId = {}, warehouseCode = {}]", new Object[]{organizationId, warehouseCode});
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> listActiveWarehouse(String onlineShopCode, Long organizationId) {
        log.error("Error listActiveWarehouse, params[tenantId = {}, onlineShopCode = {}]", organizationId, onlineShopCode);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> pageWarehouses(Long organizationId, WarehousePageQueryInnerDTO innerDTO) {
        log.error("Error pageWarehouses, params[tenantId = {}, WarehousePageQueryInnerDTO = {}]", organizationId, innerDTO);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
