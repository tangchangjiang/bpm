package org.o2.metadata.console.api.controller.v1;

import com.google.common.collect.Maps;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.util.Results;
import org.o2.metadata.console.api.dto.WarehouseQueryInnerDTO;
import org.o2.metadata.console.api.vo.WarehouseVO;
import org.o2.metadata.console.app.service.WarehouseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
@RestController("warehouseInternalController.v1")
@RequestMapping("v1/{organizationId}/warehouse-internal")
public class WarehouseInternalController {

    private WarehouseService warehouseService;

    public WarehouseInternalController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @ApiOperation(value = "查询仓库")
    @Permission(permissionPublic = true, level = ResourceLevel.ORGANIZATION)
    @GetMapping("/list")
    public ResponseEntity<Map<String, WarehouseVO>> listWarehouses(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                   @RequestParam (required = false) List<String> warehouseCodes) {
        Map<String,WarehouseVO> map = new HashMap<>(16);
        WarehouseQueryInnerDTO queryInnerDTO = new WarehouseQueryInnerDTO();
        queryInnerDTO.setWarehouseCodes(warehouseCodes);
        List<WarehouseVO> vos = warehouseService.listWarehouses(queryInnerDTO, organizationId);
        if (vos.isEmpty()){
          return Results.success(map);
        }
        for (WarehouseVO warehouseVO : vos) {
            map.put(warehouseVO.getWarehouseCode(), warehouseVO);
        }
        return  Results.success(map);
    }

    @ApiOperation(value = "查询有效仓库")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @GetMapping("/active/{onlineShopCode}")
    public ResponseEntity<List<WarehouseVO>> listActiveWarehouse(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                 @PathVariable(value = "onlineShopCode") @ApiParam(value = "网店编码", required = true) String onlineShopCode) {
        return Results.success(warehouseService.listActiveWarehouses(onlineShopCode, organizationId));
    }

    @ApiOperation("保存仓库快递配送接单量限制(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @PostMapping({"/saveExpressQuantity"})
    public ResponseEntity<?> saveExpressQuantity(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                 @RequestParam(value = "warehouseCode") String warehouseCode,
                                                 @RequestParam(value = "expressQuantity") String expressQuantity) {
        warehouseService.saveExpressQuantity(warehouseCode, expressQuantity, organizationId);
        return Results.success();
    }

    @ApiOperation("保存仓库自提接单量限制(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @PostMapping({"/savePickUpQuantity"})
    public ResponseEntity<?> savePickUpQuantity(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                @RequestParam(value = "warehouseCode") String warehouseCode,
                                                @RequestParam(value = "pickUpQuantity") String pickUpQuantity) {
        warehouseService.savePickUpQuantity(warehouseCode, pickUpQuantity, organizationId);
        return Results.success();
    }

    @ApiOperation("仓库快递配送接单量增量更新(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @PostMapping({"/updateExpressValue"})
    public ResponseEntity<?> updateExpressValue(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                @RequestParam(value = "warehouseCode") String warehouseCode,
                                                @RequestParam(value = "increment") String increment) {
        warehouseService.updateExpressValue(warehouseCode, increment, organizationId);
        return Results.success();
    }

    @ApiOperation("仓库自提接单量增量更新(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @PostMapping({"/updatePickUpValue"})
    public ResponseEntity<?> updatePickUpValue(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                               @RequestParam(value = "warehouseCode") String warehouseCode,
                                               @RequestParam(value = "increment") String increment) {
        warehouseService.updatePickUpValue(warehouseCode, increment, organizationId);
        return Results.success();
    }

    @ApiOperation("获取快递配送接单量限制(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @GetMapping({"/getExpressLimit"})
    public ResponseEntity<?> getExpressLimit(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                             @RequestParam(value = "warehouseCode") String warehouseCode) {
        String result = warehouseService.getExpressLimit(warehouseCode, organizationId);
        return Results.success(result);
    }

    @ApiOperation("获取自提接单量限制(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @GetMapping({"/getPickUpLimit"})
    public ResponseEntity<?> getPickUpLimit(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                            @RequestParam(value = "warehouseCode") String warehouseCode) {
        String result = warehouseService.getPickUpLimit(warehouseCode, organizationId);
        return Results.success(result);
    }

    @ApiOperation("获取实际快递配送接单量(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @GetMapping({"/getExpressValue"})
    public ResponseEntity<?> getExpressValue(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                             @RequestParam(value = "warehouseCode") String warehouseCode) {
        String result = warehouseService.getExpressValue(warehouseCode, organizationId);
        return Results.success(result);
    }

    @ApiOperation("获取实际自提接单量(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @GetMapping({"/getPickUpValue"})
    public ResponseEntity<?> getPickUpValue(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                            @RequestParam(value = "warehouseCode") String warehouseCode) {
        String result = warehouseService.getPickUpValue(warehouseCode, organizationId);
        return Results.success(result);
    }


    @ApiOperation("获取仓库limit缓存KEY(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @GetMapping({"/warehouseLimitCacheKey"})
    public ResponseEntity<?> warehouseLimitCacheKey(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                    @RequestParam(value = "limit") String limit) {
        String result = warehouseService.warehouseLimitCacheKey(limit, organizationId);
        return Results.success(result);
    }

    @ApiOperation("是否仓库快递配送接单量到达上限(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @GetMapping({"/isWarehouseExpressLimit"})
    public ResponseEntity<?> isWarehouseExpressLimit(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                     @RequestParam(value = "warehouseCode") String warehouseCode) {
        Boolean result = warehouseService.isWarehouseExpressLimit(warehouseCode, organizationId);
        return Results.success(result);
    }

    @ApiOperation("是否仓库自提接单量到达上限(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @GetMapping({"/isWarehousePickUpLimit"})
    public ResponseEntity<?> isWarehousePickUpLimit(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                    @RequestParam(value = "warehouseCode") String warehouseCode) {
        Boolean result = warehouseService.isWarehousePickUpLimit(warehouseCode, organizationId);
        return Results.success(result);
    }

    @ApiOperation("获取快递配送接单量到达上限的仓库(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @GetMapping({"/expressLimitWarehouseCollection"})
    public ResponseEntity<?> expressLimitWarehouseCollection(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId) {
        Set<String> result = warehouseService.expressLimitWarehouseCollection(organizationId);
        return Results.success(result);
    }

    @ApiOperation("获取自提接单量到达上限的仓库(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @GetMapping({"/pickUpLimitWarehouseCollection"})
    public ResponseEntity<?> pickUpLimitWarehouseCollection(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId) {
        Set<String> result = warehouseService.pickUpLimitWarehouseCollection(organizationId);
        return Results.success(result);
    }

    @ApiOperation("重置仓库快递配送接单量值(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @PostMapping({"/resetWarehouseExpressLimit"})
    public ResponseEntity<?> resetWarehouseExpressLimit(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                        @RequestParam(value = "warehouseCode", required = true) String warehouseCode) {
        warehouseService.resetWarehouseExpressLimit(warehouseCode, organizationId);
        return Results.success();
    }

    @ApiOperation("重置仓库自提接单量限制值(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @PostMapping({"/resetWarehousePickUpLimit"})
    public ResponseEntity<?> resetWarehousePickUpLimit(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                       @RequestParam(value = "warehouseCode", required = true) String warehouseCode) {
        warehouseService.resetWarehousePickUpLimit(warehouseCode, organizationId);
        return Results.success();
    }
}
