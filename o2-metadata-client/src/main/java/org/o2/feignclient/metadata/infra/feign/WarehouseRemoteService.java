package org.o2.feignclient.metadata.infra.feign;

import io.swagger.annotations.ApiParam;
import org.o2.feignclient.metadata.infra.constants.O2Service;
import org.o2.feignclient.metadata.infra.feign.fallback.WarehouseRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
@FeignClient(
        value = O2Service.metadata.NAME,
        path = "/v1",
        fallback = WarehouseRemoteServiceImpl.class
)
public interface WarehouseRemoteService {


    /**
     * 保存
     *
     * @param organizationId
     * @param warehouseCode
     * @param hashMap
     * @return
     */
    @PostMapping({"/{organizationId}/internal/saveWarehouse"})
    ResponseEntity<String> saveWarehouse(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                         @RequestParam(value = "warehouseCode", required = true) String warehouseCode,
                                         @RequestParam(value = "hashMap", required = false) Map<String, Object> hashMap);

    /**
     * 更新
     * @param organizationId
     * @param warehouseCode
     * @param hashMap
     * @return ResponseEntity
     */
    @PostMapping({"/{organizationId}/internal/updateWarehouse"})
    ResponseEntity<String> updateWarehouse(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                           @RequestParam(value = "warehouseCode", required = true) String warehouseCode,
                                           @RequestParam(value = "hashMap", required = false) Map<String, Object> hashMap);

    /**
     * 保存仓库快递配送接单量限制
     * @param organizationId
     * @param warehouseCode
     * @param expressQuantity
     * @return ResponseEntity
     */
    @PostMapping({"/{organizationId}/internal/saveExpressQuantity"})
    ResponseEntity<String> saveExpressQuantity(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                               @RequestParam(value = "warehouseCode") String warehouseCode,
                                               @RequestParam(value = "expressQuantity") String expressQuantity);

    /**
     * 保存仓库自提接单量限制
     * @param organizationId
     * @param warehouseCode
     * @param pickUpQuantity
     * @return ResponseEntity
     */
    @PostMapping({"/{organizationId}/internal/savePickUpQuantity"})
    ResponseEntity<String> savePickUpQuantity(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                              @RequestParam(value = "warehouseCode") String warehouseCode,
                                              @RequestParam(value = "pickUpQuantity") String pickUpQuantity);

    /**
     * 仓库快递配送接单量增量更新
     * @param organizationId
     * @param warehouseCode
     * @param increment
     * @return ResponseEntity
     */
    @PostMapping({"/{organizationId}/internal/updateExpressValue"})
    ResponseEntity<String> updateExpressValue(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                              @RequestParam(value = "warehouseCode") String warehouseCode,
                                              @RequestParam(value = "increment") String increment);

    /**
     * 仓库自提接单量增量更新
     * @param organizationId
     * @param warehouseCode
     * @param increment
     * @return ResponseEntity
     */
    @PostMapping({"/{organizationId}/internal/updatePickUpValue"})
    ResponseEntity<String> updatePickUpValue(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                             @RequestParam(value = "warehouseCode") String warehouseCode,
                                             @RequestParam(value = "increment") String increment);

    /**
     * 获取快递配送接单量限制
     * @param organizationId
     * @param warehouseCode
     * @return ResponseEntity
     */
    @GetMapping({"/{organizationId}/internal/getExpressLimit"})
    ResponseEntity<String> getExpressLimit(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                           @RequestParam(value = "warehouseCode") String warehouseCode);

    /**
     * 获取自提接单量限制
     * @param organizationId
     * @param warehouseCode
     * @return ResponseEntity
     */
    @GetMapping({"/{organizationId}/internal/getPickUpLimit"})
    ResponseEntity<String> getPickUpLimit(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                          @RequestParam(value = "warehouseCode") String warehouseCode);

    /**
     * 获取实际快递配送接单量
     * @param organizationId
     * @param warehouseCode
     * @return ResponseEntity
     */
    @GetMapping({"/{organizationId}/internal/getExpressValue"})
    ResponseEntity<String> getExpressValue(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                           @RequestParam(value = "warehouseCode") String warehouseCode);

    /**
     * 获取实际自提接单量
     * @param organizationId
     * @param warehouseCode
     * @return ResponseEntity
     */
    @GetMapping({"/{organizationId}/internal/getPickUpValue"})
    ResponseEntity<String> getPickUpValue(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                          @RequestParam(value = "warehouseCode") String warehouseCode);

    /**
     * 获取仓库缓存KEY
     * @param organizationId
     * @param warehouseCode
     * @return ResponseEntity
     */
    @GetMapping({"/{organizationId}/internal/warehouseCacheKey"})
    ResponseEntity<String> warehouseCacheKey(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                             @RequestParam(value = "warehouseCode") String warehouseCode);


    /**
     * 获取仓库limit缓存KEY
     * @param organizationId
     * @param limit
     * @return ResponseEntity
     */
    @GetMapping({"/{organizationId}/internal/warehouseLimitCacheKey"})
    ResponseEntity<String> warehouseLimitCacheKey(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                  @RequestParam(value = "limit") String limit);

    /**
     * 是否仓库快递配送接单量到达上限
     * @param organizationId
     * @param warehouseCode
     * @return ResponseEntity
     */
    @GetMapping({"/{organizationId}/internal/isWarehouseExpressLimit"})
    ResponseEntity<String> isWarehouseExpressLimit(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                   @RequestParam(value = "warehouseCode") String warehouseCode);

    /**
     * 是否仓库自提接单量到达上限
     * @param organizationId
     * @param warehouseCode
     * @return ResponseEntity
     */
    @GetMapping({"/{organizationId}/internal/isWarehousePickUpLimit"})
    ResponseEntity<String> isWarehousePickUpLimit(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                  @RequestParam(value = "warehouseCode") String warehouseCode);

    /**
     * 获取快递配送接单量到达上限的仓库
     * @param organizationId
     * @return ResponseEntity
     */
    @GetMapping({"/{organizationId}/internal/expressLimitWarehouseCollection"})
    ResponseEntity<String> expressLimitWarehouseCollection(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId);

    /**
     * 获取自提接单量到达上限的仓库
     * @param organizationId
     * @return ResponseEntity
     */
    @GetMapping({"/{organizationId}/internal/pickUpLimitWarehouseCollection"})
    ResponseEntity<String> pickUpLimitWarehouseCollection(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId);


    /**
     * 重置仓库快递配送接单量值
     * @param organizationId
     * @param warehouseCode
     * @return ResponseEntity
     */
    @PostMapping({"/{organizationId}/internal/resetWarehouseExpressLimit"})
    ResponseEntity<String> resetWarehouseExpressLimit(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                      @RequestParam(value = "warehouseCode", required = true) String warehouseCode);

    /**
     * 重置仓库自提接单量限制值
     * @param organizationId
     * @param warehouseCode
     * @return ResponseEntity
     */
    @PostMapping({"/{organizationId}/internal/resetWarehousePickUpLimit"})
    ResponseEntity<String> resetWarehousePickUpLimit(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                     @RequestParam(value = "warehouseCode", required = true) String warehouseCode);


}
