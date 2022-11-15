package org.o2.metadata.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.util.Results;
import org.o2.metadata.api.dto.FreightDTO;
import org.o2.metadata.api.co.FreightInfoCO;
import org.o2.metadata.app.service.FreightService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@Slf4j
public class FreightInternalController {
    private final FreightService freightService;

    public FreightInternalController(FreightService freightService) {
        this.freightService = freightService;
    }

    @ApiOperation(value = "查询运费模版信息")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @PostMapping("/template")
    public ResponseEntity<FreightInfoCO> getFreightTemplate(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                            @RequestBody FreightDTO freight) {
        freight.setTenantId(organizationId);
        return Results.success(freightService.getFreightTemplate(freight));
    }

    @ApiOperation(value = "批量查询运费模版信息")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @PostMapping("/template-list")
    public ResponseEntity<Map<String, FreightInfoCO>> listFreightTemplates(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                          @RequestBody List<FreightDTO> freightList) {
        freightList.forEach(e -> e.setTenantId(organizationId));
        return Results.success(freightService.listFreightTemplates(freightList));
    }
}
