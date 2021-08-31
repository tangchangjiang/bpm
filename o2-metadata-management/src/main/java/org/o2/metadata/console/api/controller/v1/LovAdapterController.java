package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.ss.formula.functions.T;
import org.hzero.boot.platform.lov.dto.LovValueDTO;

import org.hzero.core.util.Results;
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
@RestController("lovAdapterController.v1")
@RequestMapping("/v1/{organizationId}/lov")
public class LovAdapterController {
    private final LovAdapterService lovAdapterService;

    public LovAdapterController(LovAdapterService lovAdapterService) {
        this.lovAdapterService = lovAdapterService;
    }

    @ApiOperation(value = "通过编码查询货币(批量)")
    @Permission(permissionPublic = true , level = ResourceLevel.ORGANIZATION)
    @GetMapping("/value/batch")
    public ResponseEntity<Map<String, List<LovValueDTO>>> findCurrencyByCodes(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                              @RequestParam Map<String, String> queryMap) {
        return lovAdapterService.batchQueryLovInfo(queryMap,organizationId);
    }

    @ApiOperation("货币")
    @Permission(permissionPublic = true , level = ResourceLevel.ORGANIZATION )
    @GetMapping("/url/currency")
    public ResponseEntity<Page<Object>> listCurrency(@PathVariable Long organizationId,
                                                @RequestParam(required = false) Map<String,String> queryParams,
                                                PageRequest pageRequest) {
        // todo 后续替换组件
        queryParams.put("organizationId",String.valueOf(organizationId));
        queryParams.put("lovCode","O2MD.CURRENCY");
        return Results.success(lovAdapterService.pageList(queryParams, pageRequest,queryParams.get("lovCode")));
    }

    @ApiOperation("单位")
    @Permission(permissionPublic = true , level = ResourceLevel.ORGANIZATION )
    @GetMapping("/url/uom")
    public ResponseEntity<Page<Object>> listUom(@PathVariable Long organizationId,
                                                     @RequestParam(required = false) Map<String,String> queryParams,
                                                     PageRequest pageRequest) {
        // todo 后续替换组件
        queryParams.put("organizationId",String.valueOf(organizationId));
        queryParams.put("lovCode","O2MD.UOM");
        return Results.success(lovAdapterService.pageList(queryParams, pageRequest,queryParams.get("lovCode")));
    }

    @ApiOperation("单位类型")
    @Permission(permissionPublic = true , level = ResourceLevel.ORGANIZATION )
    @GetMapping("/url/uom-type")
    public ResponseEntity<Page<Object>> listUomType(@PathVariable Long organizationId,
                                                @RequestParam(required = false) Map<String,String> queryParams,
                                                PageRequest pageRequest) {
        // todo 后续替换组件
        queryParams.put("organizationId",String.valueOf(organizationId));
        queryParams.put("lovCode","O2MD.UOM_TYPE");
        return Results.success(lovAdapterService.pageList(queryParams, pageRequest,queryParams.get("lovCode")));
    }

}
