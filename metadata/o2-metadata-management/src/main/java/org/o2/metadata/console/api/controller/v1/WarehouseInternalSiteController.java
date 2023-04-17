package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.util.Results;
import org.o2.metadata.console.api.co.WarehouseCO;
import org.o2.metadata.console.api.dto.WarehouseQueryInnerDTO;
import org.o2.metadata.console.app.service.WarehouseService;
import org.o2.metadata.console.infra.config.MetadataManagementAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 仓库内部接口 站点级
 *
 * @author chao.yang05@hand-china.com 2023-04-17
 */
@RestController("warehouseInternalSiteController.v1")
@RequestMapping("v1/warehouse-internal")
@Api(tags = MetadataManagementAutoConfiguration.WAREHOUSE_SITE)
public class WarehouseInternalSiteController {

    private final WarehouseService warehouseService;

    public WarehouseInternalSiteController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @ApiOperation(value = "多租户查询仓库(站点级)")
    @Permission(permissionWithin = true, level = ResourceLevel.SITE)
    @PostMapping("/list-batch-tenant")
    public ResponseEntity<Map<Long, Map<String, WarehouseCO>>> listWarehousesBatchTenant(@RequestParam Map<Long, WarehouseQueryInnerDTO> innerDTOMap) {
        Map<Long, Map<String, WarehouseCO>> map = new HashMap<>(16);
        List<WarehouseCO> cos = new ArrayList<>();
        innerDTOMap.forEach((tenantId, innerDTO) -> cos.addAll(warehouseService.listWarehouses(innerDTO, tenantId)));
        if (cos.isEmpty()) {
            return Results.success(map);
        }
        //寻源查询出多个网店关联仓库，需要特殊处理
        Map<Long, List<WarehouseCO>> warehouseGroup = cos.stream().collect(Collectors.groupingBy(WarehouseCO::getTenantId));
        warehouseGroup.forEach((tenantId, warehouses) -> {
            Map<String, WarehouseCO> warehouseMap = new HashMap<>();
            WarehouseQueryInnerDTO innerDTO = innerDTOMap.get(tenantId);
            if (Boolean.TRUE.equals(innerDTO.getSourcingFlag())) {
                for (WarehouseCO co : warehouses) {
                    warehouseMap.put(co.getWarehouseCode() + BaseConstants.Symbol.COLON + co.getOnlineShopCode(), co);
                }
            } else {
                for (WarehouseCO co : warehouses) {
                    warehouseMap.put(co.getWarehouseCode(), co);
                }
            }
            map.put(tenantId, warehouseMap);
        });
        return Results.success(map);
    }

}
