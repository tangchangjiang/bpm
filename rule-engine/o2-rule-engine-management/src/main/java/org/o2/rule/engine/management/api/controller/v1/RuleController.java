package org.o2.rule.engine.management.api.controller.v1;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.core.response.OperateResponse;
import org.o2.rule.engine.management.app.service.RuleService;
import org.o2.rule.engine.management.domain.entity.Rule;
import org.o2.rule.engine.management.domain.repository.RuleRepository;
import org.o2.user.helper.IamUserHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.PageHelper;
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
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping
    public ResponseEntity<Page<Rule>> page(@PathVariable(value = "organizationId") Long organizationId,
                                           Rule rule,
                                           @ApiIgnore @SortDefault(value = Rule.FIELD_RULE_ID,
                                                   direction = Sort.Direction.DESC) PageRequest pageRequest) {
        rule.setTenantId(organizationId);
        final Page<Rule> rulePage = PageHelper.doPage(pageRequest, () -> {
            final List<Rule> rules = ruleRepository.ruleList(rule);
            if (CollectionUtils.isNotEmpty(rules)) {
                final List<String> createUserIds = rules.stream().map(r -> String.valueOf(r.getCreatedBy())).collect(Collectors.toList());
                final Map<Long, String> realNameMap = IamUserHelper.getRealNameMap(createUserIds);
                if (MapUtils.isEmpty(realNameMap)) {
                    return rules;
                }
                final Map<Long, List<Rule>> ruleMap = rules.stream().collect(Collectors.groupingBy(Rule::getCreatedBy));
                for (Map.Entry<Long, List<Rule>> entry : ruleMap.entrySet()) {
                    final String userName = realNameMap.get(entry.getKey());
                    entry.getValue().forEach(r -> {
                        r.setCreateUserName(userName);
                    });
                }
            }
            return rules;
        });

        return Results.success(rulePage);
    }

    @ApiOperation(value = "规则维护-查询规则明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{ruleId}")
    public ResponseEntity<Rule> detail(@PathVariable(value = "organizationId") Long organizationId,
                                       @ApiParam(value = "规则ID", required = true) @PathVariable Long ruleId) {
        return Results.success(ruleService.detail(organizationId, ruleId, null));
    }

    @ApiOperation(value = "规则维护-查询规则明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/rule-detail")
    public ResponseEntity<Rule> detailByCode(@PathVariable(value = "organizationId") Long organizationId,
                                       @ApiParam(value = "规则编码", required = true) @RequestParam String ruleCode) {
        return Results.success(ruleService.detail(organizationId, null, ruleCode));
    }

    @ApiOperation(value = "规则维护-创建规则")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<Rule> create(@PathVariable(value = "organizationId") Long organizationId,
                                       @RequestBody Rule rule) {
        rule.setTenantId(organizationId);
        validObject(rule);
        return Results.success(ruleService.createRule(organizationId, rule));
    }

    @ApiOperation(value = "规则维护-修改规则")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<Rule> update(@PathVariable(value = "organizationId") Long organizationId,
                                       @RequestBody Rule rule) {
        rule.setTenantId(organizationId);
        SecurityTokenHelper.validToken(rule);
        return Results.success(ruleService.updateRule(organizationId, rule));
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
    public ResponseEntity<OperateResponse> enable(@PathVariable Long organizationId, @RequestBody List<Long> ruleIds) {
        ruleService.enable(organizationId, ruleIds);
        return Results.success(OperateResponse.success());
    }

    @ApiOperation(value = "禁用规则")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping("/disable")
    public ResponseEntity<OperateResponse> disable(@PathVariable Long organizationId, @RequestBody List<Long> ruleIds) {
        ruleService.disable(organizationId, ruleIds);
        return Results.success(OperateResponse.success());
    }

}
