package org.o2.rule.engine.management.api.controller.v1;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.o2.rule.engine.management.app.service.RuleService;
import org.o2.rule.engine.management.domain.repository.RuleRepository;
import org.o2.rule.engine.management.domain.vo.RuleVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;

/**
 * 规则内部接口 管理 API
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
@RestController("ruleInternalController.v1")
@RequestMapping("/v1/{organizationId}/o2re-rules-internal")
public class RuleInternalController extends BaseController {

    private final RuleRepository ruleRepository;
    private final RuleService ruleService;

    public RuleInternalController(final RuleRepository ruleRepository,
                                  final RuleService ruleService) {
        this.ruleRepository = ruleRepository;
        this.ruleService = ruleService;
    }

    @ApiOperation(value = "规则维护-通过编码查询规则明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{ruleCode}")
    public ResponseEntity<RuleVO> detailByCode(@PathVariable(value = "organizationId") Long organizationId,
                                               @ApiParam(value = "规则ID", required = true) @PathVariable String ruleCode) {
        return Results.success(ruleService.detailByCode(organizationId, ruleCode));
    }

}
