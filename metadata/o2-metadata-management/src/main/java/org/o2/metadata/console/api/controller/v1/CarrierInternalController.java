package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.o2.metadata.console.api.co.CarrierCO;
import org.o2.metadata.console.api.co.CarrierDeliveryRangeCO;
import org.o2.metadata.console.api.co.CarrierLogisticsCostCO;
import org.o2.metadata.console.api.co.CarrierMappingCO;
import org.o2.metadata.console.api.dto.CarrierDeliveryRangeDTO;
import org.o2.metadata.console.api.dto.CarrierLogisticsCostDTO;
import org.o2.metadata.console.api.dto.CarrierMappingQueryInnerDTO;
import org.o2.metadata.console.api.dto.CarrierQueryInnerDTO;
import org.o2.metadata.console.app.service.CarrierService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 承运商 管理 API
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@RestController("carrierInternalController.v1")
@RequestMapping("/v1/{organizationId}/carrier-internal")
public class CarrierInternalController extends BaseController {
    private final CarrierService carrierService;

    public CarrierInternalController(CarrierService carrierService) {
        this.carrierService = carrierService;
    }

    @ApiOperation(value = "承运商")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @PostMapping("/list")
    public ResponseEntity<Map<String, CarrierCO>> list(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                       @RequestBody CarrierQueryInnerDTO carrierQueryInnerDTO) {
        return Results.success(carrierService.listCarriers(carrierQueryInnerDTO, organizationId));
    }

    @ApiOperation(value = "承运商匹配")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @PostMapping("/mapping")
    public ResponseEntity<Map<String, CarrierMappingCO>> listCarrierMappings(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody CarrierMappingQueryInnerDTO queryInnerDTO) {
        return Results.success(carrierService.listCarrierMappings(queryInnerDTO, organizationId));
    }

    @ApiOperation(value = "导入承运商列表")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @PostMapping("/import-list")
    public ResponseEntity<Map<String, CarrierCO>> importList(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId) {
        return Results.success(carrierService.importListCarriers(organizationId));
    }

    @ApiOperation(value = "承运商物流成本计算")
//    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @Permission(permissionPublic = true)
    @PostMapping("/calculate-logistics-cost")
    public ResponseEntity<List<CarrierLogisticsCostCO>> calculateLogisticsCost(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                               @RequestBody CarrierLogisticsCostDTO carrierLogisticsCostDTO) {
        carrierLogisticsCostDTO.setTenantId(organizationId);
        validObject(carrierLogisticsCostDTO);
        return Results.success(carrierService.calculateLogisticsCost(carrierLogisticsCostDTO));

    }

    @ApiOperation(value = "查询收货地址是否在承运商的送达范围内")
//    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @Permission(permissionPublic = true)
    @PostMapping("/check-delivery-range")
    public ResponseEntity<List<CarrierDeliveryRangeCO>> checkDeliveryRange(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                           @RequestBody CarrierDeliveryRangeDTO carrierDeliveryRangeDTO) {
        carrierDeliveryRangeDTO.setTenantId(organizationId);
        validObject(carrierDeliveryRangeDTO);
        return Results.success(carrierService.checkDeliveryRange(carrierDeliveryRangeDTO));

    }

}
