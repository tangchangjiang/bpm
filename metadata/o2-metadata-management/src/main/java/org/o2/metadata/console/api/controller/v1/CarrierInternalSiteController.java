package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.o2.metadata.console.api.co.CarrierCO;
import org.o2.metadata.console.api.dto.CarrierQueryInnerDTO;
import org.o2.metadata.console.app.service.CarrierService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 承运商内部接口 站点级
 *
 * @author chao.yang05@hand-china.com 2023-04-17
 */
@RestController("carrierInternalSiteController.v1")
@RequestMapping("/v1/carrier-internal")
public class CarrierInternalSiteController extends BaseController {

    private final CarrierService carrierService;

    public CarrierInternalSiteController(CarrierService carrierService) {
        this.carrierService = carrierService;
    }

    @ApiOperation(value = "多租户查询承运商（站点级）")
    @Permission(permissionWithin = true, level = ResourceLevel.SITE)
    @PostMapping("/list-batch-tenant")
    public ResponseEntity<Map<Long, Map<String, CarrierCO>>> listBatchTenant(@RequestBody Map<Long, CarrierQueryInnerDTO> carrierQueryInnerDTOMap) {
        Map<Long, Map<String, CarrierCO>> map = new HashMap<>();
        carrierQueryInnerDTOMap.forEach((tenantId, queryDTO) -> map.put(tenantId, carrierService.listCarriers(queryDTO, tenantId)));
        return Results.success(map);
    }
}
