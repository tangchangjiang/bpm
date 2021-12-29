package org.o2.metadata.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.o2.metadata.api.co.OnlineShopCO;
import org.o2.metadata.app.service.OnlineShopService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 *
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


    @ApiOperation(value = "查询运费模版信息")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @GetMapping("/online-shop")
    public ResponseEntity<OnlineShopCO> getOnlineShop(@RequestParam String  onlineShopCode) {
        return Results.success(onlineShopService.getOnlineShop(onlineShopCode));
    }
}
