package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.util.Results;
import org.o2.metadata.console.api.vo.WarehouseVO;
import org.o2.metadata.console.app.service.WarehouseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
@RestController("warehouseInternalController.v1")
@RequestMapping("v1/{organizationId}/warehouse")
public class WarehouseInternalController {

    private WarehouseService warehouseService;

    public WarehouseInternalController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }


    @ApiOperation(value = "从redis查询仓库")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{warehouseCode}")
    public ResponseEntity<WarehouseVO> getWarehouse(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                    @PathVariable(value = "warehouseCode") @ApiParam(value = "参数code", required = true) String warehouseCode) {
        return Results.success(warehouseService.getWarehouse(warehouseCode, organizationId));
    }
}
