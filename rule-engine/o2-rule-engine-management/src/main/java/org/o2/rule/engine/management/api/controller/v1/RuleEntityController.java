package org.o2.rule.engine.management.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.rule.engine.management.app.service.RuleEntityService;
import org.o2.rule.engine.management.domain.entity.RuleEntity;
import org.o2.rule.engine.management.domain.repository.RuleEntityRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 规则实体 管理 API
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
@RestController("ruleEntityController.v1")
@RequestMapping("/v1/{organizationId}/o2re-rule-entitys")
public class RuleEntityController extends BaseController {

    private final RuleEntityRepository ruleEntityRepository;
    private final RuleEntityService ruleEntityService;

    public RuleEntityController(final RuleEntityRepository ruleEntityRepository,
                                final RuleEntityService ruleEntityService) {
        this.ruleEntityRepository = ruleEntityRepository;
        this.ruleEntityService = ruleEntityService;
    }

    @ApiOperation(value = "规则实体维护-分页查询规则实体列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<RuleEntity>> page(@PathVariable(value = "organizationId") Long organizationId,
                                                 RuleEntity ruleEntity,
                                                 @ApiIgnore @SortDefault(value = RuleEntity.FIELD_RULE_ENTITY_ID,
                                                         direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ruleEntity.setTenantId(organizationId);
        Page<RuleEntity> list = PageHelper.doPageAndSort(pageRequest, () -> ruleEntityRepository.selectList(ruleEntity));
        return Results.success(list);
    }

    @ApiOperation(value = "规则实体维护-查询规则实体明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{ruleEntityId}")
    public ResponseEntity<RuleEntity> detail(@PathVariable(value = "organizationId") Long organizationId,
                                             @ApiParam(value = "规则实体ID", required = true) @PathVariable Long ruleEntityId) {
        RuleEntity ruleEntity = ruleEntityRepository.selectByPrimaryKey(ruleEntityId);
        return Results.success(ruleEntity);
    }

    @ApiOperation(value = "规则实体维护-创建规则实体")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<RuleEntity> create(@PathVariable(value = "organizationId") Long organizationId,
                                             @RequestBody RuleEntity ruleEntity) {
        ruleEntity.setTenantId(organizationId);
        validObject(ruleEntity);
        ruleEntityService.save(ruleEntity);
        return Results.success(ruleEntity);
    }

    @ApiOperation(value = "规则实体维护-修改规则实体")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<RuleEntity> update(@PathVariable(value = "organizationId") Long organizationId,
                                             @RequestBody RuleEntity ruleEntity) {
        ruleEntity.setTenantId(organizationId);
        SecurityTokenHelper.validToken(ruleEntity);
        ruleEntityService.save(ruleEntity);
        return Results.success(ruleEntity);
    }
}
