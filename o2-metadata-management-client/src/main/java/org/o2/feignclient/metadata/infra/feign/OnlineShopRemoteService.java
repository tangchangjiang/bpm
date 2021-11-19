package org.o2.feignclient.metadata.infra.feign;

import io.swagger.annotations.ApiParam;
import org.o2.feignclient.metadata.domain.dto.OnlineShopCatalogVersionDTO;
import org.o2.feignclient.metadata.domain.dto.OnlineShopQueryInnerDTO;
import org.o2.feignclient.metadata.domain.dto.OnlineShopRelWarehouseInnerDTO;
import org.o2.feignclient.metadata.infra.constants.O2Service;
import org.o2.feignclient.metadata.infra.feign.fallback.OnlineShopRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * 查询网店关联有效仓库
     *
     * @param onlineShopCode 网店编码
     * @param organizationId 租户ID
     * @return ResponseEntity<String>
     */
    @GetMapping("/{organizationId}/onlineShopRelWarehouse-internal/{onlineShopCode}")
    ResponseEntity<String> listOnlineShopRelWarehouses(@PathVariable(value = "onlineShopCode") @ApiParam(value = "网店编码", required = true) String onlineShopCode,
                                                       @PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true)  Long organizationId);

    /**
     * 查询网店关联有效仓库
     *
     * @param onlineShopRelWarehouseInnerDTO 网店编码
     * @param organizationId 租户ID
     * @return ResponseEntity<String>
     */
    @PostMapping("/{organizationId}/onlineShopRelWarehouse-internal/rel-warehouse/list")
    ResponseEntity<String> listOnlineShopRelWarehouses(@RequestBody OnlineShopRelWarehouseInnerDTO onlineShopRelWarehouseInnerDTO,
                                                       @PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId);

    /**
     * 批量查询网店
     * @param  onlineShopQueryInnerDTO 网店
     * @param  organizationId 租户ID
     * @return map
     */
    @PostMapping("/{organizationId}/online-shops-internal/onlineShop-list")
    ResponseEntity<String> listOnlineShops(@RequestBody OnlineShopQueryInnerDTO onlineShopQueryInnerDTO,
                                           @PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true)Long organizationId);

    /**
     * 批量查询网店
     * @param  onlineShopCatalogVersionList 目录版本
     * @param  organizationId 租户ID
     * @return map
     */
    @PostMapping("/{organizationId}/online-shops-internal/onlineShops")
    ResponseEntity<String> listOnlineShops(@RequestBody List<OnlineShopCatalogVersionDTO> onlineShopCatalogVersionList,
                                           @PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true)Long organizationId);

}
