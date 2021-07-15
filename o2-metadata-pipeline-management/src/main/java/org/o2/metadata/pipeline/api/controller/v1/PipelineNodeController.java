package org.o2.metadata.pipeline.api.controller.v1;

import io.choerodon.core.base.BaseController;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.message.MessageAccessor;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.pipeline.app.service.PipelineNodeService;
import org.o2.metadata.pipeline.config.EnablePipelineManager;
import org.o2.metadata.pipeline.domain.entity.PipelineNode;
import org.o2.metadata.pipeline.domain.repository.PipelineNodeRepository;
import org.o2.metadata.pipeline.infra.constants.PipelineConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;
import java.util.List;

/**
 * @author huizhen.liu@hand-china.com 2019-01-09
 */
@RestController("pipelineNodeController.v1")
@RequestMapping("/v1/{organizationId}/pipeline-node")
@Api(tags = EnablePipelineManager.PIPELINE_NODE)
public class PipelineNodeController extends BaseController {
    private final PipelineNodeRepository pipelineNodeRepository;
    private final PipelineNodeService pipelineNodeService;

    public PipelineNodeController(final PipelineNodeRepository pipelineNodeRepository,
                                  final PipelineNodeService pipelineNodeService) {
        this.pipelineNodeRepository = pipelineNodeRepository;
        this.pipelineNodeService = pipelineNodeService;
    }

    @ApiOperation(value = "流程器节点列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping
    public ResponseEntity<?> list(final PipelineNode pipelineNode,
                                  @ApiIgnore @SortDefault(value = PipelineNode.FIELD_ID) final PageRequest pageRequest, @PathVariable Long organizationId) {
        pipelineNode.setTenantId(organizationId);
        return Results.success(PageHelper.doPage(pageRequest.getPage(), pageRequest.getSize(),
                () -> pipelineNodeRepository.listPipelineNode(pipelineNode)));
    }

    @ApiOperation(value = "流程器节点列表-无分页")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping("/list")
    public ResponseEntity<?> listAll(final PipelineNode pipelineNode, @PathVariable Long organizationId) {
        pipelineNode.setTenantId(organizationId);
        return Results.success(pipelineNodeRepository.listPipelineNode(pipelineNode));
    }

    @ApiOperation(value = "流程器节点明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable final Long id, @PathVariable Long organizationId) {
        PipelineNode pipelineNode = new PipelineNode();
        pipelineNode.setTenantId(organizationId);
        pipelineNode.setId(id);
        List<PipelineNode> pipelineNodes = pipelineNodeRepository.select(pipelineNode);
        if (CollectionUtils.isNotEmpty(pipelineNodes) && pipelineNodes.size() == 1) {
            return Results.success(pipelineNodes.get(0));
        } else {
            return Results.error();
        }
    }

    @ApiOperation(value = "批量新增流程器节点")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<?> create(@RequestBody List<PipelineNode> pipelineNodes, @PathVariable Long organizationId) {
        return createOrUpdateNodes(pipelineNodes, organizationId);
    }

    private ResponseEntity<?> createOrUpdateNodes(@RequestBody List<PipelineNode> pipelineNodes, @PathVariable Long organizationId) {
        pipelineNodes.forEach(pipelineNode -> pipelineNode.setTenantId(organizationId));
        int errorCount = pipelineNodeService.batchMerge(pipelineNodes);
        if (0 == errorCount) {
            return Results.success(Arrays.asList());
        } else {
            String resultString = MessageAccessor.getMessage(PipelineConstants.Message.PIPELINE_NODE_SUCCESS_NUM, String.valueOf((pipelineNodes.size() - errorCount))).desc();
            return Results.success(Arrays.asList(resultString));
        }
    }

    @ApiOperation(value = "批量更新流程器节点")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<?> update(@RequestBody List<PipelineNode> pipelineNodes, @PathVariable Long organizationId) {
        return createOrUpdateNodes(pipelineNodes, organizationId);
    }

    @ApiOperation(value = "批量删除流程器节点")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody List<PipelineNode> pipelineNodes, @PathVariable Long organizationId) {
        SecurityTokenHelper.validToken(pipelineNodes);
        pipelineNodes.forEach(pipelineNode -> pipelineNode.setTenantId(organizationId));
        pipelineNodeRepository.batchDeleteByPrimaryKey(pipelineNodes);
        return Results.success();
    }
}
