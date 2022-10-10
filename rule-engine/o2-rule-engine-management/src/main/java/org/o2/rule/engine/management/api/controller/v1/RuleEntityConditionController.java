package org.o2.rule.engine.management.api.controller.v1;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.rule.engine.management.app.service.RuleEntityConditionService;
import org.o2.rule.engine.management.domain.entity.RuleEntityCondition;
import org.o2.rule.engine.management.domain.repository.RuleEntityConditionRepository;
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
 * 规则实体条件 管理 API
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
@RestController("ruleEntityConditionController.v1")
@RequestMapping("/v1/{organizationId}/o2re-rule-entity-conditions")
public class RuleEntityConditionController extends BaseController {

    private final RuleEntityConditionRepository ruleEntityConditionRepository;
    private final RuleEntityConditionService ruleEntityConditionService;

    public RuleEntityConditionController(final RuleEntityConditionRepository ruleEntityConditionRepository,
                                         final RuleEntityConditionService ruleEntityConditionService) {
        this.ruleEntityConditionRepository = ruleEntityConditionRepository;
        this.ruleEntityConditionService = ruleEntityConditionService;
    }

    @ApiOperation(value = "规则实体条件维护-分页查询规则实体条件列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<RuleEntityCondition>> page(@PathVariable(value = "organizationId") Long organizationId,
                                                          RuleEntityCondition ruleEntityCondition,
                                                          @ApiIgnore @SortDefault(value = RuleEntityCondition.FIELD_RULE_ENTITY_CONDITION_ID,
                                                                  direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<RuleEntityCondition> list = ruleEntityConditionRepository.pageAndSort(pageRequest, ruleEntityCondition);
        return Results.success(list);
    }

    @ApiOperation(value = "规则实体条件维护-查询规则实体条件明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{ruleEntityConditionId}")
    public ResponseEntity<RuleEntityCondition> detail(@PathVariable(value = "organizationId") Long organizationId,
                                                      @ApiParam(value = "规则实体条件ID", required = true) @PathVariable Long ruleEntityConditionId) {
        RuleEntityCondition ruleEntityCondition = ruleEntityConditionRepository.selectByPrimaryKey(ruleEntityConditionId);
        return Results.success(ruleEntityCondition);
    }

    @ApiOperation(value = "规则实体条件维护-创建规则实体条件")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<RuleEntityCondition> create(@PathVariable(value = "organizationId") Long organizationId,
                                                      @RequestBody RuleEntityCondition ruleEntityCondition) {
        validObject(ruleEntityCondition);
        ruleEntityConditionService.save(ruleEntityCondition);
        return Results.success(ruleEntityCondition);
    }

    @ApiOperation(value = "规则实体条件维护-修改规则实体条件")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<RuleEntityCondition> update(@PathVariable(value = "organizationId") Long organizationId,
                                                      @RequestBody RuleEntityCondition ruleEntityCondition) {
        SecurityTokenHelper.validToken(ruleEntityCondition);
        ruleEntityConditionService.save(ruleEntityCondition);
        return Results.success(ruleEntityCondition);
    }

    @ApiOperation(value = "规则实体条件维护-批量保存规则实体条件")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/batch-saving")
    public ResponseEntity<List<RuleEntityCondition>> batchSave(@PathVariable(value = "organizationId") Long organizationId,
                                                               @RequestBody List<RuleEntityCondition> ruleEntityConditionList) {
        SecurityTokenHelper.validToken(ruleEntityConditionList);
        ruleEntityConditionService.batchSave(ruleEntityConditionList);
        return Results.success(ruleEntityConditionList);
    }

    @ApiOperation(value = "规则实体条件维护-删除规则实体条件")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<Void> remove(@PathVariable(value = "organizationId") Long organizationId,
                                       @RequestBody RuleEntityCondition ruleEntityCondition) {
        SecurityTokenHelper.validToken(ruleEntityCondition);
        ruleEntityConditionRepository.deleteByPrimaryKey(ruleEntityCondition);
        return Results.success();
    }

}
