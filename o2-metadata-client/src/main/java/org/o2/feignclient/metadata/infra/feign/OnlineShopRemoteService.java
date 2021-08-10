package org.o2.feignclient.metadata.infra.feign;

import org.o2.feignclient.metadata.infra.constants.O2Service;
import org.o2.feignclient.metadata.infra.feign.fallback.OnlineShopRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * 运费
 *
 * @author yipeng.zhu@hand-china.com 2021-07-14
 **/
@FeignClient(
        value = O2Service.Metadata.NAME,
        path = "/v1",
        fallback = OnlineShopRemoteServiceImpl.class
)
public interface OnlineShopRemoteService {
    /**
     * 获取运费
     *
     * @param onlineShopCode 网店编码
     * @return 网店
     */
    @GetMapping("/onlineShop-internal/online-shop")
    ResponseEntity<String> getOnlineShop(@RequestParam String  onlineShopCode);
}
