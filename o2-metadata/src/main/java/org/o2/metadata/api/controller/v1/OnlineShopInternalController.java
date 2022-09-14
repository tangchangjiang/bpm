package org.o2.metadata.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.o2.core.filter.metric.UserInfo;
import org.o2.core.helper.UserHelper;
import org.o2.metadata.api.co.OnlineShopCO;
import org.o2.metadata.app.service.OnlineShopService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 运费
 *
 * @author yipeng.zhu@hand-china.com 2021-07-14
 **/
@RestController("onlineShopInternalController.v1")
@RequestMapping({"v1/onlineShop-internal"})
public class OnlineShopInternalController {
    private final OnlineShopService onlineShopService;

    public OnlineShopInternalController(OnlineShopService onlineShopService) {
        this.onlineShopService = onlineShopService;
    }


    @ApiOperation(value = "查询单个网店")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @GetMapping("/online-shop")
    public ResponseEntity<OnlineShopCO> getOnlineShop(@RequestParam String onlineShopCode, @RequestParam String tenantId) {
        return Results.success(onlineShopService.getOnlineShop(onlineShopCode, Long.valueOf(tenantId)));
    }

    @ApiOperation(value = "查询多个网店")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @PostMapping("/online-shop/list")
    public ResponseEntity<List<OnlineShopCO>> queryOnlineShop(@RequestBody List<String> onlineShopCodes) {
        UserHelper.validUserInfo(UserHelper.getUserInfo(), UserInfo.FIELD_TENANT_ID);
        return Results.success(onlineShopService.queryShopList(onlineShopCodes));
    }

    @ApiOperation(value = "查询多个网店-根据网店类型")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @GetMapping("/online-shop/list-by-type")
    public ResponseEntity<List<OnlineShopCO>> queryOnlineShopByType(@RequestParam String onlineShopType){
        UserHelper.validUserInfo(UserHelper.getUserInfo(), UserInfo.FIELD_TENANT_ID);
        return Results.success(onlineShopService.queryShopListByType(UserHelper.getTenantId(), onlineShopType));
    }

}
