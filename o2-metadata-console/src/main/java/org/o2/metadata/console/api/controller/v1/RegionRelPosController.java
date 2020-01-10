package org.o2.metadata.console.api.controller.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import org.o2.metadata.console.config.EnableMetadataConsole;
import org.o2.metadata.core.domain.entity.RegionRelPos;
import org.o2.metadata.core.domain.repository.RegionRelPosRepository;
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

    public RegionRelPosController(RegionRelPosRepository regionRelPosRepository) {
        this.regionRelPosRepository = regionRelPosRepository;
    }

    @ApiOperation(value = "通过网店id获取默认服务点配置列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{onlineStoreId}/regions")
    public ResponseEntity<Page<RegionRelPos>> listByStoreId(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                   @PathVariable @ApiParam(value = "网店ID", required = true) Long onlineStoreId,
                                                   @ApiIgnore @SortDefault(value = RegionRelPos.FIELD_REGION_REL_POS_ID, direction = Sort.Direction.DESC) PageRequest pageRequest) {
        final RegionRelPos regionRelPos = new RegionRelPos();
        regionRelPos.setOnlineShopId(onlineStoreId);
        regionRelPos.setTenantId(organizationId);
        return Results.success(regionRelPosRepository.listByCondition(pageRequest, regionRelPos));
    }

    @ApiOperation(value = "通过网店id以及地区id获取默认服务点配置列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
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

    @ApiOperation(value = "创建默认服务点配置")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<RegionRelPos> create(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                               @RequestBody RegionRelPos regionRelPos) {
        validObject(regionRelPos);
        regionRelPos.setTenantId(organizationId);
        regionRelPosRepository.insertSelective(regionRelPos);
        return Results.success(regionRelPos);
    }

    @ApiOperation(value = "修改默认服务点配置")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<RegionRelPos> update(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                               @RequestBody RegionRelPos regionRelPos) {
        SecurityTokenHelper.validToken(regionRelPos);
        regionRelPos.setTenantId(organizationId);
        regionRelPosRepository.updateByPrimaryKeySelective(regionRelPos);
        return Results.success(regionRelPos);
    }

    @ApiOperation(value = "删除默认服务点配置")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                    @RequestBody RegionRelPos regionRelPos) {
        SecurityTokenHelper.validToken(regionRelPos);
        regionRelPos.setTenantId(organizationId);
        regionRelPosRepository.deleteByPrimaryKey(regionRelPos);
        return Results.success();
    }

}
