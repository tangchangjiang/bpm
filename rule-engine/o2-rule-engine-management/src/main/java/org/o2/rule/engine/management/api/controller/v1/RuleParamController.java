package org.o2.rule.engine.management.api.controller.v1;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.rule.engine.management.app.service.RuleParamService;
import org.o2.rule.engine.management.domain.entity.RuleParam;
import org.o2.rule.engine.management.domain.repository.RuleParamRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;

/**
 * 规则参数 管理 API
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
@RestController("o2reRuleParamController.v1")
@RequestMapping("/v1/{organizationId}/o2re-rule-params")
public class RuleParamController extends BaseController {

    private final RuleParamRepository ruleParamRepository;
    private final RuleParamService ruleParamService;

    public RuleParamController(final RuleParamRepository ruleParamRepository,
                               final RuleParamService ruleParamService) {
        this.ruleParamRepository = ruleParamRepository;
        this.ruleParamService = ruleParamService;
    }

    @ApiOperation(value = "规则参数维护-分页查询规则参数列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<RuleParam>> page(@PathVariable(value = "organizationId") Long organizationId,
                                                RuleParam ruleParam,
                                                @ApiIgnore @SortDefault(value = RuleParam.FIELD_RULE_PARAM_ID,
                                                        direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<RuleParam> list = ruleParamRepository.pageAndSort(pageRequest, ruleParam);
        return Results.success(list);
    }

    @ApiOperation(value = "规则参数维护-查询规则参数明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{ruleParamId}")
    public ResponseEntity<RuleParam> detail(@PathVariable(value = "organizationId") Long organizationId,
                                            @ApiParam(value = "规则参数ID", required = true) @PathVariable Long ruleParamId) {
        RuleParam ruleParam = ruleParamRepository.selectByPrimaryKey(ruleParamId);
        return Results.success(ruleParam);
    }

    @ApiOperation(value = "规则参数维护-创建规则参数")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<RuleParam> create(@PathVariable(value = "organizationId") Long organizationId,
                                            @RequestBody RuleParam ruleParam) {
        ruleParam.setTenantId(organizationId);
        validObject(ruleParam);
        ruleParamService.save(ruleParam);
        return Results.success(ruleParam);
    }

    @ApiOperation(value = "规则参数维护-修改规则参数")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<RuleParam> update(@PathVariable(value = "organizationId") Long organizationId,
                                            @RequestBody RuleParam ruleParam) {
        SecurityTokenHelper.validToken(ruleParam);
        ruleParamService.save(ruleParam);
        return Results.success(ruleParam);
    }

    @ApiOperation(value = "规则参数维护-批量保存规则参数")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/batch-saving")
    public ResponseEntity<List<RuleParam>> batchSave(@PathVariable(value = "organizationId") Long organizationId,
                                                     @RequestBody List<RuleParam> ruleParamList) {
        SecurityTokenHelper.validToken(ruleParamList);
        ruleParamService.batchSave(ruleParamList);
        return Results.success(ruleParamList);
    }

    @ApiOperation(value = "规则参数维护-删除规则参数")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<Void> remove(@PathVariable(value = "organizationId") Long organizationId,
                                       @RequestBody RuleParam ruleParam) {
        SecurityTokenHelper.validToken(ruleParam);
        ruleParamRepository.deleteByPrimaryKey(ruleParam);
        return Results.success();
    }

}
