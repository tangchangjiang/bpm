package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.util.Results;
import org.o2.metadata.console.api.vo.OnlineShopVO;
import org.o2.metadata.console.app.service.OnlineShopService;
import org.o2.metadata.console.config.MetadataManagementAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @ApiOperation(value = "内部接口,根据网店名称查询网店code")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<List<OnlineShopVO>> getOnlineShopCode(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                               @RequestParam("platformCode") String platformCode,
                                                               @RequestParam("shopName") String shopName){

        List<OnlineShopVO> onlineShopCode = onlineShopService.getOnlineShopCode(platformCode, shopName);
        return Results.success(onlineShopCode);


    }


}
