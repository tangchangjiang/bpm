package org.o2.rule.engine.management.api.controller.v1.site;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
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
import org.o2.mybatis.tenanthelper.TenantHelper;
import org.o2.rule.engine.management.app.service.RuleService;
import org.o2.rule.engine.management.domain.entity.Rule;
import org.o2.rule.engine.management.domain.repository.RuleRepository;
import org.o2.user.helper.IamUserHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 规则 管理 API(站点层)
 *
 * @author chao.yang05@hand-china.com 2023-05-22
 */
@RestController("ruleSiteController.v1")
@RequestMapping("/v1/o2re-rules")
public class RuleSiteController extends BaseController {

    private final RuleRepository ruleRepository;
    private final RuleService ruleService;

    public RuleSiteController(final RuleRepository ruleRepository,
                          final RuleService ruleService) {
        this.ruleRepository = ruleRepository;
        this.ruleService = ruleService;
    }

    @ApiOperation(value = "规则维护-分页查询规则列表（站点层）")
    @Permission(level = ResourceLevel.SITE)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping
    public ResponseEntity<Page<Rule>> page(Rule rule,
                                           @ApiIgnore @SortDefault(value = Rule.FIELD_RULE_ID,
                                                   direction = Sort.Direction.DESC) PageRequest pageRequest) {
        rule.setTenantId(BaseConstants.DEFAULT_TENANT_ID);
        final Page<Rule> rulePage = PageHelper.doPage(pageRequest, () -> {
            final List<Rule> rules = TenantHelper.organizationLevelLimit(() -> ruleService.ruleList(rule));
            if (CollectionUtils.isNotEmpty(rules)) {
                final List<String> createUserIds = rules.stream().map(r -> String.valueOf(r.getCreatedBy())).collect(Collectors.toList());
                final Map<Long, String> realNameMap = IamUserHelper.getRealNameMap(createUserIds);
                if (MapUtils.isEmpty(realNameMap)) {
                    return rules;
                }
                final Map<Long, List<Rule>> ruleMap = rules.stream().collect(Collectors.groupingBy(Rule::getCreatedBy));
                for (Map.Entry<Long, List<Rule>> entry : ruleMap.entrySet()) {
                    final String userName = realNameMap.get(entry.getKey());
                    entry.getValue().forEach(r -> r.setCreateUserName(userName));
                }
            }
            return rules;
        });

        return Results.success(rulePage);
    }

    @ApiOperation(value = "规则维护-查询规则明细（站点层）")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping("/{ruleId}")
    public ResponseEntity<Rule> detail(@ApiParam(value = "规则ID", required = true) @PathVariable Long ruleId) {
        return Results.success(ruleService.detail(BaseConstants.DEFAULT_TENANT_ID, ruleId, null));
    }

    @ApiOperation(value = "规则维护-查询规则明细（站点层）")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping("/rule-detail")
    public ResponseEntity<Rule> detailByCode(@ApiParam(value = "规则编码", required = true) @RequestParam String ruleCode) {
        return Results.success(ruleService.detail(BaseConstants.DEFAULT_TENANT_ID, null, ruleCode));
    }

    @ApiOperation(value = "规则维护-创建规则（站点层）")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping
    public ResponseEntity<Rule> create(@RequestBody Rule rule) {
        rule.setTenantId(BaseConstants.DEFAULT_TENANT_ID);
        validObject(rule);
        return Results.success(ruleService.createRule(BaseConstants.DEFAULT_TENANT_ID, rule));
    }

    @ApiOperation(value = "规则维护-修改规则（站点层）")
    @Permission(level = ResourceLevel.SITE)
    @PutMapping
    public ResponseEntity<Rule> update(@RequestBody Rule rule) {
        rule.setTenantId(BaseConstants.DEFAULT_TENANT_ID);
        SecurityTokenHelper.validToken(rule);
        return Results.success(ruleService.updateRule(BaseConstants.DEFAULT_TENANT_ID, rule));
    }

    @ApiOperation(value = "规则维护-删除规则（站点层）")
    @Permission(level = ResourceLevel.SITE)
    @DeleteMapping
    public ResponseEntity<Void> remove(@RequestBody Rule rule) {
        SecurityTokenHelper.validToken(rule);
        ruleRepository.deleteByPrimaryKey(rule);
        return Results.success();
    }

    @ApiOperation(value = "启用规则（站点层）")
    @Permission(level = ResourceLevel.SITE)
    @PutMapping("/enable")
    public ResponseEntity<OperateResponse> enable(@RequestBody List<Long> ruleIds) {
        ruleService.enable(BaseConstants.DEFAULT_TENANT_ID, ruleIds);
        return Results.success(OperateResponse.success());
    }

    @ApiOperation(value = "禁用规则（站点层）")
    @Permission(level = ResourceLevel.SITE)
    @PutMapping("/disable")
    public ResponseEntity<OperateResponse> disable(@RequestBody List<Long> ruleIds) {
        ruleService.disable(BaseConstants.DEFAULT_TENANT_ID, ruleIds);
        return Results.success(OperateResponse.success());
    }
}
