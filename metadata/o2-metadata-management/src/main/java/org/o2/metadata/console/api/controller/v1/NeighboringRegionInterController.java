package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.util.Results;
import org.o2.metadata.console.api.co.NeighboringRegionCO;
import org.o2.metadata.console.app.service.NeighboringRegionService;
import org.o2.metadata.console.infra.config.MetadataManagementAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 临近省 管理 API
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@RestController("neighboringRegionInterController.v1")
@RequestMapping("/v1/{organizationId}/neighboring-regions-internal")
@Api(tags = MetadataManagementAutoConfiguration.NEIGHBORING_REGION)
public class NeighboringRegionInterController {
    private final NeighboringRegionService neighboringRegionService;

    public NeighboringRegionInterController(final NeighboringRegionService neighboringRegionService) {
        this.neighboringRegionService = neighboringRegionService;
    }

    @ApiOperation(value = "临近省列表(内部）")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping
    public ResponseEntity<List<NeighboringRegionCO>> listNeighboringRegions(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId) {
        return Results.success(neighboringRegionService.listNeighboringRegions(organizationId));
    }
}
