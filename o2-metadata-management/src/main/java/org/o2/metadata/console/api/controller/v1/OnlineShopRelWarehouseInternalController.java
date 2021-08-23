package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.core.util.Results;
import org.o2.metadata.console.api.co.OnlineShopRelWarehouseCO;
import org.o2.metadata.console.app.service.OnlineShopRelWarehouseService;
import org.o2.metadata.console.config.MetadataManagementAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 网店关联仓库 管理 API
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */

@RestController("onlineShopRelWarehouseInternalController.v1")
@RequestMapping("/v1/{organizationId}/onlineShopRelWarehouse-internal")
@Api(tags = MetadataManagementAutoConfiguration.ONLINE_SHOP_WAREHOUSE_REL)
@Slf4j
public class OnlineShopRelWarehouseInternalController {
    private final OnlineShopRelWarehouseService onlineShopRelWarehouseService;

    public OnlineShopRelWarehouseInternalController(OnlineShopRelWarehouseService onlineShopRelWarehouseService) {
        this.onlineShopRelWarehouseService = onlineShopRelWarehouseService;
    }

    @ApiOperation(value = "查询网店关联有效仓库")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{onlineShopCode}")
    public ResponseEntity<Map<String, OnlineShopRelWarehouseCO>> listOnlineShopRelWarehouses(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                                             @PathVariable(value = "onlineShopCode") @ApiParam(value = "参数code", required = true) String onlineShopCode) {
        List<OnlineShopRelWarehouseCO> systemParameterVOList = onlineShopRelWarehouseService.listOnlineShopRelWarehouses(onlineShopCode, organizationId);
        Map<String, OnlineShopRelWarehouseCO> map = new HashMap<>(4);
        if (CollectionUtils.isEmpty(systemParameterVOList)) {
            Results.success(map);
        }
        for (OnlineShopRelWarehouseCO co : systemParameterVOList) {
            map.put(co.getWarehouseCode(), co);
        }
        return Results.success(map);
    }

}
