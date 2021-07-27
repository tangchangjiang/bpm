package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.console.app.service.OnlineShopInfAuthService;
import org.o2.metadata.console.config.MetadataManagementAutoConfiguration;
import org.o2.metadata.console.infra.entity.OnlineShop;
import org.o2.metadata.console.infra.entity.OnlineShopInfAuth;
import org.o2.metadata.console.infra.repository.OnlineShopInfAuthRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 网店接口表 管理 API
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@RestController("onlineShopInfAuthController.v1")
@RequestMapping("/v1/{organizationId}/online-shop-inf-auths")
@Api(tags = MetadataManagementAutoConfiguration.ONLINE_SHOP_INF_AUTH)
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
    public ResponseEntity<?> list(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, final OnlineShopInfAuth onlineShopInfAuth, @ApiIgnore @SortDefault(value = OnlineShopInfAuth.FIELD_ONLINE_SHOP_INF_AUTH_ID,
            direction = Sort.Direction.DESC) final PageRequest pageRequest) {
        onlineShopInfAuth.setTenantId(organizationId);
        final Page<OnlineShopInfAuth> list = onlineShopInfAuthRepository.pageAndSort(pageRequest, onlineShopInfAuth);
        return Results.success(list);
    }

    @ApiOperation(value = "网店接口表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{onlineShopId}")
    public ResponseEntity<?> detail(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,@PathVariable final Long onlineShopId) {
        OnlineShop query = new OnlineShop();
        query.setTenantId(organizationId);
        query.setOnlineShopId(onlineShopId);
        return Results.success(onlineShopInfAuthRepository.listInfAuthByOnlineShop(query));
    }

    @ApiOperation(value = "创建网店接口表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<?> create(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,@RequestBody final OnlineShopInfAuth onlineShopInfAuth) {
        onlineShopInfAuth.setTenantId(organizationId);
        onlineShopInfAuthRepository.insertSelective(onlineShopInfAuth);
        return Results.success(onlineShopInfAuth);
    }

    @ApiOperation(value = "新建或修改网店接口表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<?> updateOrInsert(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,@RequestBody final OnlineShopInfAuth onlineShopInfAuth) {
        onlineShopInfAuth.setTenantId(organizationId);
        shopInfAuthService.updateOrInsert(onlineShopInfAuth);
        return Results.success(onlineShopInfAuth);
    }

    @ApiOperation(value = "删除网店接口表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,@RequestBody final OnlineShopInfAuth onlineShopInfAuth) {
        SecurityTokenHelper.validToken(onlineShopInfAuth);
        onlineShopInfAuth.setTenantId(organizationId);
        onlineShopInfAuthRepository.deleteByPrimaryKey(onlineShopInfAuth);
        return Results.success();
    }

}
