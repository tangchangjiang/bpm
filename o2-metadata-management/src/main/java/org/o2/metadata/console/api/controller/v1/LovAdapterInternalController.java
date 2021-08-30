package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.util.Results;
import org.o2.lov.domain.bo.CurrencyBO;
import org.o2.lov.domain.bo.UomBO;
import org.o2.lov.domain.bo.UomTypeBO;
import org.o2.metadata.console.app.service.LovAdapterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 *
 * 值集查询
 *
 * @author yipeng.zhu@hand-china.com 2021-08-30
 **/
@RestController("lovAdapterInternalController.v1")
@RequestMapping("/v1/{organizationId}/lov")
public class LovAdapterInternalController {
    private final LovAdapterService lovAdapterService;

    public LovAdapterInternalController(LovAdapterService lovAdapterService) {
        this.lovAdapterService = lovAdapterService;
    }

    @ApiOperation(value = "通过编码查询货币(批量)")
    @Permission(permissionPublic = true , level = ResourceLevel.ORGANIZATION)
    @GetMapping("/currency-by-codes")
    public ResponseEntity<Map<String, CurrencyBO>> findCurrencyByCodes(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                       @RequestParam(value = "currencyCodes" ,required = false) List<String> currencyCodes) {
        return Results.success(lovAdapterService.findCurrencyByCodes(organizationId,currencyCodes));
    }

    @ApiOperation(value = "通过编码查询单位(批量)")
    @Permission(permissionPublic = true , level = ResourceLevel.ORGANIZATION)
    @GetMapping("/uom-by-codes")
    public ResponseEntity<Map<String, UomBO>> findUomByCodes(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                             @RequestParam(value = "uomCodes",required = false) List<String> uomCodes) {
        return Results.success(lovAdapterService.findUomByCodes(organizationId,uomCodes));
    }

    @ApiOperation(value = "通过编码查询单位类型(批量)")
    @Permission(permissionPublic = true , level = ResourceLevel.ORGANIZATION)
    @GetMapping("/uomType-by-codes")
    public ResponseEntity<Map<String, UomTypeBO>> findUomTypeByCodes(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                     @RequestParam(value = "uomTypeCodes",required = false) List<String> uomTypeCodes) {
        return Results.success(lovAdapterService.findUomTypeByCodes(organizationId,uomTypeCodes));
    }
}
