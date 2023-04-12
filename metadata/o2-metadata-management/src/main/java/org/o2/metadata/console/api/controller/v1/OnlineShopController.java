package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.domain.Page;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.console.app.service.OnlineShopService;
import org.o2.metadata.console.infra.config.MetadataManagementAutoConfiguration;
import org.o2.metadata.console.infra.entity.OnlineShop;
import org.o2.metadata.console.infra.repository.OnlineShopRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 网店信息管理 API
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Slf4j
@RestController("onlineShopController.v1")
@RequestMapping("/v1/{organizationId}/online-shops")
@Api(tags = MetadataManagementAutoConfiguration.ONLINE_SHOP)
public class OnlineShopController extends BaseController {

    private final OnlineShopRepository onlineShopRepository;
    private final OnlineShopService onlineShopService;

    public OnlineShopController(OnlineShopRepository onlineShopRepository, OnlineShopService onlineShopService) {
        this.onlineShopRepository = onlineShopRepository;
        this.onlineShopService = onlineShopService;
    }

    @ApiOperation(value = "按条件查询网店列表",
            notes = "网店名称/网店编码/平台店铺编码支持模糊查询，是否有效/归属电商平台按相等处理")
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<OnlineShop>> listOnlineShopsByOptions(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                     final OnlineShop condition, @ApiIgnore PageRequest pageRequest) {
        condition.setTenantId(organizationId);
        return Results.success(PageHelper.doPageAndSort(pageRequest, () -> onlineShopService.selectByCondition(condition)));
    }

    @ApiOperation("查询所有active的网点列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/all-active")
    public ResponseEntity<Page<OnlineShop>> listAllActiveShops(OnlineShop onlineShop,
                                                               @PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                               @ApiIgnore PageRequest pageRequest) {
        onlineShop.setTenantId(organizationId);
        onlineShop.setActiveFlag(1);
        // 租户层查询
        onlineShop.setSiteFlag(BaseConstants.Flag.NO);
        return Results.success(PageHelper.doPageAndSort(pageRequest, () -> onlineShopRepository.selectShop(onlineShop)));
    }

    @ApiOperation("查询所有网点列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/all")
    public ResponseEntity<Page<OnlineShop>> listAllShops(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                         final OnlineShop onlineShop, @ApiIgnore PageRequest pageRequest) {
        onlineShop.setTenantId(organizationId);
        // 租户层查询
        onlineShop.setSiteFlag(BaseConstants.Flag.NO);
        return Results.success(PageHelper.doPageAndSort(pageRequest, () -> onlineShopRepository.selectShop(onlineShop)));
    }

    @ApiOperation(value = "网店信息明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping("/{shopId}")
    public ResponseEntity<OnlineShop> detail(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                             @PathVariable final Long shopId) {
        OnlineShop onlineShop = new OnlineShop();
        onlineShop.setTenantId(organizationId);
        onlineShop.setOnlineShopId(shopId);
        return Results.success(onlineShopRepository.selectById(onlineShop));
    }

    @ApiOperation("创建网店信息")
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<OnlineShop> createOnlineShop(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                       @ApiParam("网店信息数据") @RequestBody final OnlineShop onlineShop) {
        // 初始化部分值，否则通不过验证
        onlineShop.setTenantId(organizationId);
        onlineShop.initDefaultProperties();
        this.validObject(onlineShop);
        OnlineShop onlineShopReturn = onlineShopService.createOnlineShop(onlineShop);
        return Results.success(onlineShopReturn);
    }

    @ApiOperation("修改网店信息")
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<OnlineShop> updateOnlineShop(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                       @RequestBody final OnlineShop onlineShop) {
        SecurityTokenHelper.validToken(onlineShop);
        onlineShop.setTenantId(organizationId);
        onlineShop.initDefaultProperties();
        this.validObject(onlineShop);
        OnlineShop onlineShopReturn = onlineShopService.updateOnlineShop(onlineShop);
        return Results.success(onlineShopReturn);
    }

}
