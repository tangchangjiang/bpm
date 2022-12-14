package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.util.Results;
import org.o2.metadata.console.api.co.OnlineShopCO;
import org.o2.metadata.console.api.dto.OnlineShopCatalogVersionDTO;
import org.o2.metadata.console.api.dto.OnlineShopQueryInnerDTO;
import org.o2.metadata.console.app.service.OnlineShopService;
import org.o2.metadata.console.infra.config.MetadataManagementAutoConfiguration;
import org.o2.metadata.management.client.domain.dto.OnlineShopDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * description
 *
 * @author zhilin.ren@hand-china.com 2021/08/05 15:40
 */
@Slf4j
@RestController("onlineShopControllerInternal.v1")
@RequestMapping("/v1/{organizationId}/online-shops-internal")
@Api(tags = MetadataManagementAutoConfiguration.ONLINE_SHOP)
public class OnlineShopInternalController {

    private final OnlineShopService onlineShopService;

    public OnlineShopInternalController(OnlineShopService onlineShopService) {
        this.onlineShopService = onlineShopService;
    }

    @ApiOperation(value = "查询网店")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @PostMapping("/onlineShop-list")
    public ResponseEntity<Map<String, OnlineShopCO>> listOnlineShops(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required =
            true) Long organizationId,
                                                                     @RequestBody OnlineShopQueryInnerDTO onlineShopQueryInnerDTO) {
        return Results.success(onlineShopService.listOnlineShops(onlineShopQueryInnerDTO, organizationId));
    }

    @ApiOperation(value = "目录+目录版本批量查询网店")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @PostMapping("/onlineShops")
    public ResponseEntity<Map<String, List<OnlineShopCO>>> listOnlineShopList(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID",
            required = true) Long organizationId,
                                                                              @RequestBody List<OnlineShopCatalogVersionDTO> onlineShopDTO) {
        return Results.success(onlineShopService.listOnlineShops(onlineShopDTO, organizationId));
    }

    @ApiOperation(value = "保存网店")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @PostMapping("/onlineShop-save")
    public ResponseEntity<OnlineShopCO> saveOnlineShop(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                       @RequestBody OnlineShopDTO onlineShopDTO) {
        onlineShopDTO.setTenantId(organizationId);
        return Results.success(onlineShopService.saveOnlineShop(onlineShopDTO));
    }

    @ApiOperation(value = "批量更新网店状态")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @PostMapping("/batch-update-status")
    public ResponseEntity<List<OnlineShopCO>> batchUpdateShopStatus(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                       @RequestBody List<OnlineShopDTO> onlineShopDTOList) {
        for (OnlineShopDTO onlineShopDTO : onlineShopDTOList) {
            onlineShopDTO.setTenantId(organizationId);
        }
        return Results.success(onlineShopService.batchUpdateShopStatus(onlineShopDTOList));
    }
}
