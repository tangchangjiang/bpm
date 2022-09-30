package org.o2.metadata.management.client.infra.feign;

import io.swagger.annotations.ApiParam;
import org.o2.core.common.O2Service;
import org.o2.metadata.management.client.domain.dto.WarehousePageQueryInnerDTO;
import org.o2.metadata.management.client.domain.dto.WarehouseQueryInnerDTO;
import org.o2.metadata.management.client.infra.feign.fallback.WarehouseRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 仓库
 *
 * @author yipeng.zhu@hand-china.com 2021-07-13
 **/
@FeignClient(
        value = O2Service.MetadataManagement.NAME,
        path = "/v1",
        fallback = WarehouseRemoteServiceImpl.class
)
public interface WarehouseRemoteService {
    /**
     * 从redis查询系统仓库
     *
     * @param innerDTO       入参
     * @param organizationId 租户ID
     * @return ResponseEntity<String>
     */
    @PostMapping("/{organizationId}/warehouse-internal/list")
    ResponseEntity<String> listWarehouses(@RequestBody WarehouseQueryInnerDTO innerDTO,
                                          @PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId);

    /**
     * 保存仓库快递配送接单量限制
     *
     * @param organizationId
     * @param warehouseCode
     * @param expressQuantity
     * @return ResponseEntity
     */
    @PostMapping({"/{organizationId}/warehouse-internal/saveExpressQuantity"})
    ResponseEntity<String> saveExpressQuantity(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                               @RequestParam(value = "warehouseCode") String warehouseCode,
                                               @RequestParam(value = "expressQuantity") String expressQuantity);

    /**
     * 保存仓库自提接单量限制
     *
     * @param organizationId
     * @param warehouseCode
     * @param pickUpQuantity
     * @return ResponseEntity
     */
    @PostMapping({"/{organizationId}/warehouse-internal/savePickUpQuantity"})
    ResponseEntity<String> savePickUpQuantity(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                              @RequestParam(value = "warehouseCode") String warehouseCode,
                                              @RequestParam(value = "pickUpQuantity") String pickUpQuantity);

    /**
     * 仓库快递配送接单量增量更新
     *
     * @param organizationId
     * @param warehouseCode
     * @param increment
     * @return ResponseEntity
     */
    @PostMapping({"/{organizationId}/warehouse-internal/updateExpressValue"})
    ResponseEntity<String> updateExpressValue(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                              @RequestParam(value = "warehouseCode") String warehouseCode,
                                              @RequestParam(value = "increment") String increment);

    /**
     * 仓库自提接单量增量更新
     *
     * @param organizationId
     * @param warehouseCode
     * @param increment
     * @return ResponseEntity
     */
    @PostMapping({"/{organizationId}/warehouse-internal/updatePickUpValue"})
    ResponseEntity<String> updatePickUpValue(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                             @RequestParam(value = "warehouseCode") String warehouseCode,
                                             @RequestParam(value = "increment") String increment);

    /**
     * 获取快递配送接单量限制
     *
     * @param organizationId
     * @param warehouseCode
     * @return ResponseEntity
     */
    @GetMapping({"/{organizationId}/warehouse-internal/getExpressLimit"})
    ResponseEntity<String> getExpressLimit(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                           @RequestParam(value = "warehouseCode") String warehouseCode);

    /**
     * 获取自提接单量限制
     *
     * @param organizationId
     * @param warehouseCode
     * @return ResponseEntity
     */
    @GetMapping({"/{organizationId}/warehouse-internal/getPickUpLimit"})
    ResponseEntity<String> getPickUpLimit(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                          @RequestParam(value = "warehouseCode") String warehouseCode);

    /**
     * 获取实际快递配送接单量
     *
     * @param organizationId
     * @param warehouseCode
     * @return ResponseEntity
     */
    @GetMapping({"/{organizationId}/warehouse-internal/getExpressValue"})
    ResponseEntity<String> getExpressValue(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                           @RequestParam(value = "warehouseCode") String warehouseCode);

    /**
     * 获取实际自提接单量
     *
     * @param organizationId
     * @param warehouseCode
     * @return ResponseEntity
     */
    @GetMapping({"/{organizationId}/warehouse-internal/getPickUpValue"})
    ResponseEntity<String> getPickUpValue(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                          @RequestParam(value = "warehouseCode") String warehouseCode);

    /**
     * 获取仓库缓存KEY
     *
     * @param organizationId
     * @param warehouseCode
     * @return ResponseEntity
     */
    @GetMapping({"/{organizationId}/warehouse-internal/warehouseCacheKey"})
    ResponseEntity<String> warehouseCacheKey(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                             @RequestParam(value = "warehouseCode") String warehouseCode);


    /**
     * 获取仓库limit缓存KEY
     *
     * @param organizationId
     * @param limit
     * @return ResponseEntity
     */
    @GetMapping({"/{organizationId}/warehouse-internal/warehouseLimitCacheKey"})
    ResponseEntity<String> warehouseLimitCacheKey(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                  @RequestParam(value = "limit") String limit);

    /**
     * 是否仓库快递配送接单量到达上限
     *
     * @param organizationId
     * @param warehouseCode
     * @return ResponseEntity
     */
    @GetMapping({"/{organizationId}/warehouse-internal/isWarehouseExpressLimit"})
    ResponseEntity<String> isWarehouseExpressLimit(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                   @RequestParam(value = "warehouseCode") String warehouseCode);

    /**
     * 是否仓库自提接单量到达上限
     *
     * @param organizationId
     * @param warehouseCode
     * @return ResponseEntity
     */
    @GetMapping({"/{organizationId}/warehouse-internal/isWarehousePickUpLimit"})
    ResponseEntity<String> isWarehousePickUpLimit(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                  @RequestParam(value = "warehouseCode") String warehouseCode);

    /**
     * 获取快递配送接单量到达上限的仓库
     *
     * @param organizationId
     * @return ResponseEntity
     */
    @GetMapping({"/{organizationId}/warehouse-internal/expressLimitWarehouseCollection"})
    ResponseEntity<String> expressLimitWarehouseCollection(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId);

    /**
     * 获取自提接单量到达上限的仓库
     *
     * @param organizationId
     * @return ResponseEntity
     */
    @GetMapping({"/{organizationId}/warehouse-internal/pickUpLimitWarehouseCollection"})
    ResponseEntity<String> pickUpLimitWarehouseCollection(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId);


    /**
     * 重置仓库快递配送接单量值
     *
     * @param organizationId
     * @return ResponseEntity
     */
    @PostMapping({"/{organizationId}/warehouse-internal/allDeliveryWarehouse"})
    ResponseEntity<String> listAllDeliveryWarehouse(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId);

    /**
     * 重置仓库自提接单量限制值
     *
     * @param organizationId
     * @param warehouseCode
     * @return ResponseEntity
     */
    @PostMapping({"/{organizationId}/warehouse-internal/resetWarehousePickUpLimit"})
    ResponseEntity<String> resetWarehousePickUpLimit(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                     @RequestParam(value = "warehouseCode", required = true) String warehouseCode);

    /**
     * 查询有效仓库
     *
     * @param onlineShopCode 网店编码
     * @param organizationId 租户ID
     * @return ResponseEntity<String>
     */
    @GetMapping("/{organizationId}/warehouse-internal/active/{onlineShopCode}")
    ResponseEntity<String> listActiveWarehouse(@PathVariable @ApiParam(value = "网店编码", required = true) String onlineShopCode,
                                               @PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId);

    /**
     * 分页查询仓库
     *
     * @param innerDTO       入参
     * @param organizationId 租户ID
     * @return ResponseEntity<String>
     */
    @PostMapping("/{organizationId}/warehouse-internal/page")
    ResponseEntity<String> pageWarehouses(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                          @RequestBody WarehousePageQueryInnerDTO innerDTO);

    /**
     * 通过服务点查询仓库
     * @param posCodes 服务点编码
     * @param organizationId 租户ID
     * @return 仓库
     */
    @PostMapping("/{organizationId}/warehouse-internal/list-warehouse")
    ResponseEntity<String> listWarehousesByPosCode(@RequestParam(value = "posCodes", required = true) List<String> posCodes, @PathVariable @ApiParam(value = "租户ID", required = true) final  Long organizationId);
}
