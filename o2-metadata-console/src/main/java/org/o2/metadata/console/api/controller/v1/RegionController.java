package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.console.app.service.RegionService;
import org.o2.metadata.console.config.EnableMetadataConsole;
import org.o2.metadata.core.domain.entity.Region;
import org.o2.metadata.core.domain.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

/**
 * 地区层级定义管理
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */

@RestController("regionController.v1")
@RequestMapping("/v1/{organizationId}/regions")
@Api(tags = EnableMetadataConsole.REGION)
public class RegionController extends BaseController {

    @Autowired
    private RegionService regionService;
    @Autowired
    private RegionRepository regionRepository;

    @ApiOperation("查询国家下地区定义，使用树状结构返回")
    @GetMapping
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> treeRegionWithParent(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                  @RequestParam(value = "countryIdOrCode")final String countryIdOrCode,
                                                  @RequestParam(required = false) final String condition,
                                                  @RequestParam(required = false) final Integer enabledFlag) {
        return Results.success(regionService.treeRegionWithParent(countryIdOrCode, condition, enabledFlag,organizationId));
    }

    @ApiOperation("查询国家/地区下的有效地区列表")
    @GetMapping("/valid")
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> listValidRegions(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                              @RequestParam(required = false,value = "countryIdOrCode") final String countryIdOrCode,
                                              @RequestParam(required = false,value = "regionId") final Long regionId) {
        if (countryIdOrCode == null && regionId == null) {
            return Results.success(Collections.emptyList());
        }
        return Results.success(regionRepository.listChildren(countryIdOrCode, regionId, 1,organizationId));
    }

    @ApiOperation("查询国家/地区下的地区列表")
    @GetMapping("/all")
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> listAllRegions(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                            @RequestParam(required = false) final String countryIdOrCode,
                                            @RequestParam(required = false) final Long regionId,
                                            @RequestParam(required = false) final Integer enabledFlag) {
        if (countryIdOrCode == null && regionId == null) {
            return Results.success(Collections.emptyList());
        }
        return Results.success(regionRepository.listChildren(countryIdOrCode, regionId, enabledFlag,organizationId));
    }

    @ApiOperation("查询大区下的省份")
    @GetMapping("/area")
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> listAreaRegions(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                             @RequestParam final String countryIdOrCode,
                                             @RequestParam(required = false) final Integer enabledFlag) {
        return Results.success(regionService.listAreaRegion(countryIdOrCode, enabledFlag,organizationId));
    }

    @ApiOperation("根据ID查询指定地区")
    @GetMapping("/query-by-id")
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> queryRegionById(@RequestParam final Long regionId) {
        return Results.success(regionRepository.selectByPrimaryKey(regionId));
    }

    @ApiOperation("根据CODE查询指定地区")
    @GetMapping("/query-by-code")
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> queryRegionByCode(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                               @RequestParam final String regionCode) {
        final Region region = new Region();
        region.setRegionCode(regionCode);
        region.setTenantId(organizationId);
        return Results.success(regionRepository.selectOne(region));
    }

    @ApiOperation("新增地区定义")
    @PostMapping
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> createRegion(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,@RequestBody final Region region) {
        this.validObject(region);
        region.setTenantId(organizationId);
        return Results.success(regionService.createRegion(region));
    }

    @ApiOperation("更新地区定义")
    @PutMapping
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> updateRegion(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,@RequestBody final Region region) {
        SecurityTokenHelper.validToken(region, false);
        this.validObject(region);
        region.setTenantId(organizationId);
        return ResponseEntity.ok(regionService.updateRegion(region));
    }

    @ApiOperation("禁用/启用地区定义，影响下级")
    @PatchMapping("/disable-or-enable")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> disableOrEnableRegion(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,@RequestBody final Region region) {
        SecurityTokenHelper.validToken(region);
        region.setTenantId(organizationId);
        return ResponseEntity.ok(regionService.disableOrEnable(region));
    }
}

