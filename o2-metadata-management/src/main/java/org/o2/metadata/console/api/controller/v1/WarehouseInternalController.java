package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.*;

import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.util.Results;
import org.o2.metadata.console.api.co.WarehouseCO;
import org.o2.metadata.console.api.dto.WarehousePageQueryInnerDTO;
import org.o2.metadata.console.api.dto.WarehouseQueryInnerDTO;
import org.o2.metadata.console.app.service.WarehouseService;
import org.o2.metadata.console.infra.config.MetadataManagementAutoConfiguration;
import org.o2.metadata.console.infra.entity.Warehouse;
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
@Api(tags = MetadataManagementAutoConfiguration.WAREHOUSE)
public class WarehouseInternalController {

    private WarehouseService warehouseService;
    public WarehouseInternalController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @ApiOperation(value = "仓库信息列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    public ResponseEntity<Page<WarehouseCO>> pageWarehouses(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                  WarehousePageQueryInnerDTO innerDTO) {
        innerDTO.setTenantId(organizationId);
        PageRequest pageRequest = new PageRequest();
        pageRequest.setSize(innerDTO.getPageSize());
        pageRequest.setPage(innerDTO.getPage());
        final Page<WarehouseCO> posList = PageHelper.doPage(pageRequest,
                () -> warehouseService.pageWarehouses(innerDTO));
        return Results.success(posList);
    }

    @ApiOperation(value = "查询仓库(内部调用)")
    @Permission(permissionPublic = true, level = ResourceLevel.ORGANIZATION)
    @PostMapping("/list")
    public ResponseEntity<Map<String, WarehouseCO>> listWarehouses(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                   @RequestBody WarehouseQueryInnerDTO queryInnerDTO) {
        Map<String, WarehouseCO> map = new HashMap<>(16);
        List<WarehouseCO> cos = warehouseService.listWarehouses(queryInnerDTO, organizationId);
        if (cos.isEmpty()){
          return Results.success(map);
        }
        for (WarehouseCO co : cos) {
            map.put(co.getWarehouseCode(), co);
        }
        return  Results.success(map);
    }


    @ApiOperation("仓库快递配送接单量增量更新(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionPublic = true)
    @PostMapping({"/updateExpressValue"})
    public ResponseEntity<Integer> updateExpressValue(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                @RequestParam(value = "warehouseCode") String warehouseCode,
                                                @RequestParam(value = "increment") String increment) {
        return Results.success(warehouseService.updateExpressValue(warehouseCode, increment, organizationId));
    }

    @ApiOperation("仓库自提接单量增量更新(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionPublic = true)
    @PostMapping({"/updatePickUpValue"})
    public ResponseEntity<Integer> updatePickUpValue(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                               @RequestParam(value = "warehouseCode") String warehouseCode,
                                               @RequestParam(value = "increment") String increment) {
        return Results.success(warehouseService.updatePickUpValue(warehouseCode, increment, organizationId));
    }

    @ApiOperation("获取仓库limit缓存KEY(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionPublic = true)
    @GetMapping({"/warehouseLimitCacheKey"})
    public ResponseEntity<String> warehouseLimitCacheKey(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                    @RequestParam(value = "limit") String limit) {
        return Results.success(warehouseService.warehouseLimitCacheKey(limit, organizationId));
    }

    @ApiOperation("是否仓库快递配送接单量到达上限(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionPublic = true)
    @GetMapping({"/isWarehouseExpressLimit"})
    public ResponseEntity<Boolean> isWarehouseExpressLimit(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                     @RequestParam(value = "warehouseCode") String warehouseCode) {
        Boolean result = warehouseService.isWarehouseExpressLimit(warehouseCode, organizationId);
        return Results.success(result);
    }

    @ApiOperation("是否仓库自提接单量到达上限(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionPublic = true)
    @GetMapping({"/isWarehousePickUpLimit"})
    public ResponseEntity<Boolean> isWarehousePickUpLimit(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                    @RequestParam(value = "warehouseCode") String warehouseCode) {
        return Results.success(warehouseService.isWarehousePickUpLimit(warehouseCode, organizationId));
    }

    @ApiOperation("获取快递配送接单量到达上限的仓库(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionPublic = true)
    @GetMapping({"/expressLimitWarehouseCollection"})
    public ResponseEntity<Set<String>> expressLimitWarehouseCollection(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId) {
        return Results.success(warehouseService.expressLimitWarehouseCollection(organizationId));
    }

    @ApiOperation("获取自提接单量到达上限的仓库(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionPublic = true)
    @GetMapping({"/pickUpLimitWarehouseCollection"})
    public ResponseEntity<Set<String> > pickUpLimitWarehouseCollection(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId) {
        return Results.success(warehouseService.pickUpLimitWarehouseCollection(organizationId));
    }

    @ApiOperation("重置仓库快递配送接单量值(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionPublic = true)
    @PostMapping({"/resetWarehouseExpressLimit"})
    public ResponseEntity<Void> resetWarehouseExpressLimit(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                        @RequestParam(value = "warehouseCode", required = true) String warehouseCode) {
        warehouseService.resetWarehouseExpressLimit(warehouseCode, organizationId);
        return Results.success();
    }

    @ApiOperation("重置仓库自提接单量限制值(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionPublic = true)
    @PostMapping({"/resetWarehousePickUpLimit"})
    public ResponseEntity<Void> resetWarehousePickUpLimit(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                       @RequestParam(value = "warehouseCode", required = true) String warehouseCode) {
        warehouseService.resetWarehousePickUpLimit(warehouseCode, organizationId);
        return Results.success();
    }
}
