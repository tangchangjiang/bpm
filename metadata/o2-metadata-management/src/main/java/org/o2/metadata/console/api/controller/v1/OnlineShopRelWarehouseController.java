package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.domain.Page;
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
import org.o2.metadata.console.api.dto.OnlineShopRelWarehouseDTO;
import org.o2.metadata.console.api.vo.OnlineShopRelWarehouseVO;
import org.o2.metadata.console.app.service.OnlineShopRelWarehouseService;
import org.o2.metadata.console.infra.config.MetadataManagementAutoConfiguration;
import org.o2.metadata.console.infra.entity.OnlineShopRelWarehouse;
import org.o2.metadata.console.infra.entity.Warehouse;
import org.o2.metadata.console.infra.repository.WarehouseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 网店关联仓库 管理 API
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */

@RestController("onlineShopRelWarehouseController.v1")
@RequestMapping("/v1/{organizationId}")
@Api(tags = MetadataManagementAutoConfiguration.ONLINE_SHOP_WAREHOUSE_REL)
public class OnlineShopRelWarehouseController extends BaseController {

    private final OnlineShopRelWarehouseService onlineShopRelWarehouseService;
    private final WarehouseRepository warehouseRepository;

    public OnlineShopRelWarehouseController(final OnlineShopRelWarehouseService onlineShopRelWarehouseService,
                                            final WarehouseRepository warehouseRepository) {
        this.onlineShopRelWarehouseService = onlineShopRelWarehouseService;
        this.warehouseRepository = warehouseRepository;
    }

    @ApiOperation(value = "批量创建网店关联仓库关系")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/online-shop-rel-warehouse/batch-create")
    public ResponseEntity<List<OnlineShopRelWarehouse>> create(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody final List<OnlineShopRelWarehouse> onlineShopRelWarehouseList) {
        this.validList(onlineShopRelWarehouseList);
        final List<OnlineShopRelWarehouse> relationShips = onlineShopRelWarehouseService.batchInsertSelective(organizationId,onlineShopRelWarehouseList);
        return Results.success(relationShips);
    }

    @ApiOperation("批量更新网点关联仓库状态")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/online-shop-rel-warehouse/batch-update")
    public ResponseEntity<List<OnlineShopRelWarehouse>>  batchUpdateActived(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,@RequestBody final List<OnlineShopRelWarehouse> onlineShopRelWarehouseList) {
        this.validList(onlineShopRelWarehouseList);
        SecurityTokenHelper.validToken(onlineShopRelWarehouseList);
        final List<OnlineShopRelWarehouse> relationships = onlineShopRelWarehouseService.batchUpdateByPrimaryKey(organizationId,onlineShopRelWarehouseList);
        return Results.success(relationships);
    }

    @ApiOperation(("查询未与指定网点绑定的仓库"))
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping("/online-shops/{onlineShopId}/unbind-warehouse")
    public ResponseEntity<Page<Warehouse>>  queryUnbindWarehouses(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                           @PathVariable("onlineShopId") final Long onlineShopId,
                                           @RequestParam(required = false) final String warehouseCode,
                                           @RequestParam(required = false) final String warehouseName,
                                           @ApiIgnore final PageRequest pageRequest) {
        final Page<Warehouse> posList = PageHelper.doPage(pageRequest.getPage(), pageRequest.getSize(),
                () -> warehouseRepository.listUnbindWarehouseList(onlineShopId, warehouseCode, warehouseName,organizationId));
        return Results.success(posList);
    }

    @ApiOperation(value = "查询与指定网店关联的仓库列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping("/online-shops/{onlineShopId}/shop-warehouse-relationships")
    public ResponseEntity<Page<OnlineShopRelWarehouseVO>>  list(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                               @PathVariable("onlineShopId") final Long onlineShopId,
                               final OnlineShopRelWarehouseDTO onlineShopRelWarehouseDTO,
                               @ApiIgnore final PageRequest pageRequest) {
        onlineShopRelWarehouseDTO.setTenantId(organizationId);
        final Page<OnlineShopRelWarehouseVO> list = PageHelper.doPage(pageRequest.getPage(), pageRequest.getSize(),
                () -> onlineShopRelWarehouseService.listShopPosRelsByOption(onlineShopId, onlineShopRelWarehouseDTO));
        return Results.success(list);
    }

    @ApiOperation(value = "通过网店编码查询与指定网店关联的仓库列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping("/online-shops/shop-warehouse-relationships")
    public ResponseEntity<Page<OnlineShopRelWarehouseVO>>  listWarehouse(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                final OnlineShopRelWarehouseDTO onlineShopRelWarehouseDTO,
                                                                @ApiIgnore final PageRequest pageRequest) {
        onlineShopRelWarehouseDTO.setTenantId(organizationId);
        final Page<OnlineShopRelWarehouseVO> list = PageHelper.doPage(pageRequest.getPage(), pageRequest.getSize(),
                () -> onlineShopRelWarehouseService.listShopRelWarehouse(onlineShopRelWarehouseDTO));
        return Results.success(list);
    }
}
