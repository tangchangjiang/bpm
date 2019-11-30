package org.o2.metadata.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.app.service.OnlineShopInfAuthService;
import org.o2.metadata.config.MetadataSwagger;
import org.o2.metadata.domain.entity.OnlineShopInfAuth;
import org.o2.metadata.domain.repository.OnlineShopInfAuthRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 网店接口表 管理 API
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@RestController("onlineShopInfAuthController.v1")
@RequestMapping("/v1/online-shop-inf-auths")
@Api(tags = MetadataSwagger.ONLINE_SHOP_INF_AUTH)
public class OnlineShopInfAuthController extends BaseController {

    private final OnlineShopInfAuthRepository onlineShopInfAuthRepository;
    private final OnlineShopInfAuthService shopInfAuthService;

    public OnlineShopInfAuthController(final OnlineShopInfAuthRepository onlineShopInfAuthRepository,
                                       final OnlineShopInfAuthService shopInfAuthService) {
        this.onlineShopInfAuthRepository = onlineShopInfAuthRepository;
        this.shopInfAuthService = shopInfAuthService;
    }

    @ApiOperation(value = "网店接口表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<?> list(final OnlineShopInfAuth onlineShopInfAuth, @ApiIgnore @SortDefault(value = OnlineShopInfAuth.FIELD_ONLINE_SHOP_INF_AUTH_ID,
            direction = Sort.Direction.DESC) final PageRequest pageRequest) {
        final Page<OnlineShopInfAuth> list = onlineShopInfAuthRepository.pageAndSort(pageRequest, onlineShopInfAuth);
        return Results.success(list);
    }

    @ApiOperation(value = "网店接口表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{onlineShopInfAuthId}")
    public ResponseEntity<?> detail(@PathVariable final Long onlineShopInfAuthId) {
        return Results.success(onlineShopInfAuthRepository.listOnlineShopInfAuthByOption(onlineShopInfAuthId));
    }

    @ApiOperation(value = "创建网店接口表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<?> create(@RequestBody final OnlineShopInfAuth onlineShopInfAuth) {
        onlineShopInfAuthRepository.insertSelective(onlineShopInfAuth);
        return Results.success(onlineShopInfAuth);
    }

    @ApiOperation(value = "新建或修改网店接口表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<?> updateOrInsert(@RequestBody final OnlineShopInfAuth onlineShopInfAuth) {
        shopInfAuthService.updateOrInsert(onlineShopInfAuth);
        return Results.success(onlineShopInfAuth);
    }

    @ApiOperation(value = "删除网店接口表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody final OnlineShopInfAuth onlineShopInfAuth) {
        SecurityTokenHelper.validToken(onlineShopInfAuth);
        onlineShopInfAuthRepository.deleteByPrimaryKey(onlineShopInfAuth);
        return Results.success();
    }

}
