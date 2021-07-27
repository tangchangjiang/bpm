package org.o2.metadata.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.util.Results;
import org.o2.metadata.api.vo.WarehouseVO;
import org.o2.metadata.app.service.WarehouseService;
import org.o2.metadata.config.MetadataAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @ApiOperation(value = "从redis查询仓库")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @GetMapping("/internal//{warehouseCode}")
    public ResponseEntity<WarehouseVO> getWarehouse(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                    @PathVariable(value = "warehouseCode") @ApiParam(value = "参数code", required = true) String warehouseCode) {
        return Results.success(warehouseService.getWarehouse(warehouseCode, organizationId));
    }
}
