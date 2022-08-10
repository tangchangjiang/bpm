package org.o2.business.process.management.api;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.business.process.management.app.BusinessProcessService;
import org.o2.business.process.management.domain.entity.BusinessProcess;
import org.o2.business.process.management.domain.repository.BusinessProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 业务流程定义表 管理 API
 *
 * @author youlong.peng@hand-china.com
 * @date 2022-08-10 14:23:57
 */
@RestController("businessProcessController.v1")
@RequestMapping("/v1/{organizationId}/business-processs")
public class BusinessProcessController extends BaseController {

    @Autowired
    private BusinessProcessRepository businessProcessRepository;
    @Autowired
    private BusinessProcessService businessProcessService;

    @ApiOperation(value = "业务流程定义表维护-分页查询业务流程定义表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<BusinessProcess>> page(@PathVariable(value = "organizationId") Long organizationId,
                                                            BusinessProcess businessProcess,
                                                            @ApiIgnore @SortDefault(value = BusinessProcess.FIELD_BIZ_PROCESS_ID,
                                                                     direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<BusinessProcess> list = businessProcessRepository.pageAndSort(pageRequest, businessProcess);
        return Results.success(list);
    }

    @ApiOperation(value = "业务流程定义表维护-查询业务流程定义表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{bizProcessId}")
    public ResponseEntity<BusinessProcess> detail(@PathVariable(value = "organizationId") Long organizationId,
                                                        @ApiParam(value = "业务流程定义表ID", required = true) @PathVariable Long bizProcessId) {
        BusinessProcess businessProcess = businessProcessRepository.selectByPrimaryKey(bizProcessId);
        return Results.success(businessProcess);
    }

    @ApiOperation(value = "业务流程定义表维护-创建业务流程定义表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<BusinessProcess> create(@PathVariable(value = "organizationId") Long organizationId,
                                                       @RequestBody BusinessProcess businessProcess) {
        validObject(businessProcess);
        businessProcessService.save(businessProcess);
        return Results.success(businessProcess);
    }

    @ApiOperation(value = "业务流程定义表维护-修改业务流程定义表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<BusinessProcess> update(@PathVariable(value = "organizationId") Long organizationId,
                                                       @RequestBody BusinessProcess businessProcess) {
        SecurityTokenHelper.validToken(businessProcess);
        businessProcessService.save(businessProcess);
        return Results.success(businessProcess);
    }

        @ApiOperation(value = "业务流程定义表维护-批量保存业务流程定义表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/batch-saving")
    public ResponseEntity<List<BusinessProcess>> batchSave(@PathVariable(value = "organizationId") Long organizationId,
                                                       @RequestBody List<BusinessProcess> businessProcessList) {
        SecurityTokenHelper.validToken(businessProcessList);
        businessProcessService.batchSave(businessProcessList);
        return Results.success(businessProcessList);
    }

    @ApiOperation(value = "业务流程定义表维护-删除业务流程定义表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<Void> remove(@PathVariable(value = "organizationId") Long organizationId,
                                       @RequestBody BusinessProcess businessProcess) {
        SecurityTokenHelper.validToken(businessProcess);
        businessProcessRepository.deleteByPrimaryKey(businessProcess);
        return Results.success();
    }

}
