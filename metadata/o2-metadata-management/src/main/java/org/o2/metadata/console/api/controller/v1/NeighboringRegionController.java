package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.core.response.OperateResponse;
import org.o2.metadata.console.api.dto.NeighboringRegionQueryDTO;
import org.o2.metadata.console.app.service.NeighboringRegionService;
import org.o2.metadata.console.app.service.SourcingCacheUpdateService;
import org.o2.metadata.console.infra.config.MetadataManagementAutoConfiguration;
import org.o2.metadata.console.infra.entity.NeighboringRegion;
import org.o2.metadata.console.infra.repository.NeighboringRegionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 临近省 管理 API
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@RestController("neighboringRegionController.v1")
@RequestMapping("/v1/{organizationId}/neighboring-regions")
@Api(tags = MetadataManagementAutoConfiguration.NEIGHBORING_REGION)
public class NeighboringRegionController extends BaseController {
    private final NeighboringRegionRepository neighboringRegionRepository;
    private final NeighboringRegionService neighboringRegionService;
    private final SourcingCacheUpdateService sourcingCacheUpdateService;

    public NeighboringRegionController(final NeighboringRegionRepository neighboringRegionRepository,
                                       final NeighboringRegionService neighboringRegionService, SourcingCacheUpdateService sourcingCacheUpdateService) {
        this.neighboringRegionRepository = neighboringRegionRepository;
        this.neighboringRegionService = neighboringRegionService;
        this.sourcingCacheUpdateService = sourcingCacheUpdateService;
    }

    @ApiOperation(value = "临近省列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping
    public ResponseEntity<Page<NeighboringRegion>> list(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,final NeighboringRegionQueryDTO neighboringRegion,
                                  @ApiIgnore @SortDefault(value = NeighboringRegion.FIELD_SOURCE_REGION_CODE,
                                          direction = Sort.Direction.ASC) final PageRequest pageRequest) {
        neighboringRegion.setTenantId(organizationId);
        final Page<NeighboringRegion> result = PageHelper.doPageAndSort(pageRequest,
                () -> neighboringRegionService.findNeighboringRegions(neighboringRegion));
        return Results.success(result);
    }

    @ApiOperation(value = "创建临近省")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<List<NeighboringRegion>> create(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,@RequestBody final List<NeighboringRegion> neighboringRegion) {
        List<NeighboringRegion> result = neighboringRegionService.batchInsert(organizationId, neighboringRegion);
        sourcingCacheUpdateService.refreshSourcingNearRegion(organizationId, this.getClass().getSimpleName());
        return Results.success(result);
    }

    @ApiOperation(value = "批量删除临近省")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<OperateResponse> remove(@RequestBody final List<NeighboringRegion> neighboringRegions, @PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId) {
        SecurityTokenHelper.validToken(neighboringRegions);
        neighboringRegionRepository.batchDeleteByPrimaryKey(neighboringRegions);
        sourcingCacheUpdateService.refreshSourcingNearRegion(organizationId, this.getClass().getSimpleName());
        return Results.success(OperateResponse.success());
    }
}