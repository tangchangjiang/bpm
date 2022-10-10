package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.util.Results;
import org.o2.metadata.console.api.co.PageCO;
import org.o2.metadata.console.api.dto.RegionQueryLovInnerDTO;
import org.o2.metadata.console.app.bo.CurrencyBO;
import org.o2.metadata.console.app.bo.UomBO;
import org.o2.metadata.console.app.bo.UomTypeBO;
import org.o2.metadata.console.app.service.LovAdapterService;
import org.o2.metadata.console.infra.entity.Region;
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
@RequestMapping("/v1/{organizationId}/lov-internal")
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
    @Permission(permissionWithin = true , level = ResourceLevel.ORGANIZATION)
    @GetMapping("/uom-by-codes")
    public ResponseEntity<Map<String, UomBO>> findUomByCodes(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                             @RequestParam(value = "uomCodes",required = false) List<String> uomCodes) {
        return Results.success(lovAdapterService.findUomByCodes(organizationId,uomCodes));
    }

    @ApiOperation(value = "通过编码查询单位类型(批量)")
    @Permission(permissionWithin = true , level = ResourceLevel.ORGANIZATION)
    @GetMapping("/uomType-by-codes")
    public ResponseEntity<Map<String, UomTypeBO>> findUomTypeByCodes(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                     @RequestParam(value = "uomTypeCodes",required = false) List<String> uomTypeCodes) {
        return Results.success(lovAdapterService.findUomTypeByCodes(organizationId,uomTypeCodes));
    }

    @ApiOperation(value = "独立查询值集详细信息")
    @Permission(permissionWithin = true , level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-lov-value")
    public ResponseEntity<List<LovValueDTO>> queryLovValue(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                           @RequestParam  String lovCode) {
        return Results.success(lovAdapterService.queryLovValue(organizationId,lovCode));
    }

    @ApiOperation(value = "独立查询值集中指定值的 描述信息（meaning）")
    @Permission(permissionWithin = true , level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-lov-value-meaning")
    public ResponseEntity<String> queryLovValueMeaning(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                       @RequestParam String lovCode,
                                                       @RequestParam (required = false)String lovValue) {
        return Results.success(lovAdapterService.queryLovValueMeaning(organizationId,lovCode,lovValue));
    }

    @ApiOperation(value = " 批量查询指定值集内容(sql&url)")
    @Permission(permissionWithin = true , level = ResourceLevel.ORGANIZATION)
    @GetMapping("/batch-query-lov-value-meaning")
    public ResponseEntity<List<Map<String, Object>>> batchQueryLovValueMeaning(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                       @RequestParam String lovCode,
                                                       @RequestParam (required = false) Map<String, String> queryLovValueMap) {
        return Results.success(lovAdapterService.queryLovValueMeaning(organizationId,lovCode,queryLovValueMap));
    }

    @ApiOperation(value = " 批量查询指定值集内容(sql&url)")
    @Permission(permissionWithin = true , level = ResourceLevel.ORGANIZATION)
    @GetMapping("/batch-page-query-lov-value-meaning")
    public ResponseEntity<List<Map<String, Object>>> batchQueryLovValueMeaningPage(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                               @RequestParam String lovCode,
                                                                               @RequestParam (required = false) Map<String, String> queryLovValueMap,
                                                                                   @RequestParam(required = false) Integer page,
                                                                                   @RequestParam(required = false) Integer size) {
        return Results.success(lovAdapterService.queryLovValueMeaning(organizationId, lovCode, page, size, queryLovValueMap));
    }

    @ApiOperation(value = " 分页查询指定值集内容")
    @Permission(permissionWithin = true , level = ResourceLevel.ORGANIZATION)
    @GetMapping("/page-query-lov-value")
    public ResponseEntity<String> queryLovPage(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                     @RequestParam String lovCode,
                                                     @RequestParam (required = false) Map<String, String> queryParams,
                                                     PageRequest pageRequest) {
        queryParams.put("organizationId", String.valueOf(organizationId));
        return Results.success(lovAdapterService.queryLovPage(queryParams, pageRequest, lovCode,organizationId));
    }

    @ApiOperation(value = "查询地区值")
    @Permission(permissionWithin= true , level = ResourceLevel.ORGANIZATION)
    @PostMapping("/query-region-lov")
    public ResponseEntity<List<Region>> queryRegion(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                    @RequestBody RegionQueryLovInnerDTO innerDTO) {
        return Results.success(lovAdapterService.queryRegion(organizationId,innerDTO));
    }

    @ApiOperation(value = "分页查询地区值")
    @Permission(permissionWithin = true , level = ResourceLevel.ORGANIZATION)
    @PostMapping("/page-query-region-lov")
    public ResponseEntity<PageCO<Region>> queryRegionPage(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                          @RequestBody RegionQueryLovInnerDTO innerDTO,
                                                           PageRequest pageRequest) {
        return Results.success(lovAdapterService.queryRegionPage(organizationId,pageRequest.getPage(),pageRequest.getSize(),innerDTO));
    }
}