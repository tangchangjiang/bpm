package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.o2.metadata.console.api.co.WarehouseCO;
import org.o2.metadata.console.api.co.WarehouseRelAddressCO;
import org.o2.metadata.console.api.dto.WarehousePageQueryInnerDTO;
import org.o2.metadata.console.api.dto.WarehouseQueryInnerDTO;
import org.o2.metadata.console.app.service.OnlineShopRelWarehouseService;
import org.o2.metadata.console.app.service.WarehouseService;
import org.o2.metadata.console.infra.config.MetadataManagementAutoConfiguration;
import org.o2.metadata.console.infra.entity.OnlineShopRelWarehouse;
import org.o2.metadata.console.infra.entity.Warehouse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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
public class WarehouseInternalController extends BaseController {

    private final WarehouseService warehouseService;

    private final OnlineShopRelWarehouseService onlineShopRelWarehouseService;

    public WarehouseInternalController(WarehouseService warehouseService, OnlineShopRelWarehouseService onlineShopRelWarehouseService) {
        this.warehouseService = warehouseService;
        this.onlineShopRelWarehouseService = onlineShopRelWarehouseService;
    }

    @ApiOperation(value = "仓库信息列表 wms调用")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/page")
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    public ResponseEntity<Page<WarehouseCO>> pageWarehouses(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                            PageRequest pageRequest,
                                                            @RequestBody(required = false) WarehousePageQueryInnerDTO innerDTO) {

        Page<WarehouseCO> posList;
        if (null == innerDTO) {
            WarehousePageQueryInnerDTO query = new WarehousePageQueryInnerDTO();
            query.setTenantId(organizationId);
            posList = PageHelper.doPage(pageRequest,
                    () -> warehouseService.pageWarehouses(query));
            return Results.success(posList);
        }
        innerDTO.setTenantId(organizationId);
        posList = PageHelper.doPage(pageRequest,
                () -> warehouseService.pageWarehouses(innerDTO));
        return Results.success(posList);
    }

    @ApiOperation(value = "查询仓库(内部调用)")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @PostMapping("/list")
    public ResponseEntity<Map<String, WarehouseCO>> listWarehouses(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                   @RequestBody WarehouseQueryInnerDTO queryInnerDTO) {
        Map<String, WarehouseCO> map = new HashMap<>(16);
        List<WarehouseCO> cos = warehouseService.listWarehouses(queryInnerDTO, organizationId);
        if (cos.isEmpty()) {
            return Results.success(map);
        }
        //寻源查询出多个网店关联仓库，需要特殊处理
        if (Boolean.TRUE.equals(queryInnerDTO.getSourcingFlag())) {
            for (WarehouseCO co : cos) {
                map.put(co.getWarehouseCode() + BaseConstants.Symbol.COLON + co.getOnlineShopCode(), co);
            }
        } else {
            for (WarehouseCO co : cos) {
                map.put(co.getWarehouseCode(), co);
            }
        }
        return Results.success(map);
    }

    @ApiOperation("仓库快递配送接单量增量更新(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @PostMapping({"/updateExpressValue"})
    public ResponseEntity<Long> updateExpressValue(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                   @RequestParam(value = "warehouseCode") String warehouseCode,
                                                   @RequestParam(value = "increment") String increment) {
        return Results.success(warehouseService.updateExpressValue(warehouseCode, increment, organizationId));
    }

    @ApiOperation("仓库自提接单量增量更新(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @PostMapping({"/updatePickUpValue"})
    public ResponseEntity<Long> updatePickUpValue(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                  @RequestParam(value = "warehouseCode") String warehouseCode,
                                                  @RequestParam(value = "increment") String increment) {
        return Results.success(warehouseService.updatePickUpValue(warehouseCode, increment, organizationId));
    }

    @ApiOperation("获取仓库limit缓存KEY(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @GetMapping({"/warehouseLimitCacheKey"})
    public ResponseEntity<String> warehouseLimitCacheKey(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                         @RequestParam(value = "limit") String limit) {
        return Results.success(warehouseService.warehouseLimitCacheKey(limit, organizationId));
    }

    @ApiOperation("是否仓库快递配送接单量到达上限(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @GetMapping({"/isWarehouseExpressLimit"})
    public ResponseEntity<Boolean> isWarehouseExpressLimit(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                           @RequestParam(value = "warehouseCode") String warehouseCode) {
        Boolean result = warehouseService.isWarehouseExpressLimit(warehouseCode, organizationId);
        return Results.success(result);
    }

    @ApiOperation("是否仓库自提接单量到达上限(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @GetMapping({"/isWarehousePickUpLimit"})
    public ResponseEntity<Boolean> isWarehousePickUpLimit(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                          @RequestParam(value = "warehouseCode") String warehouseCode) {
        return Results.success(warehouseService.isWarehousePickUpLimit(warehouseCode, organizationId));
    }

    @ApiOperation("获取快递配送接单量到达上限的仓库(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @GetMapping({"/expressLimitWarehouseCollection"})
    public ResponseEntity<Set<String>> expressLimitWarehouseCollection(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId) {
        return Results.success(warehouseService.expressLimitWarehouseCollection(organizationId));
    }

    @ApiOperation("获取自提接单量到达上限的仓库(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @GetMapping({"/pickUpLimitWarehouseCollection"})
    public ResponseEntity<Set<String>> pickUpLimitWarehouseCollection(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId) {
        return Results.success(warehouseService.pickUpLimitWarehouseCollection(organizationId));
    }

    @ApiOperation("查询可发货仓库(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @PostMapping({"/allDeliveryWarehouse"})
    public ResponseEntity<List<WarehouseRelAddressCO>> listAllDeliveryWarehouse(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId) {
        return Results.success(warehouseService.selectAllDeliveryWarehouse(organizationId));
    }

    @ApiOperation("重置仓库自提接单量限制值(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @PostMapping({"/resetWarehousePickUpLimit"})
    public ResponseEntity<Void> resetWarehousePickUpLimit(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                          @RequestParam(value = "warehouseCode", required = true) String warehouseCode) {
        warehouseService.resetWarehousePickUpLimit(warehouseCode, organizationId);
        return Results.success();
    }

    @ApiOperation(value = "查询仓库(内部调用)")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @PostMapping("/list-warehouse")
    public ResponseEntity<Map<String, List<WarehouseCO>>> listWarehousesByPosCode(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                                  @RequestParam(value = "posCodes") List<String> posCodes) {
        Map<String, List<WarehouseCO>> map = new HashMap<>(16);
        List<WarehouseCO> cos = warehouseService.listWarehousesByPosCode(posCodes, organizationId);
        if (cos.isEmpty()) {
            return Results.success(map);
        }
        for (WarehouseCO co : cos) {
            map.computeIfAbsent(co.getPosCode(), key -> new ArrayList<>()).add(co);
        }
        return Results.success(map);
    }

    @ApiOperation(value = "批量保存门店(内部调用)")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @PostMapping("/warehouses")
    public ResponseEntity<List<Warehouse>> batchSaveWarehouses(@PathVariable(value = "organizationId")
                                                               @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                               @RequestBody final List<Warehouse> warehouses) {
        warehouses.forEach(w -> {
            w.setTenantId(organizationId);
            w.setActiveFlag(BaseConstants.Flag.YES);
            this.validObject(w);
        });
        List<Warehouse> warehouseList = warehouseService.batchSave(organizationId, warehouses);
        return Results.success(warehouseList);
    }


    @ApiOperation(value = "网店关联仓库(内部调用)")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @PostMapping("/warehouse/shop")
    public ResponseEntity<List<OnlineShopRelWarehouse>> createWarehouseRelShop(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                               @RequestBody final List<OnlineShopRelWarehouse> onlineShopRelWarehouseList) {
        this.validList(onlineShopRelWarehouseList);
        final List<OnlineShopRelWarehouse> relationShips = onlineShopRelWarehouseService.batchInsertSelective(organizationId,
                onlineShopRelWarehouseList);
        return Results.success(relationShips);
    }
}
