package org.o2.metadata.client.infra.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.o2.metadata.client.infra.feign.OnlineShopRemoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 *
 * 运费
 *
 * @author yipeng.zhu@hand-china.com 2021-07-14
 **/
@Component
@Slf4j
public class OnlineShopRemoteServiceImpl implements OnlineShopRemoteService {


    @Override
    public ResponseEntity<String> getOnlineShop(String onlineShopCode) {
        log.error("Error getOnlineShop, params[onlineShopCode = {}]", onlineShopCode);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
