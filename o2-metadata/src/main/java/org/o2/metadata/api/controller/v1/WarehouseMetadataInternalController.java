package org.o2.metadata.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.util.Results;
import org.o2.metadata.api.vo.WarehouseVO;
import org.o2.metadata.app.service.WarehouseService;
import org.o2.metadata.config.EnableMetadata;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
@Api(tags = {EnableMetadata.SYS_WAREHOUSE_INTERNAL})
@RestController("warehouseMetadataInternalController.v1")
@RequestMapping("v1/{organizationId}")
public class WarehouseMetadataInternalController {
    private WarehouseService warehouseService;

    public WarehouseMetadataInternalController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @ApiOperation("保存(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @PostMapping({"/internal/saveWarehouse"})
    public ResponseEntity<?> saveWarehouse(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                           @RequestParam(value = "warehouseCode", required = true) String warehouseCode,
                                           @RequestParam(value = "hashMap", required = false) Map<String, Object> hashMap) {
        warehouseService.saveWarehouse(warehouseCode, hashMap, organizationId);
        return Results.success();
    }

    @ApiOperation("更新(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @PostMapping({"/internal/updateWarehouse"})
    public ResponseEntity<?> updateWarehouse(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                             @RequestParam(value = "warehouseCode", required = true) String warehouseCode,
                                             @RequestParam(value = "hashMap", required = false) Map<String, Object> hashMap) {
        warehouseService.updateWarehouse(warehouseCode, hashMap, organizationId);
        return Results.success();
    }

    @ApiOperation("保存仓库快递配送接单量限制(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @PostMapping({"/internal/saveExpressQuantity"})
    public ResponseEntity<?> saveExpressQuantity(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                 @RequestParam(value = "warehouseCode") String warehouseCode,
                                                 @RequestParam(value = "expressQuantity") String expressQuantity) {
        warehouseService.saveExpressQuantity(warehouseCode, expressQuantity, organizationId);
        return Results.success();
    }

    @ApiOperation("保存仓库自提接单量限制(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @PostMapping({"/internal/savePickUpQuantity"})
    public ResponseEntity<?> savePickUpQuantity(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                @RequestParam(value = "warehouseCode") String warehouseCode,
                                                @RequestParam(value = "pickUpQuantity") String pickUpQuantity) {
        warehouseService.savePickUpQuantity(warehouseCode, pickUpQuantity, organizationId);
        return Results.success();
    }

    @ApiOperation("仓库快递配送接单量增量更新(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @PostMapping({"/internal/updateExpressValue"})
    public ResponseEntity<?> updateExpressValue(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                @RequestParam(value = "warehouseCode") String warehouseCode,
                                                @RequestParam(value = "increment") String increment) {
        warehouseService.updateExpressValue(warehouseCode, increment, organizationId);
        return Results.success();
    }

    @ApiOperation("仓库自提接单量增量更新(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @PostMapping({"/internal/updatePickUpValue"})
    public ResponseEntity<?> updatePickUpValue(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                               @RequestParam(value = "warehouseCode") String warehouseCode,
                                               @RequestParam(value = "increment") String increment) {
        warehouseService.updatePickUpValue(warehouseCode, increment, organizationId);
        return Results.success();
    }

    @ApiOperation("获取快递配送接单量限制(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @GetMapping({"/internal/getExpressLimit"})
    public ResponseEntity<?> getExpressLimit(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                             @RequestParam(value = "warehouseCode") String warehouseCode) {
        String result = warehouseService.getExpressLimit(warehouseCode, organizationId);
        return Results.success(result);
    }

    @ApiOperation("获取自提接单量限制(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @GetMapping({"/internal/getPickUpLimit"})
    public ResponseEntity<?> getPickUpLimit(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                            @RequestParam(value = "warehouseCode") String warehouseCode) {
        String result = warehouseService.getPickUpLimit(warehouseCode, organizationId);
        return Results.success(result);
    }

    @ApiOperation("获取实际快递配送接单量(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @GetMapping({"/internal/getExpressValue"})
    public ResponseEntity<?> getExpressValue(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                             @RequestParam(value = "warehouseCode") String warehouseCode) {
        String result = warehouseService.getExpressValue(warehouseCode, organizationId);
        return Results.success(result);
    }

    @ApiOperation("获取实际自提接单量(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @GetMapping({"/internal/getPickUpValue"})
    public ResponseEntity<?> getPickUpValue(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                            @RequestParam(value = "warehouseCode") String warehouseCode) {
        String result = warehouseService.getPickUpValue(warehouseCode, organizationId);
        return Results.success(result);
    }

    @ApiOperation("获取仓库缓存KEY(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @GetMapping({"/internal/warehouseCacheKey"})
    public ResponseEntity<?> warehouseCacheKey(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                               @RequestParam(value = "warehouseCode") String warehouseCode) {
        String result = warehouseService.warehouseCacheKey(warehouseCode, organizationId);
        return Results.success(result);
    }

    @ApiOperation("获取仓库limit缓存KEY(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @GetMapping({"/internal/warehouseLimitCacheKey"})
    public ResponseEntity<?> warehouseLimitCacheKey(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                    @RequestParam(value = "limit") String limit) {
        String result = warehouseService.warehouseLimitCacheKey(limit, organizationId);
        return Results.success(result);
    }

    @ApiOperation("是否仓库快递配送接单量到达上限(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @GetMapping({"/internal/isWarehouseExpressLimit"})
    public ResponseEntity<?> isWarehouseExpressLimit(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                     @RequestParam(value = "warehouseCode") String warehouseCode) {
        Boolean result = warehouseService.isWarehouseExpressLimit(warehouseCode, organizationId);
        return Results.success(result);
    }

    @ApiOperation("是否仓库自提接单量到达上限(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @GetMapping({"/internal/isWarehousePickUpLimit"})
    public ResponseEntity<?> isWarehousePickUpLimit(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                    @RequestParam(value = "warehouseCode") String warehouseCode) {
        Boolean result = warehouseService.isWarehousePickUpLimit(warehouseCode, organizationId);
        return Results.success(result);
    }

    @ApiOperation("获取快递配送接单量到达上限的仓库(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @GetMapping({"/internal/expressLimitWarehouseCollection"})
    public ResponseEntity<?> expressLimitWarehouseCollection(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId) {
        Set<String> result = warehouseService.expressLimitWarehouseCollection(organizationId);
        return Results.success(result);
    }

    @ApiOperation("获取自提接单量到达上限的仓库(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @GetMapping({"/internal/pickUpLimitWarehouseCollection"})
    public ResponseEntity<?> pickUpLimitWarehouseCollection(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId) {
        Set<String> result = warehouseService.pickUpLimitWarehouseCollection(organizationId);
        return Results.success(result);
    }

    @ApiOperation("重置仓库快递配送接单量值(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @PostMapping({"/internal/resetWarehouseExpressLimit"})
    public ResponseEntity<?> resetWarehouseExpressLimit(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                        @RequestParam(value = "warehouseCode", required = true) String warehouseCode) {
        warehouseService.resetWarehouseExpressLimit(warehouseCode, organizationId);
        return Results.success();
    }

    @ApiOperation("重置仓库自提接单量限制值(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @PostMapping({"/internal/resetWarehousePickUpLimit"})
    public ResponseEntity<?> resetWarehousePickUpLimit(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                        @RequestParam(value = "warehouseCode", required = true) String warehouseCode) {
        warehouseService.resetWarehousePickUpLimit(warehouseCode, organizationId);
        return Results.success();
    }
    @ApiOperation(value = "从redis查询仓库")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @GetMapping("/internal//{warehouseCode}")
    public ResponseEntity<WarehouseVO> getWarehouse(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                    @PathVariable(value = "warehouseCode") @ApiParam(value = "参数code", required = true) String warehouseCode) {
        return Results.success(warehouseService.getWarehouse(warehouseCode, organizationId));
    }
}
