package org.o2.rule.engine.management.api.controller.v1;

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
import org.o2.rule.engine.management.app.service.RuleCondRelEntityService;
import org.o2.rule.engine.management.domain.entity.RuleCondRelEntity;
import org.o2.rule.engine.management.domain.repository.RuleCondRelEntityRepository;
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
 * 规则关联条件 管理 API
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
@RestController("ruleCondRelEntityController.v1")
@RequestMapping("/v1/{organizationId}/o2re-rule-cond-rel-entitys")
public class RuleCondRelEntityController extends BaseController {

    private final RuleCondRelEntityRepository ruleCondRelEntityRepository;
    private final RuleCondRelEntityService ruleCondRelEntityService;

    public RuleCondRelEntityController(final RuleCondRelEntityRepository ruleCondRelEntityRepository,
                                       final RuleCondRelEntityService ruleCondRelEntityService) {
        this.ruleCondRelEntityRepository = ruleCondRelEntityRepository;
        this.ruleCondRelEntityService = ruleCondRelEntityService;
    }

    @ApiOperation(value = "规则关联条件维护-分页查询规则关联条件列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<RuleCondRelEntity>> page(@PathVariable(value = "organizationId") Long organizationId,
                                                        RuleCondRelEntity ruleCondRelEntity,
                                                        @ApiIgnore @SortDefault(value = RuleCondRelEntity.FIELD_RULE_COND_REL_ENTITY_ID,
                                                                direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<RuleCondRelEntity> list = ruleCondRelEntityRepository.pageAndSort(pageRequest, ruleCondRelEntity);
        return Results.success(list);
    }

    @ApiOperation(value = "规则关联条件维护-查询规则关联条件明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{ruleCondRelEntityId}")
    public ResponseEntity<RuleCondRelEntity> detail(@PathVariable(value = "organizationId") Long organizationId,
                                                    @ApiParam(value = "规则关联条件ID", required = true) @PathVariable Long ruleCondRelEntityId) {
        RuleCondRelEntity ruleCondRelEntity = ruleCondRelEntityRepository.selectByPrimaryKey(ruleCondRelEntityId);
        return Results.success(ruleCondRelEntity);
    }

    @ApiOperation(value = "规则关联条件维护-创建规则关联条件")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<RuleCondRelEntity> create(@PathVariable(value = "organizationId") Long organizationId,
                                                    @RequestBody RuleCondRelEntity ruleCondRelEntity) {
        validObject(ruleCondRelEntity);
        ruleCondRelEntityService.save(ruleCondRelEntity);
        return Results.success(ruleCondRelEntity);
    }

    @ApiOperation(value = "规则关联条件维护-修改规则关联条件")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<RuleCondRelEntity> update(@PathVariable(value = "organizationId") Long organizationId,
                                                    @RequestBody RuleCondRelEntity ruleCondRelEntity) {
        SecurityTokenHelper.validToken(ruleCondRelEntity);
        ruleCondRelEntityService.save(ruleCondRelEntity);
        return Results.success(ruleCondRelEntity);
    }

    @ApiOperation(value = "规则关联条件维护-批量保存规则关联条件")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/batch-saving")
    public ResponseEntity<List<RuleCondRelEntity>> batchSave(@PathVariable(value = "organizationId") Long organizationId,
                                                             @RequestBody List<RuleCondRelEntity> ruleCondRelEntityList) {
        SecurityTokenHelper.validToken(ruleCondRelEntityList);
        ruleCondRelEntityService.batchSave(ruleCondRelEntityList);
        return Results.success(ruleCondRelEntityList);
    }

    @ApiOperation(value = "规则关联条件维护-删除规则关联条件")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<Void> remove(@PathVariable(value = "organizationId") Long organizationId,
                                       @RequestBody RuleCondRelEntity ruleCondRelEntity) {
        SecurityTokenHelper.validToken(ruleCondRelEntity);
        ruleCondRelEntityRepository.deleteByPrimaryKey(ruleCondRelEntity);
        return Results.success();
    }

}
