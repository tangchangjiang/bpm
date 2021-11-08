package org.o2.metadata.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.util.Results;

import org.o2.metadata.api.co.CurrencyCO;
import org.o2.metadata.api.dto.RegionQueryLovInnerDTO;
import org.o2.metadata.app.bo.UomBO;
import org.o2.metadata.app.service.LovAdapterService;
import org.o2.metadata.infra.entity.Region;
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
    @ApiOperation(value = "查询值集中指定值的 描述信息（meaning）")
    @Permission(permissionWithin = true , level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-lov-value-meaning")
    public ResponseEntity<String> queryLovValueMeaning(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                       @RequestParam String lovCode,
                                                       @RequestParam (required = false)String lovValue) {
        return Results.success(lovAdapterService.queryLovValueMeaning(organizationId,lovCode,lovValue));
    }
    @ApiOperation(value = "通过编码查询货币(批量)")
    @Permission(permissionPublic = true , level = ResourceLevel.ORGANIZATION)
    @GetMapping("/currency-by-codes")
    public ResponseEntity<Map<String, CurrencyCO>> findCurrencyByCodes(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                       @RequestParam(value = "currencyCodes" ,required = false) List<String> currencyCodes) {
        return Results.success(lovAdapterService.findCurrencyByCodes(organizationId,currencyCodes));
    }
    @ApiOperation(value = "通过编码查询单位(批量)")
    @Permission(permissionWithin = true , level = ResourceLevel.ORGANIZATION)
    @GetMapping("/uom-by-codes")
    public ResponseEntity<Map<String, UomBO>> findUomByCodes(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                             @RequestParam(value = "uomCodes",required = false) List<String> uomCodes) {
        return Results.success(lovAdapterService.findUomByCodes(organizationId,uomCodes));
    }
    @ApiOperation(value = "查询地区值")
    @Permission(permissionWithin= true , level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-region-lov")
    public ResponseEntity<List<Region>> queryRegion(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                    RegionQueryLovInnerDTO innerDTO) {
        return Results.success(lovAdapterService.queryRegion(organizationId,innerDTO));
    }
}
