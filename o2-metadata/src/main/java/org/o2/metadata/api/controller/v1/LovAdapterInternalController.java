package org.o2.metadata.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.util.Results;

import org.o2.metadata.app.service.LovAdapterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
}
