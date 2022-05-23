package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.util.Results;

import org.o2.metadata.console.api.co.FreightInfoCO;
import org.o2.metadata.console.api.co.FreightTemplateCO;
import org.o2.metadata.console.api.dto.FreightDTO;
import org.o2.metadata.console.app.service.FreightTemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 *
 * 运费
 *
 * @author yipeng.zhu@hand-china.com 2021-07-14
 **/
@RestController("freightInternalController.v1")
@RequestMapping({"v1/{organizationId}/freight-internal"})
public class FreightInternalController {
    private final FreightTemplateService freightService;

    public FreightInternalController(FreightTemplateService freightService) {
        this.freightService = freightService;
    }

    @ApiOperation(value = "查询模版信息")
    @Permission(permissionPublic = true , level = ResourceLevel.ORGANIZATION)
    @PostMapping("/template")
    public ResponseEntity<FreightInfoCO> getFreightTemplate(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody FreightDTO freight) {
        freight.setTenantId(organizationId);
        return Results.success(freightService.getFreightTemplate(freight));
    }


    @ApiOperation(value = "查询默认模版信息")
    @Permission(permissionPublic = true, level = ResourceLevel.ORGANIZATION)
    @GetMapping("/default")
    public ResponseEntity<FreightTemplateCO> getDefaultTemplate(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId) {
        return Results.success(freightService.getDefaultTemplate(organizationId));
    }

    @ApiOperation(value = "批量查询运费模版信息")
    @Permission(permissionPublic = true , level = ResourceLevel.ORGANIZATION)
    @PostMapping("/templates")
    public ResponseEntity<Map<String, FreightTemplateCO>> listFreightTemplate(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                          @RequestBody List<String> templateCodes) {
        return Results.success(freightService.listFreightTemplate(organizationId, templateCodes));
    }
}
