package org.o2.feignclient.metadata.infra.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.o2.feignclient.metadata.domain.dto.OnlineShopCatalogVersionDTO;
import org.o2.feignclient.metadata.domain.dto.OnlineShopDTO;
import org.o2.feignclient.metadata.infra.feign.OnlineShopRemoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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
        log.error("Error getOnlineShopCode, params[tenantId = {}, platformCode = {},shopName= {}]",organizationId, platformCode,shopName);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> listOnlineShopRelWarehouses(String onlineShopCode, Long tenantId) {
        log.error("Error listOnlineShopRelWarehouses, params[tenantId = {}, onlineShopCode = {}]",tenantId, onlineShopCode);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> listOnlineShops(OnlineShopDTO onlineShopDTO, Long organizationId) {
        log.error("Error listOnlineShops, params[tenantId = {}, platformCode = {}]",organizationId, onlineShopDTO);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> listOnlineShops(List<OnlineShopCatalogVersionDTO> onlineShopCatalogVersionList, Long organizationId) {
        log.error("Error listOnlineShops, params[tenantId = {}, onlineShopCatalogVersionList = {}]",organizationId, onlineShopCatalogVersionList);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
