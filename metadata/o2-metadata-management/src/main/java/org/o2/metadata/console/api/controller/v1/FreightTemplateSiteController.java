package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.o2.annotation.annotation.ProcessAnnotationValue;
import org.o2.metadata.console.api.vo.FreightTemplateManagementVO;
import org.o2.metadata.console.app.service.FreightTemplateService;
import org.o2.metadata.console.infra.config.MetadataManagementAutoConfiguration;
import org.o2.metadata.console.infra.entity.FreightTemplate;
import org.o2.metadata.console.infra.repository.FreightTemplateRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 运费模板管理API 站点级
 *
 * @author chao.yang05@hand-china.com 2023-04-14
 */
@RestController("freightTemplateSiteController.v1")
@RequestMapping("/v1/freight-templates")
@Api(tags = MetadataManagementAutoConfiguration.FREIGHT_TEMPLATE_SITE)
public class FreightTemplateSiteController extends BaseController {

    private final FreightTemplateRepository freightTemplateRepository;
    private final FreightTemplateService freightTemplateService;

    public FreightTemplateSiteController(final FreightTemplateRepository freightTemplateRepository, final FreightTemplateService freightTemplateService) {
        this.freightTemplateRepository = freightTemplateRepository;
        this.freightTemplateService = freightTemplateService;
    }

    @ApiOperation(value = "运费模板列表（站点级）")
    @Permission(level = ResourceLevel.SITE)
    @ProcessAnnotationValue(targetField = BaseConstants.FIELD_BODY)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping
    public ResponseEntity<Page<FreightTemplate>> list(final FreightTemplate freightTemplate, final PageRequest pageRequest) {
        //默认查询生效的
        freightTemplate.setActiveFlag(1);
        freightTemplate.setSiteFlag(BaseConstants.Flag.YES);

        final Page<FreightTemplate> list = PageHelper.doPage(pageRequest.getPage(), pageRequest.getSize(),
                () -> freightTemplateRepository.listFreightTemplates(freightTemplate));
        // 平台层查询全租户的数据，需要根据租户分组，查询对应租户的值集进行翻译
        List<FreightTemplate> templateList = list.getContent();
        Map<Long, List<FreightTemplate>> templateGroupByTenant = templateList.stream().collect(Collectors.groupingBy(FreightTemplate::getTenantId));
        templateGroupByTenant.forEach((tenant, templates) -> freightTemplateService.tranLov(templates, tenant));
        return Results.success(list);
    }

    @ApiOperation(value = "根据主键查询运费模板和运费模板明细（站点级）")
    @Permission(level = ResourceLevel.SITE)
    @ProcessAnnotationValue(targetField = BaseConstants.FIELD_BODY)
    @ProcessLovValue(targetField = {BaseConstants.FIELD_BODY,
            BaseConstants.FIELD_BODY + "." + FreightTemplateManagementVO.FIELD_DEFAULT_FREIGHT_TEMPLATE_DETAILS,
            BaseConstants.FIELD_BODY + "." + FreightTemplateManagementVO.FIELD_REGION_FREIGHT_DETAIL_DISPLAY_LIST,
            BaseConstants.FIELD_BODY + "." + FreightTemplateManagementVO.FIELD_REGION_FREIGHT_TEMPLATE_DETAILS})
    @GetMapping("/{templateId}")
    public ResponseEntity<FreightTemplateManagementVO> detail(@PathVariable final Long templateId, Long tenantId) {
        final FreightTemplateManagementVO freightTemplate = freightTemplateService.queryTemplateAndDetails(templateId, tenantId);
        return Results.success(freightTemplate);
    }

}
