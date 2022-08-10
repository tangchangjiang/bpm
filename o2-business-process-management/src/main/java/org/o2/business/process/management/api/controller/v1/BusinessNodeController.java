package org.o2.business.process.management.api.controller.v1;

import io.swagger.annotations.Api;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import org.o2.business.process.management.config.BusinessProcessManagerAutoConfiguration;
import org.o2.business.process.management.domain.entity.BusinessNode;
import org.o2.business.process.management.domain.repository.BusinessNodeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.business.process.management.app.service.BusinessNodeService;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;
import io.swagger.annotations.ApiParam;
import java.util.List;

/**
 * 业务流程节点表 管理 API
 *
 * @author zhilin.ren@hand-china.com 2022-08-10 14:31:01
 */
@RestController("businessNodeController.v1")
@Api(tags = BusinessProcessManagerAutoConfiguration.BUSINESS_PROCESS_NODE)
@RequestMapping("/v1/{organizationId}/business-nodes")
public class BusinessNodeController extends BaseController {

    private final BusinessNodeRepository businessNodeRepository;
    private final BusinessNodeService businessNodeService;

    public BusinessNodeController(BusinessNodeRepository businessNodeRepository, BusinessNodeService businessNodeService) {
        this.businessNodeRepository = businessNodeRepository;
        this.businessNodeService = businessNodeService;
    }

    @ApiOperation(value = "业务流程节点表维护-分页查询业务流程节点表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<BusinessNode>> page(@PathVariable(value = "organizationId") Long organizationId,
                                                            BusinessNode businessNode,
                                                            @ApiIgnore @SortDefault(value = BusinessNode.FIELD_BIZ_NODE_ID,
                                                                     direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<BusinessNode> list = businessNodeRepository.pageAndSort(pageRequest, businessNode);
        return Results.success(list);
    }

    @ApiOperation(value = "业务流程节点表维护-查询业务流程节点表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{bizNodeId}")
    public ResponseEntity<BusinessNode> detail(@PathVariable(value = "organizationId") Long organizationId,
                                                        @ApiParam(value = "业务流程节点表ID", required = true) @PathVariable Long bizNodeId) {
        BusinessNode businessNode = businessNodeRepository.selectByPrimaryKey(bizNodeId);
        return Results.success(businessNode);
    }

    @ApiOperation(value = "业务流程节点表维护-创建业务流程节点表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<BusinessNode> create(@PathVariable(value = "organizationId") Long organizationId,
                                                       @RequestBody BusinessNode businessNode) {
        validObject(businessNode);
        businessNodeService.save(businessNode);
        return Results.success(businessNode);
    }

    @ApiOperation(value = "业务流程节点表维护-修改业务流程节点表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<BusinessNode> update(@PathVariable(value = "organizationId") Long organizationId,
                                                       @RequestBody BusinessNode businessNode) {
        SecurityTokenHelper.validToken(businessNode);
        businessNodeService.save(businessNode);
        return Results.success(businessNode);
    }

        @ApiOperation(value = "业务流程节点表维护-批量保存业务流程节点表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/batch-saving")
    public ResponseEntity<List<BusinessNode>> batchSave(@PathVariable(value = "organizationId") Long organizationId,
                                                       @RequestBody List<BusinessNode> businessNodeList) {
        SecurityTokenHelper.validToken(businessNodeList);
        businessNodeService.batchSave(businessNodeList);
        return Results.success(businessNodeList);
    }

    @ApiOperation(value = "业务流程节点表维护-删除业务流程节点表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<Void> remove(@PathVariable(value = "organizationId") Long organizationId,
                                       @RequestBody BusinessNode businessNode) {
        SecurityTokenHelper.validToken(businessNode);
        businessNodeRepository.deleteByPrimaryKey(businessNode);
        return Results.success();
    }

}
