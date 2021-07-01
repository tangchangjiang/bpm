package org.o2.metadata.console.api.controller.v1;

import io.choerodon.swagger.annotation.CustomPageRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import org.o2.metadata.console.app.service.RegionService;
import org.o2.metadata.console.config.EnableMetadataConsole;
import org.o2.metadata.console.domain.entity.Pos;
import org.o2.metadata.console.domain.entity.Region;
import org.o2.metadata.console.domain.entity.RegionRelPos;
import org.o2.metadata.console.domain.repository.RegionRelPosRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.hzero.mybatis.helper.SecurityTokenHelper;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 默认服务点配置 管理 API
 *
 * @author wei.cai@hand-china.com 2020-01-09 15:41:36
 */
@RestController("o2mdRegionRelPosController.v1")
@RequestMapping("/v1/{organizationId}/region-rel-pos")
@Api(tags = EnableMetadataConsole.REGION_REL_POS)
public class RegionRelPosController extends BaseController {

    private final RegionRelPosRepository regionRelPosRepository;
    private final RegionService regionService;

    public RegionRelPosController(RegionRelPosRepository regionRelPosRepository, RegionService regionService) {
        this.regionRelPosRepository = regionRelPosRepository;
        this.regionService = regionService;
    }

    @ApiOperation(value = "通过网店id获取默认服务点配置列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @CustomPageRequest
    @GetMapping("/{onlineStoreId}/regions")
    public ResponseEntity<Page<RegionRelPos>> listByStoreId(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                            @PathVariable @ApiParam(value = "网店ID", required = true) Long onlineStoreId,
                                                            @ApiIgnore @SortDefault(value = RegionRelPos.FIELD_REGION_REL_POS_ID, direction = Sort.Direction.DESC) PageRequest pageRequest) {
        final RegionRelPos regionRelPos = new RegionRelPos();
        regionRelPos.setOnlineShopId(onlineStoreId);
        regionRelPos.setTenantId(organizationId);
        return Results.success(regionRelPosRepository.listByCondition(pageRequest, regionRelPos));
    }

    @ApiOperation(value = "通过网店id获取未关联地区树")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{onlineStoreId}/unbind-regions")
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    public ResponseEntity<List<Region>> listUnbindByStoreId(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                            @PathVariable @ApiParam(value = "网店ID", required = true) Long onlineStoreId) {
        return Results.success(regionService.treeOnlineStoreUnbindRegion(organizationId, onlineStoreId));
    }

    @ApiOperation(value = "通过网店id以及地区id获取默认服务点配置列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @CustomPageRequest
    @GetMapping("/{onlineStoreId}/regions/{regionId}/pos")
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    public ResponseEntity<Page<RegionRelPos>> listByRegionId(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                   @PathVariable @ApiParam(value = "网店ID", required = true) Long onlineStoreId,
                                                   @PathVariable @ApiParam(value = "地区ID", required = true) Long regionId,
                                                   @ApiIgnore @SortDefault(value = RegionRelPos.FIELD_REGION_REL_POS_ID, direction = Sort.Direction.DESC) PageRequest pageRequest) {
        final RegionRelPos regionRelPos = new RegionRelPos();
        regionRelPos.setOnlineShopId(onlineStoreId);
        regionRelPos.setRegionId(regionId);
        regionRelPos.setTenantId(organizationId);
        return Results.success(regionRelPosRepository.listByCondition(pageRequest, regionRelPos));
    }

    @ApiOperation(value = "通过网店id以及地区id获取未关联服务点配置列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @CustomPageRequest
    @GetMapping("/{onlineShopId}/regions/{regionId}/unbind-pos")
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    public ResponseEntity<Page<Pos>> listUnbindByRegionId(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                          @PathVariable @ApiParam(value = "网店ID", required = true) final Long onlineShopId,
                                                          @PathVariable @ApiParam(value = "地区ID", required = true)final Long regionId,
                                                          final  RegionRelPos regionRelPos,
                                                          @ApiIgnore @SortDefault(value = RegionRelPos.FIELD_REGION_REL_POS_ID, direction = Sort.Direction.DESC) PageRequest pageRequest) {
        regionRelPos.setOnlineShopId(onlineShopId);
        regionRelPos.setTenantId(organizationId);
        regionRelPos.setRegionId(regionId);
        return Results.success(regionRelPosRepository.listUnbindPos(pageRequest, regionRelPos));
    }

    @ApiOperation(value = "批量创建默认服务点配置")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/batch")
    public ResponseEntity<?> batchCreate(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                   @RequestBody List<RegionRelPos> regionRelPos) {
        validList(regionRelPos);
        return regionRelPosRepository.batchCreate(organizationId, regionRelPos).getResponseEntity();
    }

    @ApiOperation(value = "批量修改默认服务点配置")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping("/batch")
    public ResponseEntity<?> batchUpdate(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                               @RequestBody List<RegionRelPos> regionRelPos) {
        SecurityTokenHelper.validToken(regionRelPos);
        return regionRelPosRepository.batchUpdate(organizationId, regionRelPos).getResponseEntity();
    }

}
