package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.CustomPageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.console.api.dto.WarehouseAddrQueryDTO;
import org.o2.metadata.console.api.dto.WarehouseRelCarrierQueryDTO;
import org.o2.metadata.console.api.dto.WarehouseRelPosDTO;
import org.o2.metadata.console.api.vo.WarehouseRelPosVO;
import org.o2.metadata.console.app.service.WarehouseService;
import org.o2.metadata.console.infra.config.MetadataManagementAutoConfiguration;
import org.o2.metadata.console.infra.entity.Carrier;
import org.o2.metadata.console.infra.entity.Pos;
import org.o2.metadata.console.infra.entity.Warehouse;
import org.o2.metadata.console.infra.repository.PosRepository;
import org.o2.metadata.console.infra.repository.WarehouseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * @author NieYong
 * @Title WarehouseController
 * @Description
 * @date 2020/3/4 10:26
 **/

@RestController("warehouseController.v1")
@RequestMapping("/v1/{organizationId}/warehouse")
@Api(tags = MetadataManagementAutoConfiguration.WAREHOUSE)
public class WarehouseController extends BaseController {

    private final WarehouseService warehouseService;
    private final WarehouseRepository warehouseRepository;
    private final PosRepository posRepository;

    public WarehouseController(WarehouseService warehouseService,
                               WarehouseRepository warehouseRepository,
                               PosRepository posRepository) {
        this.warehouseService = warehouseService;
        this.warehouseRepository = warehouseRepository;
        this.posRepository = posRepository;
    }

    @ApiOperation(value = "仓库信息列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping
    @CustomPageRequest
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = Warehouse.class, responseContainer = "List"))
    public ResponseEntity<Page<Warehouse>> list(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                  final Warehouse warehouse,
                                  @ApiIgnore final PageRequest pageRequest) {
        warehouse.setTenantId(organizationId);
        final Page<Warehouse> posList = PageHelper.doPage(pageRequest,
                () -> warehouseRepository.listWarehouseByCondition(warehouse));
        return Results.success(posList);
    }

    @ApiOperation(value = "创建仓库")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<List<Warehouse>> create(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                    @RequestBody final List<Warehouse> warehouses) {
        warehouses.forEach(w -> {
            w.setTenantId(organizationId);
            w.setActiveFlag(1);
            this.validObject(w);
        });
        List<Warehouse> batch = warehouseService.createBatch(organizationId, warehouses);
        return Results.success(batch);
    }

    @ApiOperation(value = "修改仓库信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<List<Warehouse>> update(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                    @RequestBody final List<Warehouse> warehouses) {
        SecurityTokenHelper.validToken(warehouses);
        warehouses.forEach(w -> {
            w.setTenantId(organizationId);
            w.setActiveFlag(1);
        });
        // 更新前数据
        List<Warehouse> oldWarehouse =  getOldWarehouse(warehouses);
        // 更后数据
        List<Warehouse> newList = warehouseService.updateBatch(organizationId, warehouses);
        warehouseService.triggerWhStockCalWithWh(organizationId,newList, oldWarehouse);
        return Results.success(newList);
    }
    /**
     *  更新前数据
     * @param warehouses 仓库
     * @return  list
     */
    private List<Warehouse> getOldWarehouse(List<Warehouse> warehouses) {
        List<Long> ids = new ArrayList<>();
        for (Warehouse warehouse : warehouses) {
            ids.add(warehouse.getWarehouseId());
        }
        if (CollectionUtils.isEmpty(ids)) {
            return  new ArrayList<>();
        }
        // 更新前数据
        return warehouseRepository.selectByIds(StringUtils.join(ids,BaseConstants.Symbol.COMMA));
    }

    @ApiOperation(value = "服务点查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping("/pos")
    public ResponseEntity<Page<Pos>> listPos(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                     final Pos pos,
                                     @ApiIgnore final PageRequest pageRequest) {
        pos.setTenantId(organizationId);
        final Page<Pos> posList = PageHelper.doPageAndSort(pageRequest, () -> posRepository.listPosByCondition(pos));
        return Results.success(posList);
    }

    @ApiOperation(value = "仓库关联承运商")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/carriers")
    public ResponseEntity<Page<Carrier>> listCarriers(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                      final WarehouseRelCarrierQueryDTO queryDTO,
                                                      final PageRequest pageRequest) {
        queryDTO.setTenantId(organizationId);
        final Page<Carrier> carrier = PageHelper.doPageAndSort(pageRequest, () -> warehouseService.listCarriers(queryDTO));
        return Results.success(carrier);
    }

    @ApiOperation(value = "仓库地址")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/address")
    public ResponseEntity<Page<Warehouse>> listWarehouseAddr(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                      final WarehouseAddrQueryDTO queryDTO,
                                                      final PageRequest pageRequest) {
        queryDTO.setTenantId(organizationId);
        final Page<Warehouse> warehouses = PageHelper.doPageAndSort(pageRequest, () -> warehouseService.listWarehouseAddr(queryDTO));
        return Results.success(warehouses);
    }

    /**
     * 仓库关联服务点
     * @param organizationId 租户ID
     * @param warehouseRelPosDTO 请求参数
     * @param pageRequest 分页参数
     * @return WarehouseRelPosVO
     */
    @ApiOperation(value = "仓库关联服务点")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/rel-pos")
    public ResponseEntity<Page<WarehouseRelPosVO>> listWarehouseRelPos(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                       WarehouseRelPosDTO warehouseRelPosDTO,
                                                                       final PageRequest pageRequest) {

        warehouseRelPosDTO.setTenantId(organizationId);
        final Page<WarehouseRelPosVO> warehouseRelPosVOS = PageHelper.doPageAndSort(pageRequest,() -> warehouseRepository.listWarehouseRelPos(warehouseRelPosDTO));
        return Results.success(warehouseRelPosVOS);
    }
}
