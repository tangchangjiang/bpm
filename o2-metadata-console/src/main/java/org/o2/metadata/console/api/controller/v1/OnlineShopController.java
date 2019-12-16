package org.o2.metadata.console.api.controller.v1;

import com.google.common.base.Preconditions;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.console.app.service.OnlineShopRelPosService;
import org.o2.metadata.console.config.EnableMetadataConsole;
import org.o2.metadata.core.domain.entity.Catalog;
import org.o2.metadata.core.domain.entity.OnlineShop;
import org.o2.metadata.core.domain.repository.CatalogRepository;
import org.o2.metadata.core.domain.repository.OnlineShopRepository;
import org.o2.metadata.core.infra.constants.BasicDataConstants;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 网店信息管理 API
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */

@RestController("onlineShopController.v1")
@RequestMapping("/v1/{organizationId}/online-shops")
@Api(tags = EnableMetadataConsole.ONLINE_SHOP)
public class OnlineShopController extends BaseController {
    private final OnlineShopRepository onlineShopRepository;
    private final OnlineShopRelPosService onlineShopRelPosService;
    private final CatalogRepository catalogRepository;
    public OnlineShopController(final OnlineShopRepository onlineShopRepository, final OnlineShopRelPosService onlineShopRelPosService,final CatalogRepository catalogRepository) {
        this.onlineShopRepository = onlineShopRepository;
        this.onlineShopRelPosService = onlineShopRelPosService;
        this.catalogRepository = catalogRepository;
    }

    @ApiOperation(value = "按条件查询网店列表",
            notes = "网店名称/网店编码/平台店铺编码支持模糊查询，是否有效/归属电商平台按相等处理")
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity listOnlineShopsByOptions(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, final OnlineShop condition, @ApiIgnore PageRequest pageRequest) {
        condition.setTenantId(organizationId);
        return Results.success(PageHelper.doPageAndSort(pageRequest, () -> onlineShopRepository.selectByCondition(condition)));
    }

    @ApiOperation("查询所有active的网点列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/all-active")
    public ResponseEntity listAllActiveShops(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId) {
        OnlineShop onlineShop = new OnlineShop();
        onlineShop.setTenantId(organizationId);
        onlineShop.setActiveFlag(1);
        return Results.success(onlineShopRepository.select(onlineShop));
    }

    @ApiOperation("查询所有网点列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/all")
    public ResponseEntity listAllShops(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, final OnlineShop onlineShop) {
        onlineShop.setTenantId(organizationId);
        return Results.success(onlineShopRepository.select(onlineShop));
    }

    @ApiOperation(value = "网店信息明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping("/{shopId}")
    public ResponseEntity detail(@PathVariable final Long shopId) {
        OnlineShop onlineShop = onlineShopRepository.selectByPrimaryKey(shopId);
        Catalog catalog = catalogRepository.selectOne(Catalog.builder().catalogId(onlineShop.getCatalogId()).build());
        onlineShop.setCatalogCode(catalog.getCatalogCode());
        return Results.success(onlineShop);
    }

    @ApiOperation("创建网店信息")
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity createOnlineShop(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @ApiParam("网店信息数据") @RequestBody final OnlineShop onlineShop) {
        // 初始化部分值，否则通不过验证
        Preconditions.checkArgument(null != onlineShop.getCatalogCode(), BasicDataConstants.ErrorCode.BASIC_DATA_CATALOG_CODE_IS_NULL);
        onlineShop.setTenantId(organizationId);
        Catalog catalog = catalogRepository.selectOne(Catalog.builder().catalogCode(onlineShop.getCatalogCode()).tenantId(organizationId).build());
        onlineShop.initDefaultProperties();
        this.validObject(onlineShop);
        if (onlineShop.exist(onlineShopRepository)) {
            return new ResponseEntity<>(getExceptionResponse(BaseConstants.ErrorCode.DATA_EXISTS), HttpStatus.OK);
        }
        try {
            onlineShop.setCatalogId(catalog.getCatalogId());
            return Results.success(this.onlineShopRepository.insertSelective(onlineShop));
        } catch (final DuplicateKeyException e) {
            throw new CommonException(BasicDataConstants.ErrorCode.BASIC_DATA_DUPLICATE_CODE, e,
                    "OnlineShop(" + onlineShop.getOnlineShopId() + ")");
        }
    }

    @ApiOperation("修改网店信息")
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity updateOnlineShop(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody final OnlineShop onlineShop) {
        SecurityTokenHelper.validToken(onlineShop);
        onlineShop.setTenantId(organizationId);
        onlineShop.initDefaultProperties();
        this.validObject(onlineShop);
        onlineShop.validate(onlineShopRepository);
        if (!onlineShop.exist(onlineShopRepository)) {
            return new ResponseEntity<>(getExceptionResponse(BaseConstants.ErrorCode.NOT_FOUND), HttpStatus.OK);
        }
        final int result = onlineShopRepository.updateByPrimaryKeySelective(onlineShop);
        //触发网店关联服务点更新
        onlineShopRelPosService.resetIsInvCalculated(onlineShop.getOnlineShopCode(), null, onlineShop.getTenantId());
        return Results.success(result);
    }

}
