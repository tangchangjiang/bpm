package org.o2.metadata.api.controller.v1;

import com.google.common.collect.Maps;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.util.Results;
import org.o2.metadata.api.vo.WarehouseCO;
import org.o2.metadata.app.service.WarehouseService;
import org.o2.metadata.config.MetadataAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
@Api(tags = {MetadataAutoConfiguration.SYS_WAREHOUSE_INTERNAL})
@RestController("warehouseMetadataInternalController.v1")
@RequestMapping("v1/{organizationId}")
public class WarehouseMetadataInternalController {
    private WarehouseService warehouseService;

    public WarehouseMetadataInternalController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }
    @ApiOperation(value = "查询仓库")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @GetMapping("/internal/list")
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
    @PostMapping({"/internal/updateExpressValue"})
    public ResponseEntity<Void> updateExpressValue(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                @RequestParam(value = "warehouseCode") String warehouseCode,
                                                @RequestParam(value = "increment") String increment) {
        warehouseService.updateExpressValue(warehouseCode, increment, organizationId);
        return Results.success();
    }
}
