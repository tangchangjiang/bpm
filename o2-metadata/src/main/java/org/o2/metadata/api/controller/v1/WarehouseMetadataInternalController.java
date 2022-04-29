package org.o2.metadata.api.controller.v1;

import com.google.common.collect.Maps;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.util.Results;
import org.o2.metadata.api.co.WarehouseCO;
import org.o2.metadata.app.service.WarehouseService;
import org.o2.metadata.config.MetadataAutoConfiguration;
import org.o2.metadata.infra.entity.WarehouseLimit;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
@Api(tags = {MetadataAutoConfiguration.SYS_WAREHOUSE_INTERNAL})
@RestController("warehouseMetadataInternalController.v1")
@RequestMapping("v1/{organizationId}/warehouse-internal")
public class WarehouseMetadataInternalController {
    private WarehouseService warehouseService;

    public WarehouseMetadataInternalController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }
    @ApiOperation(value = "查询仓库")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @GetMapping("/list")
    public ResponseEntity<Map<String, WarehouseCO>> listWarehouses(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                   @RequestParam List<String> warehouseCodes) {
        Map<String, WarehouseCO> map = Maps.newHashMapWithExpectedSize(warehouseCodes.size());
        List<WarehouseCO> vos = warehouseService.listWarehouses(warehouseCodes, organizationId);
        if (vos.isEmpty()){
            return Results.success(map);
        }
        for (WarehouseCO warehouseVO : vos) {
            map.put(warehouseVO.getWarehouseCode(), warehouseVO);
        }
        return  Results.success(map);
    }
    @ApiOperation("仓库快递配送接单量增量更新(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @PostMapping({"/updateExpressValue"})
    public ResponseEntity<Void> updateExpressValue(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                @RequestParam(value = "warehouseCode") String warehouseCode,
                                                @RequestParam(value = "increment") String increment) {
        warehouseService.updateExpressValue(warehouseCode, increment, organizationId);
        return Results.success();
    }

    @ApiOperation(value = "查询仓库已自提量")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @GetMapping("/limit-list")
    public ResponseEntity<Map<String, WarehouseLimit>> listWarehousePickupLimit(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                                                @RequestParam List<String> warehouseCodes) {
        Map<String, WarehouseLimit> limitMap = warehouseService.listWarehousePickupLimit(warehouseCodes, organizationId);
        return Results.success(limitMap);
    }

    @ApiOperation(value = "查询仓库")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @GetMapping("/list-pos")
    public ResponseEntity<Map<String, List<WarehouseCO>>> listWarehousesByPosCode(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                   @RequestParam("posCodes") List<String> posCodes) {
        Map<String, List<WarehouseCO>> map = Maps.newHashMapWithExpectedSize(posCodes.size());
        List<WarehouseCO> vos = warehouseService.listWarehousesByPosCode(posCodes, organizationId);
        if (vos.isEmpty()){
            return Results.success(map);
        }
        for (WarehouseCO co : vos) {
            map.computeIfAbsent(co.getPosCode(),key -> new ArrayList<>()).add(co);
        }
        return  Results.success(map);
    }
}
