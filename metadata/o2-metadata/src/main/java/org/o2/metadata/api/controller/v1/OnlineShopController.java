package org.o2.metadata.api.controller.v1;

import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.o2.metadata.api.vo.OnlineShopVO;
import org.o2.metadata.app.service.OnlineShopService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 网店
 *
 * @author chao.yang05@hand-china.com 2023-04-18
 */
@RestController("onlineShopController.v1")
@RequestMapping({"/v1"})
public class OnlineShopController {

    private final OnlineShopService onlineShopService;

    public OnlineShopController(OnlineShopService onlineShopService) {
        this.onlineShopService = onlineShopService;
    }

    /**
     * 通过编码查询网店信息
     *
     * @param onlineShopCode 网店编码
     * @return 网店信息
     */
    @ApiOperation("查询网店信息")
    @Permission(permissionPublic = true)
    @GetMapping("/pub/onlineShop/{onlineShopCode}")
    public ResponseEntity<OnlineShopVO> onlineShopInfo(@PathVariable String onlineShopCode) {
//        onlineShopService.getOnlineShop();
        return Results.success(null);
    }
}
