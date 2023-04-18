package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.o2.metadata.console.app.bo.CurrencyBO;
import org.o2.metadata.console.app.bo.UomBO;
import org.o2.metadata.console.app.service.LovAdapterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 值集查询 站点级
 *
 * @author chao.yang05@hand-china.com 2023-04-17
 */
@RestController("lovAdapterInternalSiteController.v1")
@RequestMapping("/v1/lov-internal")
public class LovAdapterInternalSiteController extends BaseController {

    private final LovAdapterService lovAdapterService;

    public LovAdapterInternalSiteController(LovAdapterService lovAdapterService) {
        this.lovAdapterService = lovAdapterService;
    }

    @ApiOperation(value = "通过编码查询货币(批量-多租户)")
    @Permission(permissionPublic = true, level = ResourceLevel.SITE)
    @GetMapping("/currency-by-codes-batch-tenant")
    public ResponseEntity<Map<Long, Map<String, CurrencyBO>>> findCurrencyByCodes(@RequestBody Map<Long, List<String>> currencyCodeMap) {
        Map<Long, Map<String, CurrencyBO>> map = new HashMap<>();
        currencyCodeMap.forEach((tenantId, currencyCodes) -> map.put(tenantId, lovAdapterService.findCurrencyByCodes(tenantId, currencyCodes)));
        return Results.success(map);
    }

    @ApiOperation(value = "通过编码查询单位(批量-多租户)")
    @Permission(permissionWithin = true, level = ResourceLevel.SITE)
    @GetMapping("/uom-by-codes-batch-tenant")
    public ResponseEntity<Map<Long, Map<String, UomBO>>> findUomByCodesBatchTenant(@RequestBody Map<Long, List<String>> uomCodesMap) {
        Map<Long, Map<String, UomBO>> map = new HashMap<>();
        uomCodesMap.forEach((tenantId, uomCodes) -> map.put(tenantId, lovAdapterService.findUomByCodes(tenantId, uomCodes)));
        return Results.success(map);
    }

    @ApiOperation(value = "独立查询值集详细信息(多租户)")
    @Permission(permissionWithin = true, level = ResourceLevel.SITE)
    @GetMapping("/query-lov-value-batch-tenant")
    public ResponseEntity<Map<Long, List<LovValueDTO>>> queryLovValueBatchTenant(@RequestBody Map<Long, String> lovCodeMap) {
        Map<Long, List<LovValueDTO>> map = new HashMap<>();
        lovCodeMap.forEach((tenantId, lovCode) -> map.put(tenantId, lovAdapterService.queryLovValue(tenantId, lovCode)));
        return Results.success(map);
    }

}
