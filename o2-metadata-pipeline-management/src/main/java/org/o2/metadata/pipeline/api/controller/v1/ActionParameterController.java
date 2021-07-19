package org.o2.metadata.pipeline.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.pipeline.config.EnablePipelineManager;
import org.o2.metadata.pipeline.domain.entity.ActionParameter;
import org.o2.metadata.pipeline.domain.repository.ActionParameterRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 行为参数 管理 API
 *
 * @author wei.cai@hand-china.com 2020-03-18
 */
@RestController("actionParameterController.v1")
@RequestMapping("/v1/{organizationId}/action-parameters")
@Api(tags = {EnablePipelineManager.ACTION_PARAMETER})
public class ActionParameterController extends BaseController {

    private final ActionParameterRepository actionParameterRepository;

    public ActionParameterController(final ActionParameterRepository actionParameterRepository) {
        this.actionParameterRepository = actionParameterRepository;
    }

    @ApiOperation(value = "行为参数列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<ActionParameter>> list(@PathVariable Long organizationId, ActionParameter actionParameter, @ApiIgnore @SortDefault(value = ActionParameter.FIELD_ACTION_PARAMETER_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        actionParameter.setTenantId(organizationId);
        Page<ActionParameter> list = actionParameterRepository.pageAndSort(pageRequest, actionParameter);
        return Results.success(list);
    }

    @ApiOperation(value = "创建行为参数")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/batch")
    public ResponseEntity<Void> batchCreate(@PathVariable Long organizationId, @RequestBody List<ActionParameter> actionParameters) {
        actionParameters.forEach(a -> a.setTenantId(organizationId));
        validObject(actionParameters);
        actionParameterRepository.batchInsertSelective(actionParameters);
        actionParameters.forEach(actionParameterRepository::cache);
        return Results.success();
    }

    @ApiOperation(value = "修改行为参数")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping("/batch")
    public ResponseEntity<Void> batchUpdate(@PathVariable Long organizationId, @RequestBody List<ActionParameter> actionParameters) {
        actionParameters.forEach(a -> a.setTenantId(organizationId));
        SecurityTokenHelper.validToken(actionParameters);
        actionParameterRepository.batchUpdateByPrimaryKeySelective(actionParameters);
        actionParameters.forEach(actionParameterRepository::cache);
        return Results.success();
    }

    @ApiOperation(value = "删除行为参数")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<Void> remove(@RequestBody ActionParameter actionParameter) {
        SecurityTokenHelper.validToken(actionParameter);
        actionParameterRepository.deleteByPrimaryKey(actionParameter);
        actionParameterRepository.removeCache(actionParameter);
        return Results.success();
    }

}
