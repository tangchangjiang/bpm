package org.o2.metadata.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.util.Results;
import org.o2.metadata.api.co.CarrierCO;
import org.o2.metadata.app.service.CarrierService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 *
 * 运费
 *
 * @author yipeng.zhu@hand-china.com 2021-07-14
 **/
@RestController("CarrierInternalController.v1")
@RequestMapping({"v1/{organizationId}/carriers-internal"})
public class CarrierInternalController {
    private final CarrierService carrierService;

    public CarrierInternalController(CarrierService carrierService) {
        this.carrierService = carrierService;
    }

    @ApiOperation(value = "查询承运商信息")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @GetMapping("/list")
    public ResponseEntity<List<CarrierCO>> listCarriers(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId) {
        return Results.success(carrierService.listCarriers(organizationId));
    }
}
