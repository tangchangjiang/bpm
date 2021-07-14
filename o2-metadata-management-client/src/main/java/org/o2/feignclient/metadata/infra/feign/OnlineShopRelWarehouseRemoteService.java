package org.o2.feignclient.metadata.infra.feign;

import io.swagger.annotations.ApiParam;
import org.o2.feignclient.metadata.infra.constants.O2Service;
import org.o2.feignclient.metadata.infra.feign.fallback.OnlineShopRelWarehouseRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 *
 *
 * @author yipeng.zhu@hand-china.com 2021-07-13
 **/
@FeignClient(
        value = O2Service.metadata.NAME,
        path = "/v1",
        fallback = OnlineShopRelWarehouseRemoteServiceImpl.class
)
public interface OnlineShopRelWarehouseRemoteService {
    /**
     * 从redis查询网店关联仓库
     *
     * @param onlineShopCode 网店编码
     * @param tenantId 租户ID
     * @return ResponseEntity<String>
     */
    @GetMapping("/{organizationId}/onlineShopRelWarehouse-internal/{onlineShopCode}")
    ResponseEntity<String> listOnlineShopRelWarehouses(@PathVariable @ApiParam(value = "网店编码", required = true) String onlineShopCode,
                                                       @PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true)Long tenantId);

}
