package org.o2.feignclient.metadata.infra.feign;

import io.swagger.annotations.ApiParam;
import org.o2.feignclient.metadata.infra.constants.O2Service;
import org.o2.feignclient.metadata.infra.feign.fallback.OnlineShopRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * description 网店内部接口
 *
 * @author zhilin.ren@hand-china.com 2021/08/05 16:39
 */
@FeignClient(
        value = O2Service.Metadata.NAME,
        path = "/v1",
        fallback = OnlineShopRemoteServiceImpl.class
)
public interface OnlineShopRemoteService {

    /**
     * 内部调用获取商品的code
     * @param organizationId 租户id
     * @param platformCode 平台code
     * @param shopName 网店名称
     * @return String 结果
     */
    @GetMapping("/{organizationId}/online-shops-internal")
    ResponseEntity<String> getOnlineShopCode(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                     @RequestParam("platformCode") String platformCode,
                                     @RequestParam("shopName") String shopName);

}
