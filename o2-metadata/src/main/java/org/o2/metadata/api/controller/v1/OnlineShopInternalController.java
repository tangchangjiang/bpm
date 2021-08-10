package org.o2.metadata.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.o2.metadata.api.vo.OnlineShopVO;
import org.o2.metadata.app.service.OnlineShopService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


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
    @Permission(permissionPublic = true, level = ResourceLevel.ORGANIZATION)
    @PostMapping("/online-shop")
    public ResponseEntity<OnlineShopVO> getOnlineShop(@RequestParam String  onlineShopCode) {
        return Results.success(onlineShopService.getOnlineShop(onlineShopCode));
    }
}
