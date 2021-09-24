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
import org.o2.metadata.console.api.vo.FreightTemplateManagementVO;
import org.o2.metadata.console.app.service.FreightTemplateService;
import org.o2.metadata.console.infra.config.MetadataManagementAutoConfiguration;
import org.o2.metadata.console.infra.entity.FreightTemplate;
import org.o2.metadata.console.infra.repository.FreightTemplateRepository;
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
@Api(tags = MetadataManagementAutoConfiguration.FREIGHT_TEMPLATE)
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
    public ResponseEntity<Page<FreightTemplate>> list(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, final FreightTemplate freightTemplate, final PageRequest pageRequest) {
        //默认查询生效的
        freightTemplate.setActiveFlag(1);

        final Page<FreightTemplate> list = PageHelper.doPage(pageRequest.getPage(), pageRequest.getSize(),
                () -> freightTemplateRepository.listFreightTemplates(freightTemplate));
        freightTemplateService.tranLov(list.getContent(),organizationId);
        return Results.success(list);
    }

    @ApiOperation(value = "根据主键查询运费模板和运费模板明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = {BaseConstants.FIELD_BODY,
            BaseConstants.FIELD_BODY + "." + FreightTemplateManagementVO.FIELD_DEFAULT_FREIGHT_TEMPLATE_DETAILS,
            BaseConstants.FIELD_BODY + "." + FreightTemplateManagementVO.FIELD_REGION_FREIGHT_DETAIL_DISPLAY_LIST,
            BaseConstants.FIELD_BODY + "." + FreightTemplateManagementVO.FIELD_REGION_FREIGHT_TEMPLATE_DETAILS})
    @GetMapping("/{templateId}")
    public ResponseEntity<FreightTemplateManagementVO> detail(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @PathVariable final Long templateId) {
        final FreightTemplateManagementVO freightTemplate = freightTemplateService.queryTemplateAndDetails(templateId,organizationId);
        return Results.success(freightTemplate);
    }

    @ApiOperation(value = "新增运费模板和运费模板明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<FreightTemplate> create(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody final FreightTemplateManagementVO freightTemplateManagementVO) {
        final FreightTemplate insert = freightTemplateService.createTemplateAndDetails(freightTemplateManagementVO);
        return Results.success(insert);
    }

    @ApiOperation(value = "修改运费模板和运费模板明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<FreightTemplate> update(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody final FreightTemplateManagementVO freightTemplateManagementVO) {
        SecurityTokenHelper.validToken(freightTemplateManagementVO);
        final FreightTemplate update = freightTemplateService.updateTemplateAndDetails(freightTemplateManagementVO);
        return Results.success(update);
    }

    @ApiOperation(value = "删除运费模板和运费模板明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<Void> remove(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody final List<FreightTemplate> freightTemplateList) {
        freightTemplateService.removeTemplateAndDetails(freightTemplateList,organizationId);
        return Results.success();
    }

    @ApiOperation(value = "设置默认模板")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping("/setDefaultTemp")
    public ResponseEntity<Void> setDefaultTemp(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody final FreightTemplate freightTemplate) {
        freightTemplateService.setDefaultTemp(organizationId,freightTemplate.getTemplateId());
        return Results.success();
    }

    @ApiOperation(value = "获取默认的运费模板及其明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = {BaseConstants.FIELD_BODY,
            BaseConstants.FIELD_BODY + "." + FreightTemplateManagementVO.FIELD_DEFAULT_FREIGHT_TEMPLATE_DETAILS,
            BaseConstants.FIELD_BODY + "." + FreightTemplateManagementVO.FIELD_REGION_FREIGHT_TEMPLATE_DETAILS})
    @GetMapping("/defaultTemplate")
    public ResponseEntity<FreightTemplateManagementVO> querydefaultTemplate(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId) {
        final FreightTemplateManagementVO freightTemplate = freightTemplateService.queryDefaultTemplateDetail(organizationId);
        return Results.success(freightTemplate);
    }

}
