package org.o2.metadata.management.client.infra.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.o2.metadata.management.client.domain.dto.OnlineShopCatalogVersionDTO;
import org.o2.metadata.management.client.domain.dto.OnlineShopDTO;
import org.o2.metadata.management.client.domain.dto.OnlineShopQueryInnerDTO;
import org.o2.metadata.management.client.domain.dto.OnlineShopRelWarehouseInnerDTO;
import org.o2.metadata.management.client.infra.feign.OnlineShopRemoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * description
 *
 * @author zhilin.ren@hand-china.com 2021/08/05 16:48
 */
@Service("onlineShopRemoteManagementService")
@Slf4j
public class OnlineShopRemoteServiceImpl implements OnlineShopRemoteService {

    @Override
    public ResponseEntity<String> listOnlineShopRelWarehouses(String onlineShopCode, Long tenantId) {
        log.error("Error listOnlineShopRelWarehouses, params[tenantId = {}, onlineShopCode = {}]", tenantId, onlineShopCode);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> listOnlineShopRelWarehouses(OnlineShopRelWarehouseInnerDTO onlineShopRelWarehouseInnerDTO, Long organizationId) {
        return null;
    }

    @Override
    public ResponseEntity<String> listOnlineShops(OnlineShopQueryInnerDTO onlineShopQueryInnerDTO, Long organizationId) {
        log.error("Error listOnlineShops, params[tenantId = {}, platformCode = {}]", organizationId, onlineShopQueryInnerDTO);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> listOnlineShopsOfSite(OnlineShopQueryInnerDTO onlineShopQueryInnerDTO) {
        log.error("Error listOnlineShopsOfSite, params = {}", onlineShopQueryInnerDTO);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> listOnlineShops(List<OnlineShopCatalogVersionDTO> onlineShopCatalogVersionList, Long organizationId) {
        log.error("Error listOnlineShops, params[tenantId = {}, onlineShopCatalogVersionList = {}]", organizationId, onlineShopCatalogVersionList);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> saveOnlineShop(OnlineShopDTO onlineShopDTO, Long organizationId) {
        log.error("Error onlineShop, params[tenantId = {}, onlineShopDTO = {}]", organizationId, onlineShopDTO);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> batchUpdateShopStatus(List<OnlineShopDTO> onlineShopDTOList, Long organizationId) {
        log.error("Error onlineShop, params[tenantId = {}, onlineShopDTO = {}]", organizationId, onlineShopDTOList);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> queryOnlineShops(Long tenantId, OnlineShopQueryInnerDTO onlineShopQueryInnerDTO) {
        log.error("Error onlineShop, params[tenantId = {}, onlineShopQueryInnerDTO = {}]", tenantId, onlineShopQueryInnerDTO);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
