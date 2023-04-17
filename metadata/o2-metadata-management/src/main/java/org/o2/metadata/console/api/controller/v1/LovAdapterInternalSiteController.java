package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.MapUtils;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.o2.metadata.console.app.bo.CurrencyBO;
import org.o2.metadata.console.app.service.LovAdapterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @ApiOperation(value = "通过编码查询货币(批量)")
    @Permission(permissionPublic = true, level = ResourceLevel.SITE)
    @GetMapping("/currency-by-codes-batch-tenant")
    public ResponseEntity<Map<Long, Map<String, CurrencyBO>>> findCurrencyByCodes(@RequestParam Map<Long, List<String>> currencyCodeMap) {
        Map<Long, Map<String, CurrencyBO>> map = new HashMap<>();
        currencyCodeMap.forEach((tenantId, currencyCodes) -> {
            Map<String, CurrencyBO> currencyMap = lovAdapterService.findCurrencyByCodes(tenantId, currencyCodes);
            if (MapUtils.isNotEmpty(currencyMap)) {
                map.put(tenantId, currencyMap);
            }
        });
        return Results.success(map);
    }
}
