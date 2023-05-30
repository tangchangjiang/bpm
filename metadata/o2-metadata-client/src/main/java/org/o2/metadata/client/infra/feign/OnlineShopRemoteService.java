package org.o2.metadata.client.infra.feign;

import org.o2.core.common.O2Service;
import org.o2.metadata.client.infra.feign.fallback.OnlineShopRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 门店
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
     * 查询网店
     *
     * @param onlineShopCode 网店编码
     * @param tenantId       租户id
     * @return 网店
     */
    @GetMapping("/onlineShop-internal/online-shop")
    ResponseEntity<String> getOnlineShop(@RequestParam String onlineShopCode, @RequestParam String tenantId);

    /**
     * 查询网店
     *
     * @param onlineShopCode 网店编码
     * @return 网店
     */
    @GetMapping("/onlineShop-internal/online-shop-by-code")
    ResponseEntity<String> getOnlineShopByCode(@RequestParam String onlineShopCode);

    /**
     * 批量获取门店
     *
     * @param onlineShopCodes 门店codes
     * @return ResponseEntity<String>
     */
    @PostMapping("/onlineShop-internal/online-shop/list")
    ResponseEntity<String> queryOnlineShop(@RequestBody List<String> onlineShopCodes);

    /**
     * 批量查询网店-根据网店类型
     * @param onlineShopType 网店类型
     * @return ResponseEntity<String>
     */
    @GetMapping("/onlineShop-internal/online-shop/list-by-type")
    ResponseEntity<String> queryOnlineShopByType(@RequestParam String tenantId, @RequestParam String onlineShopType);

    /**
     * 批量获取网店
     * @param tenantId
     * @param onlineShopCodes
     * @return
     */
    @GetMapping("/onlineShop-internal/online-shop/batch-list")
    ResponseEntity<String> batchQueryOnlineShop(@RequestParam String tenantId, @RequestParam List<String> onlineShopCodes);

    /**
     * 多租户查询网店信息
     *
     * @param onlineShopTenantMap 租户关联网店
     * @return 网店信息
     */
    @Deprecated
    @PostMapping("/onlineShop-internal/online-shop/batch-tenant")
    ResponseEntity<String> queryOnlineShopBatchTenant(@RequestBody Map<Long, List<String>> onlineShopTenantMap);
}
