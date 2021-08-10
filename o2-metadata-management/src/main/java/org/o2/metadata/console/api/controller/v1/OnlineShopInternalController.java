package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.util.Results;
import org.o2.metadata.console.api.dto.OnlineShopCatalogVersionDTO;
import org.o2.metadata.console.api.dto.OnlineShopDTO;
import org.o2.metadata.console.api.vo.OnlineShopVO;
import org.o2.metadata.console.app.service.OnlineShopService;
import org.o2.metadata.console.config.MetadataManagementAutoConfiguration;
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
    @Permission(permissionPublic = true, level = ResourceLevel.ORGANIZATION)
    @PostMapping("/onlineShop-list")
    public ResponseEntity<Map<String, OnlineShopVO>> listOnlineShops(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                     @RequestBody OnlineShopDTO onlineShopDTO) {
        return Results.success(onlineShopService.listOnlineShops(onlineShopDTO, organizationId));
    }

    @ApiOperation(value = "目录+目录版本批量查询网店")
    @Permission(permissionPublic = true, level = ResourceLevel.ORGANIZATION)
    @PostMapping("/onlineShops")
    public ResponseEntity<Map<String, List<OnlineShopVO>>> listOnlineShopList(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                     @RequestBody List<OnlineShopCatalogVersionDTO> onlineShopDTO) {
        return Results.success(onlineShopService.listOnlineShops(onlineShopDTO, organizationId));
    }
}
