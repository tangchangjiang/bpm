package org.o2.rule.engine.management.api.controller.v1;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.rule.engine.management.app.service.RuleCondParamValueService;
import org.o2.rule.engine.management.domain.entity.RuleCondParamValue;
import org.o2.rule.engine.management.domain.repository.RuleCondParamValueRepository;
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
 * 规则条件参数值 管理 API
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
@RestController("ruleCondParamValueController.v1")
@RequestMapping("/v1/{organizationId}/o2re-cond-param-values")
public class RuleCondParamValueController extends BaseController {

    private final RuleCondParamValueRepository ruleCondParamValueRepository;
    private final RuleCondParamValueService ruleCondParamValueService;

    public RuleCondParamValueController(final RuleCondParamValueRepository ruleCondParamValueRepository,
                                        final RuleCondParamValueService ruleCondParamValueService) {
        this.ruleCondParamValueRepository = ruleCondParamValueRepository;
        this.ruleCondParamValueService = ruleCondParamValueService;
    }

    @ApiOperation(value = "规则条件参数值维护-分页查询规则条件参数值列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<RuleCondParamValue>> page(@PathVariable(value = "organizationId") Long organizationId,
                                                         RuleCondParamValue ruleCondParamValue,
                                                         @ApiIgnore @SortDefault(value = RuleCondParamValue.FIELD_COND_PARAM_VALUE_ID,
                                                                 direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<RuleCondParamValue> list = ruleCondParamValueRepository.pageAndSort(pageRequest, ruleCondParamValue);
        return Results.success(list);
    }

    @ApiOperation(value = "规则条件参数值维护-查询规则条件参数值明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{condParamValueId}")
    public ResponseEntity<RuleCondParamValue> detail(@PathVariable(value = "organizationId") Long organizationId,
                                                     @ApiParam(value = "规则条件参数值ID", required = true) @PathVariable Long condParamValueId) {
        RuleCondParamValue ruleCondParamValue = ruleCondParamValueRepository.selectByPrimaryKey(condParamValueId);
        return Results.success(ruleCondParamValue);
    }

    @ApiOperation(value = "规则条件参数值维护-创建规则条件参数值")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<RuleCondParamValue> create(@PathVariable(value = "organizationId") Long organizationId,
                                                     @RequestBody RuleCondParamValue ruleCondParamValue) {
        validObject(ruleCondParamValue);
        ruleCondParamValueService.save(ruleCondParamValue);
        return Results.success(ruleCondParamValue);
    }

    @ApiOperation(value = "规则条件参数值维护-修改规则条件参数值")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<RuleCondParamValue> update(@PathVariable(value = "organizationId") Long organizationId,
                                                     @RequestBody RuleCondParamValue ruleCondParamValue) {
        SecurityTokenHelper.validToken(ruleCondParamValue);
        ruleCondParamValueService.save(ruleCondParamValue);
        return Results.success(ruleCondParamValue);
    }

    @ApiOperation(value = "规则条件参数值维护-批量保存规则条件参数值")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/batch-saving")
    public ResponseEntity<List<RuleCondParamValue>> batchSave(@PathVariable(value = "organizationId") Long organizationId,
                                                              @RequestBody List<RuleCondParamValue> ruleCondParamValueList) {
        SecurityTokenHelper.validToken(ruleCondParamValueList);
        ruleCondParamValueService.batchSave(ruleCondParamValueList);
        return Results.success(ruleCondParamValueList);
    }

    @ApiOperation(value = "规则条件参数值维护-删除规则条件参数值")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<Void> remove(@PathVariable(value = "organizationId") Long organizationId,
                                       @RequestBody RuleCondParamValue ruleCondParamValue) {
        SecurityTokenHelper.validToken(ruleCondParamValue);
        ruleCondParamValueRepository.deleteByPrimaryKey(ruleCondParamValue);
        return Results.success();
    }

}
