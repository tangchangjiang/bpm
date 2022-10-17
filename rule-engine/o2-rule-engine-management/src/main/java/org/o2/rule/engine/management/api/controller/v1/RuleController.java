package org.o2.rule.engine.management.api.controller.v1;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.core.response.OperateResponse;
import org.o2.rule.engine.management.app.service.RuleService;
import org.o2.rule.engine.management.domain.entity.Rule;
import org.o2.rule.engine.management.domain.repository.RuleRepository;
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
 * 规则 管理 API
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
@RestController("ruleController.v1")
@RequestMapping("/v1/{organizationId}/o2re-rules")
public class RuleController extends BaseController {

    private final RuleRepository ruleRepository;
    private final RuleService ruleService;

    public RuleController(final RuleRepository ruleRepository,
                          final RuleService ruleService) {
        this.ruleRepository = ruleRepository;
        this.ruleService = ruleService;
    }

    @ApiOperation(value = "规则维护-分页查询规则列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<Rule>> page(@PathVariable(value = "organizationId") Long organizationId,
                                           Rule rule,
                                           @ApiIgnore @SortDefault(value = Rule.FIELD_RULE_ID,
                                                   direction = Sort.Direction.DESC) PageRequest pageRequest) {
        rule.setTenantId(organizationId);
        Page<Rule> list = ruleRepository.pageAndSort(pageRequest, rule);
        return Results.success(list);
    }

    @ApiOperation(value = "规则维护-查询规则明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{ruleId}")
    public ResponseEntity<Rule> detail(@PathVariable(value = "organizationId") Long organizationId,
                                       @ApiParam(value = "规则ID", required = true) @PathVariable Long ruleId) {
        return Results.success(ruleService.detail(organizationId, ruleId));
    }

    @ApiOperation(value = "规则维护-创建规则")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<Rule> create(@PathVariable(value = "organizationId") Long organizationId,
                                       @RequestBody Rule rule) {
        validObject(rule);
        ruleService.createRule(organizationId, rule);
        return Results.success(rule);
    }

    @ApiOperation(value = "规则维护-修改规则")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<Rule> update(@PathVariable(value = "organizationId") Long organizationId,
                                       @RequestBody Rule rule) {
        SecurityTokenHelper.validToken(rule);
        ruleService.updateRule(organizationId, rule);
        return Results.success(rule);
    }

    @ApiOperation(value = "规则维护-删除规则")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<Void> remove(@PathVariable(value = "organizationId") Long organizationId,
                                       @RequestBody Rule rule) {
        SecurityTokenHelper.validToken(rule);
        ruleRepository.deleteByPrimaryKey(rule);
        return Results.success();
    }

    @ApiOperation(value = "启用规则")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping("/enable")
    public ResponseEntity<OperateResponse> enable(@PathVariable Long organizationId, @RequestBody List<Rule> rules) {
        SecurityTokenHelper.validToken(rules, false);
        rules.forEach(r -> r.setTenantId(organizationId));

        return Results.success(OperateResponse.success());
    }

    @ApiOperation(value = "禁用规则")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping("/disable")
    public ResponseEntity<OperateResponse> disable(@PathVariable Long organizationId, @RequestBody List<Rule> rules) {
        SecurityTokenHelper.validToken(rules, false);
        rules.forEach(r -> r.setTenantId(organizationId));

        return Results.success(OperateResponse.success());
    }

}
