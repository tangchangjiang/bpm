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
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.rule.engine.management.app.service.RuleEntityConditionService;
import org.o2.rule.engine.management.domain.entity.RuleEntityCondition;
import org.o2.rule.engine.management.domain.repository.RuleEntityConditionRepository;
import org.springframework.http.ResponseEntity;
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
 * 规则实体条件 管理 API(站点层)
 *
 * @author chao.yang05@hand-china.com 2023-05-22
 */
@RestController("ruleEntityConditionSiteController.v1")
@RequestMapping("/v1/o2re-rule-entity-conditions")
public class RuleEntityConditionSiteController extends BaseController {

    private final RuleEntityConditionRepository ruleEntityConditionRepository;
    private final RuleEntityConditionService ruleEntityConditionService;

    public RuleEntityConditionSiteController(final RuleEntityConditionRepository ruleEntityConditionRepository,
                                         final RuleEntityConditionService ruleEntityConditionService) {
        this.ruleEntityConditionRepository = ruleEntityConditionRepository;
        this.ruleEntityConditionService = ruleEntityConditionService;
    }

    @ApiOperation(value = "规则实体条件维护-分页查询规则实体条件列表（站点层）")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping
    public ResponseEntity<Page<RuleEntityCondition>> page(RuleEntityCondition ruleEntityCondition,
                                                          @ApiIgnore @SortDefault(value = RuleEntityCondition.FIELD_ORDER_SEQ,
                                                                  direction = Sort.Direction.ASC) PageRequest pageRequest) {
        // 通过规则实体Id查询（ruleEntityId）
        Page<RuleEntityCondition> list = PageHelper.doPage(pageRequest, () -> ruleEntityConditionRepository.selectList(ruleEntityCondition));
        return Results.success(list);
    }

    @ApiOperation(value = "规则实体条件维护-不分页查询规则实体条件列表（站点层）")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping("/list/{ruleEntityCode}")
    public ResponseEntity<List<RuleEntityCondition>> list(@PathVariable(value = "ruleEntityCode") String ruleEntityCode,
                                                          RuleEntityCondition ruleEntityCondition) {
        return Results.success(ruleEntityConditionRepository.selectListByRuleEntityCode(BaseConstants.DEFAULT_TENANT_ID, ruleEntityCode, ruleEntityCondition));
    }

    @ApiOperation(value = "规则实体条件维护-查询规则实体条件明细（站点层）")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping("/{ruleEntityConditionId}")
    public ResponseEntity<RuleEntityCondition> detail(@ApiParam(value = "规则实体条件ID", required = true) @PathVariable Long ruleEntityConditionId) {
        RuleEntityCondition ruleEntityCondition = ruleEntityConditionRepository.selectByPrimaryKey(ruleEntityConditionId);
        return Results.success(ruleEntityCondition);
    }

    @ApiOperation(value = "规则实体条件维护-创建规则实体条件（站点层）")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping
    public ResponseEntity<RuleEntityCondition> create(@RequestBody RuleEntityCondition ruleEntityCondition) {
        ruleEntityCondition.setTenantId(BaseConstants.DEFAULT_TENANT_ID);
        validObject(ruleEntityCondition);
        ruleEntityConditionService.save(ruleEntityCondition);
        return Results.success(ruleEntityCondition);
    }

    @ApiOperation(value = "规则实体条件维护-修改规则实体条件（站点层）")
    @Permission(level = ResourceLevel.SITE)
    @PutMapping
    public ResponseEntity<RuleEntityCondition> update(@RequestBody RuleEntityCondition ruleEntityCondition) {
        ruleEntityCondition.setTenantId(BaseConstants.DEFAULT_TENANT_ID);
        SecurityTokenHelper.validToken(ruleEntityCondition);
        ruleEntityConditionService.save(ruleEntityCondition);
        return Results.success(ruleEntityCondition);
    }
}
