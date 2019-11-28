package org.o2.metadata.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.app.service.NeighboringRegionService;
import org.o2.metadata.config.EnableMetadata;
import org.o2.metadata.domain.entity.NeighboringRegion;
import org.o2.metadata.domain.repository.NeighboringRegionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 临近省 管理 API
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@RestController("neighboringRegionController.v1")
@RequestMapping("/v1/neighboring-regions")
@Api(tags = EnableMetadata.NEIGHBORING_REGION)
public class NeighboringRegionController extends BaseController {
    private final NeighboringRegionRepository neighboringRegionRepository;
    private final NeighboringRegionService neighboringRegionService;

    public NeighboringRegionController(final NeighboringRegionRepository neighboringRegionRepository,
                                       final NeighboringRegionService neighboringRegionService) {
        this.neighboringRegionRepository = neighboringRegionRepository;
        this.neighboringRegionService = neighboringRegionService;
    }

    @ApiOperation(value = "临近省列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping
    public ResponseEntity<?> list(final NeighboringRegion neighboringRegion,
                                  @ApiIgnore @SortDefault(value = NeighboringRegion.FIELD_SOURCE_REGION_ID,
                                          direction = Sort.Direction.ASC) final PageRequest pageRequest) {
        final Page<NeighboringRegion> result = PageHelper.doPageAndSort(pageRequest,
                () -> neighboringRegionService.findNeighboringRegions(neighboringRegion));
        return Results.success(result);
    }

    @ApiOperation(value = "创建临近省")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<?> create(@RequestBody final List<NeighboringRegion> neighboringRegion) {
        return Results.success(neighboringRegionService.batchInsert(neighboringRegion));
    }

    @ApiOperation(value = "批量删除临近省")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody final List<NeighboringRegion> neighboringRegions) {
        SecurityTokenHelper.validToken(neighboringRegions);
        neighboringRegionRepository.batchDeleteByPrimaryKey(neighboringRegions);
        return Results.success();
    }
}
