package org.o2.metadata.pipeline.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.pipeline.app.service.PipelineActionService;
import org.o2.metadata.pipeline.config.PipelineManagerAutoConfiguration;
import org.o2.metadata.pipeline.domain.entity.PipelineAction;
import org.o2.metadata.pipeline.domain.repository.PipelineActionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 流程器行为 管理 API
 *
 * @author wenjun.deng01@hand-china.com 2019-12-16 10:36:04
 */
@RestController("pipelineActionController.v1")
@RequestMapping("/v1/{organizationId}/pipeline-actions")
@Api(tags = {PipelineManagerAutoConfiguration.PIPELINE_ACTION})
public class PipelineActionController extends BaseController {

    private final PipelineActionRepository pipelineActionRepository;
    private final PipelineActionService pipelineActionService;

    public PipelineActionController(final PipelineActionRepository pipelineActionRepository,
                                    final PipelineActionService pipelineActionService) {
        this.pipelineActionRepository = pipelineActionRepository;
        this.pipelineActionService = pipelineActionService;
    }

    @ApiOperation(value = "流程器行为列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    public ResponseEntity<Page<PipelineAction>> list(PipelineAction pipelineAction, @ApiIgnore @SortDefault(value = PipelineAction.FIELD_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest, @PathVariable Long organizationId) {
        pipelineAction.setTenantId(organizationId);
        return Results.success(PageHelper.doPage(pageRequest, () -> pipelineActionRepository.listPipelineAction(pipelineAction)));
    }

    @ApiOperation(value = "获取行为列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/all")
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    public ResponseEntity<List<PipelineAction>> all(PipelineAction pipelineAction, @PathVariable Long organizationId) {
        pipelineAction.setTenantId(organizationId);
        final List<PipelineAction> list = pipelineActionRepository.allWithParameters(pipelineAction);
        return Results.success(list);
    }

    @ApiOperation(value = "流程器行为明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{id}")
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    public ResponseEntity<PipelineAction> detail(@PathVariable Long id, @PathVariable Long organizationId) {
        PipelineAction pipelineAction = new PipelineAction();
        pipelineAction.setTenantId(organizationId);
        pipelineAction.setId(id);
        List<PipelineAction> pipelineActions = pipelineActionRepository.select(pipelineAction);
        if (CollectionUtils.isNotEmpty(pipelineActions) && pipelineActions.size() == 1) {
            return Results.success(pipelineActions.get(0));
        } else {
            return Results.error();
        }
    }

    @ApiOperation(value = "创建流程器行为")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    public ResponseEntity<PipelineAction> create(@RequestBody PipelineAction pipelineAction, @PathVariable Long organizationId) {
        pipelineAction.setTenantId(organizationId);
        pipelineActionService.savePipelineAction(pipelineAction, organizationId);
        return Results.success(pipelineAction);
    }

    @ApiOperation(value = "修改流程器行为")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    public ResponseEntity<PipelineAction> update(@RequestBody PipelineAction pipelineAction, @PathVariable Long organizationId) {
        pipelineActionService.updatePipelineAction(pipelineAction, organizationId);
        return Results.success(pipelineAction);
    }

    @ApiOperation(value = "删除流程器行为")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<PipelineAction> remove(@RequestBody PipelineAction pipelineAction, @PathVariable Long organizationId) {
        SecurityTokenHelper.validToken(pipelineAction);
        pipelineAction.setTenantId(organizationId);
        pipelineActionService.delete(pipelineAction, organizationId);
        return Results.success();
    }

}
