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
import org.o2.metadata.console.api.vo.AreaRegionVO;
import org.o2.metadata.console.api.vo.RegionVO;
import org.o2.metadata.console.app.service.RegionService;
import org.o2.metadata.console.config.MetadataManagementAutoConfiguration;
import org.o2.metadata.console.infra.repository.RegionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * 地区层级定义管理
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */

@RestController("regionController.v1")
@RequestMapping("/v1/{organizationId}/regions")
@Api(tags = MetadataManagementAutoConfiguration.REGION)
public class RegionController extends BaseController {

    private final RegionService regionService;
    private final RegionRepository regionRepository;

    public RegionController(RegionService regionService, RegionRepository regionRepository) {
        this.regionService = regionService;
        this.regionRepository = regionRepository;
    }

    @ApiOperation("查询国家下地区定义，使用树状结构返回")
    @GetMapping
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<RegionVO>> treeRegionWithParent(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                  @RequestParam(value = "countryCode")final String countryCode,
                                                  @RequestParam(required = false) final String condition,
                                                  @RequestParam(required = false) final Integer enabledFlag) {
        return Results.success(regionService.treeRegionWithParent(countryCode, condition, enabledFlag,organizationId));
    }

    @ApiOperation("查询国家/地区下的有效地区列表")
    @GetMapping("/valid")
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<RegionVO>> listValidRegions(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                           @RequestParam(required = false,value = "countryIdOrCode") final String countryIdOrCode,
                                                           @RequestParam(required = false,value = "regionId") final Long regionId) {
        if (countryIdOrCode == null && regionId == null) {
            return Results.success(Collections.emptyList());
        }
        return Results.success(regionService.listChildren(countryIdOrCode, regionId, 1,organizationId));
    }

    @ApiOperation("查询国家/地区下的地区列表")
    @GetMapping("/all")
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<RegionVO>> listAllRegions(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                            @RequestParam(required = false) final String countryIdOrCode,
                                            @RequestParam(required = false) final Long regionId,
                                            @RequestParam(required = false) final Integer enabledFlag) {
        if (countryIdOrCode == null && regionId == null) {
            return Results.success(Collections.emptyList());
        }
        return Results.success(regionService.listChildren(countryIdOrCode, regionId, enabledFlag,organizationId));
    }

    @ApiOperation("查询大区下的省份")
    @GetMapping("/area")
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<AreaRegionVO>> listAreaRegions(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
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
    public ResponseEntity<RegionVO> queryRegionByCode(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                               @RequestParam final String regionCode) {
        return Results.success(regionService.selectOneByCode(regionCode,organizationId));
    }

}

