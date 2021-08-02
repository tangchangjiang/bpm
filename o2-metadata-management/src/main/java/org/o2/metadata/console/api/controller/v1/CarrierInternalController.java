package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.o2.metadata.console.api.dto.CarrierDTO;
import org.o2.metadata.console.api.vo.CarrierVO;
import org.o2.metadata.console.app.service.CarrierService;
import org.o2.metadata.console.config.MetadataManagementAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 承运商 管理 API
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@RestController("carrierController.v1")
@RequestMapping("/v1/{organizationId}/carrier-internal")
@Api(tags = MetadataManagementAutoConfiguration.CARRIER)
public class CarrierInternalController extends BaseController {
    private final CarrierService carrierService;

    public CarrierInternalController(CarrierService carrierService) {
        this.carrierService = carrierService;
    }


    @ApiOperation(value = "承运商")
    @Permission(permissionWithin =  true,level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @PostMapping("/list")
    public ResponseEntity<Map<String, CarrierVO>> list(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody CarrierDTO carrierDTO) {
        return Results.success(carrierService.listCarriers(carrierDTO,organizationId));
    }


}
