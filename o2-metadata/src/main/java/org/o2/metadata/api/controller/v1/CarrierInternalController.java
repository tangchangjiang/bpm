package org.o2.metadata.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.o2.core.helper.UserHelper;
import org.o2.metadata.api.vo.CarrierVO;
import org.o2.metadata.app.service.CarrierService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 *
 * 运费
 *
 * @author yipeng.zhu@hand-china.com 2021-07-14
 **/
@RestController("carrierInternalController.v1")
@RequestMapping({"v1/carrier-internal"})
public class CarrierInternalController {
    private final CarrierService carrierService;

    public CarrierInternalController(CarrierService carrierService) {
        this.carrierService = carrierService;
    }

    @ApiOperation(value = "查询承运商信息")
    @Permission(permissionPublic = true, level = ResourceLevel.ORGANIZATION)
    @PostMapping("/template")
    public ResponseEntity<CarrierVO> getFreightTemplate() {
        return Results.success(carrierService.listCarriers(UserHelper.getTenantId()));
    }
}
