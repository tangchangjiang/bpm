package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.util.Results;
import org.o2.metadata.console.api.co.CarrierMappingCO;
import org.o2.metadata.console.api.dto.CarrierMappingQueryInnerDTO;
import org.o2.metadata.console.api.dto.CarrierQueryInnerDTO;
import org.o2.metadata.console.api.co.CarrierCO;
import org.o2.metadata.console.app.service.CarrierService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 承运商 管理 API
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@RestController("carrierInternalController.v1")
@RequestMapping("/v1/{organizationId}/carrier-internal")
public class CarrierInternalController{
    private final CarrierService carrierService;

    public CarrierInternalController(CarrierService carrierService) {
        this.carrierService = carrierService;
    }


    @ApiOperation(value = "承运商")
    @Permission(permissionWithin =  true,level = ResourceLevel.ORGANIZATION)
    @PostMapping("/list")
    public ResponseEntity<Map<String, CarrierCO>> list(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody CarrierQueryInnerDTO carrierQueryInnerDTO) {
        return Results.success(carrierService.listCarriers(carrierQueryInnerDTO,organizationId));
    }

    @ApiOperation(value = "承运商匹配")
    @Permission(permissionWithin =  true,level = ResourceLevel.ORGANIZATION)
    @PostMapping("/mapping")
    public ResponseEntity<Map<String, CarrierMappingCO>> listCarrierMappings(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody CarrierMappingQueryInnerDTO queryInnerDTO) {
        return Results.success(carrierService.listCarrierMappings(queryInnerDTO,organizationId));
    }

    @ApiOperation(value = "导入承运商列表")
    @Permission(permissionWithin =  true,level = ResourceLevel.ORGANIZATION)
    @PostMapping("/import-list")
    public ResponseEntity<Map<String, CarrierCO>> importList(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId) {
        return Results.success(carrierService.importListCarriers(organizationId));
    }


}
