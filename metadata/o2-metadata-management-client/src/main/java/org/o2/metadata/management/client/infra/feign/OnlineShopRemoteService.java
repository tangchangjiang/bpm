package org.o2.metadata.management.client.infra.feign;

import io.swagger.annotations.ApiParam;
import org.o2.core.common.O2Service;
import org.o2.metadata.management.client.domain.co.MerchantInfoCO;
import org.o2.metadata.management.client.domain.dto.OnlineShopCatalogVersionDTO;
import org.o2.metadata.management.client.domain.dto.OnlineShopDTO;
import org.o2.metadata.management.client.domain.dto.OnlineShopQueryInnerDTO;
import org.o2.metadata.management.client.domain.dto.OnlineShopRelWarehouseInnerDTO;
import org.o2.metadata.management.client.infra.feign.fallback.OnlineShopRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * description 网店内部接口
 *
 * @author zhilin.ren@hand-china.com 2021/08/05 16:39
 */
@FeignClient(
        value = O2Service.MetadataManagement.NAME,
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
                                                       @PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId);

    /**
     * 查询网店关联有效仓库
     *
     * @param onlineShopRelWarehouseInnerDTO 网店编码
     * @param organizationId                 租户ID
     * @return ResponseEntity<String>
     */
    @PostMapping("/{organizationId}/onlineShopRelWarehouse-internal/rel-warehouse/list")
    ResponseEntity<String> listOnlineShopRelWarehouses(@RequestBody OnlineShopRelWarehouseInnerDTO onlineShopRelWarehouseInnerDTO,
                                                       @PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId);

    /**
     * 批量查询网店
     *
     * @param onlineShopQueryInnerDTO 网店
     * @param organizationId          租户ID
     * @return map
     */
    @PostMapping("/{organizationId}/online-shops-internal/onlineShop-list")
    ResponseEntity<String> listOnlineShops(@RequestBody OnlineShopQueryInnerDTO onlineShopQueryInnerDTO,
                                           @PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId);

    /**
     * 批量查询网店（平台层查询）
     *
     * @param onlineShopQueryInnerDTO 网店
     * @return map
     */
    @PostMapping("/online-shops-internal/onlineShop-list-site")
    ResponseEntity<String> listOnlineShopsOfSite(@RequestBody OnlineShopQueryInnerDTO onlineShopQueryInnerDTO);

    /**
     * 批量查询网店
     *
     * @param onlineShopCatalogVersionList 目录版本
     * @param organizationId               租户ID
     * @return map
     */
    @PostMapping("/{organizationId}/online-shops-internal/onlineShops")
    ResponseEntity<String> listOnlineShops(@RequestBody List<OnlineShopCatalogVersionDTO> onlineShopCatalogVersionList,
                                           @PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId);

    /**
     * 新建或修改网店
     *
     * @param onlineShopDTO  网店
     * @param organizationId 租户id
     * @return 网店
     */
    @PostMapping("/{organizationId}/online-shops-internal/onlineShop-save")
    ResponseEntity<String> saveOnlineShop(@RequestBody OnlineShopDTO onlineShopDTO,
                                          @PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId);

    /**
     * 批量更新网店状态
     *
     * @param onlineShopDTOList 网店信息
     * @param organizationId    租户id
     * @return 网店
     */
    @PostMapping("/{organizationId}/online-shops-internal/batch-update-status")
    ResponseEntity<String> batchUpdateShopStatus(@RequestBody List<OnlineShopDTO> onlineShopDTOList,
                                                 @PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId);

    /**
     * 分页查询排除网店
     *
     * @param onlineShopQueryInnerDTO 查询条件
     * @param organizationId                租户id
     * @return 网店列表
     */
    @PostMapping("/{organizationId}/online-shops-internal/query-exclude-onlineShops")
    ResponseEntity<String> queryOnlineShops(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                            @RequestBody OnlineShopQueryInnerDTO onlineShopQueryInnerDTO);

    /**
     * 同步商家信息
     *
     * @param organizationId 租户Id
     * @param merchantInfoCO 商家信息
     * @return 操作结果
     */
    @PostMapping("/{organizationId}/online-shops-internal/sync-merchant")
    ResponseEntity<String> syncMerchantInfo(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                            @RequestBody MerchantInfoCO merchantInfoCO);
}
