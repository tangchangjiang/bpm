package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.console.app.service.FreightTemplateService;
import org.o2.metadata.console.config.EnableMetadataConsole;
import org.o2.metadata.core.domain.entity.FreightTemplate;
import org.o2.metadata.core.domain.repository.FreightTemplateRepository;
import org.o2.metadata.core.domain.vo.FreightTemplateVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 运费模板管理API
 *
 * @author peng.xu@hand-china.com 2019/5/16
 */
@RestController("freightTemplateController.v1")
@RequestMapping("/v1/{organizationId}/freight-templates")
@Api(tags = EnableMetadataConsole.FREIGHT_TEMPLATE)
public class FreightTemplateController extends BaseController {
    private final FreightTemplateRepository freightTemplateRepository;
    private final FreightTemplateService freightTemplateService;

    public FreightTemplateController(final FreightTemplateRepository freightTemplateRepository, final FreightTemplateService freightTemplateService) {
        this.freightTemplateRepository = freightTemplateRepository;
        this.freightTemplateService = freightTemplateService;
    }

    @ApiOperation(value = "运费模板列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping
    public ResponseEntity<?> list(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, final FreightTemplate freightTemplate, final PageRequest pageRequest) {
        freightTemplate.setActiveFlag(1); //默认查询生效的

        final Page<FreightTemplate> list = PageHelper.doPage(pageRequest.getPage(), pageRequest.getSize(),
                () -> freightTemplateRepository.listFreightTemplates(freightTemplate));
        freightTemplateService.tranLov(list.getContent(),organizationId);
        return Results.success(list);
    }

    @ApiOperation(value = "根据主键查询运费模板和运费模板明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = {BaseConstants.FIELD_BODY,
            BaseConstants.FIELD_BODY + "." + FreightTemplateVO.FIELD_DEFAULT_FREIGHT_TEMPLATE_DETAILS,
            BaseConstants.FIELD_BODY + "." + FreightTemplateVO.FIELD_REGION_FREIGHT_DETAIL_DISPLAY_LIST,
            BaseConstants.FIELD_BODY + "." + FreightTemplateVO.FIELD_REGION_FREIGHT_TEMPLATE_DETAILS})
    @GetMapping("/{templateId}")
    public ResponseEntity<?> detail(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @PathVariable final Long templateId) {
        final FreightTemplateVO freightTemplate = freightTemplateService.queryTemplateAndDetails(templateId,organizationId);
        return Results.success(freightTemplate);
    }

    @ApiOperation(value = "新增运费模板和运费模板明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<?> create(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody final FreightTemplateVO freightTemplateVO) {
        final FreightTemplate insert = freightTemplateService.createTemplateAndDetails(freightTemplateVO);
        return Results.success(insert);
    }

    @ApiOperation(value = "修改运费模板和运费模板明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<?> update(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody final FreightTemplateVO freightTemplateVO) {
        SecurityTokenHelper.validToken(freightTemplateVO);
        final FreightTemplate update = freightTemplateService.updateTemplateAndDetails(freightTemplateVO);
        return Results.success(update);
    }

    @ApiOperation(value = "删除运费模板和运费模板明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody final List<FreightTemplate> freightTemplateList) {
        freightTemplateService.removeTemplateAndDetails(freightTemplateList);
        return Results.success();
    }

    @ApiOperation(value = "更新运费模板redis缓存")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/refresh-cache")
    public ResponseEntity<?> refreshCache(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody final Long templateId) {
        freightTemplateService.refreshCache(templateId);
        return Results.success();
    }

    @ApiOperation(value = "设置默认模板")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/setDefaultTemp")
    public ResponseEntity<?> setDefaultTemp(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody final Long templateId) {
        freightTemplateService.setDefaultTemp(organizationId,templateId);
        return Results.success();
    }

    @ApiOperation(value = "获取默认的运费模板及其明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = {BaseConstants.FIELD_BODY,
            BaseConstants.FIELD_BODY + "." + FreightTemplateVO.FIELD_DEFAULT_FREIGHT_TEMPLATE_DETAILS,
            BaseConstants.FIELD_BODY + "." + FreightTemplateVO.FIELD_REGION_FREIGHT_TEMPLATE_DETAILS})
    @GetMapping("/defaultTemplate")
    public ResponseEntity<?> querydefaultTemplate(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId) {
        final FreightTemplateVO freightTemplate = freightTemplateService.querydefaultTemplate(organizationId);
        return Results.success(freightTemplate);
    }

}
