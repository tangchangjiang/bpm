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
import org.o2.rule.engine.management.app.service.RuleParamService;
import org.o2.rule.engine.management.domain.entity.RuleParam;
import org.o2.rule.engine.management.domain.repository.RuleParamRepository;
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
 * 规则参数 管理 API(站点层)
 *
 * @author chao.yang05@hand-china.com 2023-05-22
 */
@RestController("o2reRuleParamSiteController.v1")
@RequestMapping("/v1/o2re-rule-params")
public class RuleParamSiteController extends BaseController {

    private final RuleParamRepository ruleParamRepository;
    private final RuleParamService ruleParamService;

    public RuleParamSiteController(final RuleParamRepository ruleParamRepository,
                               final RuleParamService ruleParamService) {
        this.ruleParamRepository = ruleParamRepository;
        this.ruleParamService = ruleParamService;
    }

    @ApiOperation(value = "规则参数维护-分页查询规则参数列表")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping
    public ResponseEntity<Page<RuleParam>> page(RuleParam ruleParam,
                                                @ApiIgnore @SortDefault(value = RuleParam.FIELD_ORDER_SEQ,
                                                        direction = Sort.Direction.ASC) PageRequest pageRequest) {
        // 通过参数关联实体Id查询（参数关联实体id）
        Page<RuleParam> list = PageHelper.doPage(pageRequest, () -> ruleParamRepository.selectList(ruleParam));
        return Results.success(list);
    }

    @ApiOperation(value = "规则参数维护-查询规则参数明细")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping("/{ruleParamId}")
    public ResponseEntity<RuleParam> detail(@ApiParam(value = "规则参数ID", required = true) @PathVariable Long ruleParamId) {
        RuleParam ruleParam = ruleParamRepository.selectByPrimaryKey(ruleParamId);
        return Results.success(ruleParam);
    }

    @ApiOperation(value = "规则参数维护-创建规则参数")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping
    public ResponseEntity<RuleParam> create(@RequestBody RuleParam ruleParam) {
        ruleParam.setTenantId(BaseConstants.DEFAULT_TENANT_ID);
        validObject(ruleParam);
        ruleParamService.save(ruleParam);
        return Results.success(ruleParam);
    }

    @ApiOperation(value = "规则参数维护-修改规则参数")
    @Permission(level = ResourceLevel.SITE)
    @PutMapping
    public ResponseEntity<RuleParam> update(@RequestBody RuleParam ruleParam) {
        ruleParam.setTenantId(BaseConstants.DEFAULT_TENANT_ID);
        SecurityTokenHelper.validToken(ruleParam);
        ruleParamService.save(ruleParam);
        return Results.success(ruleParam);
    }
}
