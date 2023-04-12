package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.util.Results;
import org.o2.core.helper.JsonHelper;
import org.o2.metadata.console.api.co.OnlineShopCO;
import org.o2.metadata.console.api.dto.OnlineShopQueryInnerDTO;
import org.o2.metadata.console.app.service.OnlineShopService;
import org.o2.metadata.console.infra.config.MetadataManagementAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 网店内部接口 站点级
 *
 * @author chao.yang05@hand-china.com 2023-04-12
 */
@Slf4j
@RestController("onlineShopControllerSiteInternal.v1")
@RequestMapping("/v1/online-shops-internal")
@Api(tags = MetadataManagementAutoConfiguration.ONLINE_SHOP_SITE)
public class OnlineShopInternalSiteController {

    private final OnlineShopService onlineShopService;

    public OnlineShopInternalSiteController(OnlineShopService onlineShopService) {
        this.onlineShopService = onlineShopService;
    }

    @ApiOperation(value = "查询网店（站点级）")
    @Permission(permissionWithin = true, level = ResourceLevel.SITE)
    @PostMapping("/onlineShop-list-site")
    public ResponseEntity<Map<String, OnlineShopCO>> listOnlineShopsOfSite(@RequestBody OnlineShopQueryInnerDTO onlineShopQueryInnerDTO) {
        log.info("onlineShopQueryInnerDTO:{}", JsonHelper.objectToString(onlineShopQueryInnerDTO));
        return Results.success(onlineShopService.listOnlineShops(onlineShopQueryInnerDTO));
    }
}
