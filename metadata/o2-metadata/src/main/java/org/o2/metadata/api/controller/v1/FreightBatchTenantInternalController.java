package org.o2.metadata.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.o2.metadata.api.co.FreightInfoCO;
import org.o2.metadata.api.dto.FreightDTO;
import org.o2.metadata.app.service.FreightService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 运费模板
 *
 * @author chao.yang05@hand-china.com 2023-04-18
 */
@RestController("freightBatchTenantInternalController.v1")
@RequestMapping({"v1/freight-internal"})
public class FreightBatchTenantInternalController {

    private final FreightService freightService;

    public FreightBatchTenantInternalController(FreightService freightService) {
        this.freightService = freightService;
    }

    @ApiOperation(value = "查询运费模版信息（多租户）")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @PostMapping("/template-batch-tenant")
    public ResponseEntity<Map<Long, FreightInfoCO>> getFreightTemplateBatchTenant(@RequestBody Map<Long, FreightDTO> freightMap) {
        Map<Long, FreightInfoCO> resultMap = new HashMap<>();
        freightMap.forEach((tenant, freight) -> {
            freight.setTenantId(tenant);
            resultMap.put(tenant, freightService.getFreightTemplate(freight));
        });
        return Results.success(resultMap);
    }

    @ApiOperation(value = "批量查询运费模版信息（多租户）")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @PostMapping("/template-list-batch-tenant")
    public ResponseEntity<Map<Long, Map<String, FreightInfoCO>>> listFreightTemplatesBatchTenant(@RequestBody Map<Long, List<FreightDTO>> freightMap) {
        Map<Long, Map<String, FreightInfoCO>> resultMap = new HashMap<>();
        freightMap.forEach((tenant, freightList) -> {
            freightList.forEach(e -> e.setTenantId(tenant));
            resultMap.put(tenant, freightService.listFreightTemplates(freightList));
        });
        return Results.success(resultMap);
    }
}
