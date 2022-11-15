package org.o2.metadata.client.infra.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.o2.core.helper.JsonHelper;
import org.o2.metadata.client.infra.feign.OnlineShopRemoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 运费
 *
 * @author yipeng.zhu@hand-china.com 2021-07-14
 **/
@Component
@Slf4j
public class OnlineShopRemoteServiceImpl implements OnlineShopRemoteService {

    @Override
    public ResponseEntity<String> getOnlineShop(String onlineShopCode, String tenantId) {
        log.error("Error getOnlineShop, params[onlineShopCode = {},tenantId = {}]", onlineShopCode, tenantId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> queryOnlineShop(List<String> onlineShopCodes) {
        log.error("Error getOnlineShop, params[onlineShopCodes = {}]", JsonHelper.objectToString(onlineShopCodes));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> queryOnlineShopByType(String tenantId, String onlineShopType) {
        log.error("Error queryOnlineShopByType, params[tenantId= {}, onlineShopType = {}]", tenantId, onlineShopType);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> batchQueryOnlineShop(String tenantId, List<String> onlineShopCodes) {
        log.error("Error batchQueryOnlineShop, params[tenantId= {}, onlineShopCodes = {}]", tenantId, JsonHelper.objectToString(onlineShopCodes));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
