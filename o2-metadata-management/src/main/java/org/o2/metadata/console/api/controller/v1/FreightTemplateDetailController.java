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
import org.o2.core.response.OperateResponse;
import org.o2.metadata.console.app.service.FreightTemplateDetailService;
import org.o2.metadata.console.infra.config.MetadataManagementAutoConfiguration;
import org.o2.metadata.console.infra.entity.FreightTemplateDetail;
import org.o2.metadata.console.infra.repository.FreightTemplateDetailRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 运费模板明细管理API
 *
 * @author peng.xu@hand-china.com 2019/5/17
 */
@RestController("freightTemplateDetailController.v1")
@RequestMapping("/v1/{organizationId}/freight-template-details")
@Api(tags = MetadataManagementAutoConfiguration.FREIGHT_TEMPLATE_DETAIL)
public class FreightTemplateDetailController extends BaseController {
    private final FreightTemplateDetailRepository freightTemplateDetailRepository;
    private final FreightTemplateDetailService freightTemplateDetailService;

    public FreightTemplateDetailController(final FreightTemplateDetailRepository freightTemplateDetailRepository, final FreightTemplateDetailService freightTemplateDetailService) {
        this.freightTemplateDetailRepository = freightTemplateDetailRepository;
        this.freightTemplateDetailService = freightTemplateDetailService;
    }

    @ApiOperation(value = "查询默认运费模板明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping("/default/{templateId}")
    public ResponseEntity<Page<FreightTemplateDetail>> queryDefaultFreightTemplateDetail(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @PathVariable final Long templateId, final PageRequest pageRequest) {
        final Page<FreightTemplateDetail> list = PageHelper.doPage(pageRequest.getPage(), pageRequest.getSize(),
                () -> freightTemplateDetailService.queryDefaultFreightTemplateDetail(templateId));
        return Results.success(list);
    }

    @ApiOperation(value = "查询区域运费模板明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping("/region/{templateId}")
    public ResponseEntity<Page<FreightTemplateDetail>> queryRegionFreightTemplateDetail(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @PathVariable final Long templateId, final PageRequest pageRequest) {
        final Page<FreightTemplateDetail> list = PageHelper.doPage(pageRequest.getPage(), pageRequest.getSize(),
                () -> freightTemplateDetailService.queryRegionFreightTemplateDetail(templateId));
        return Results.success(list);
    }

    @ApiOperation(value = "根据ID查询运费模板明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping("/{templateDetailId}")
    public ResponseEntity<FreightTemplateDetail> detail(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @PathVariable final Long templateDetailId) {
        final FreightTemplateDetail freightTemplateDetail = freightTemplateDetailRepository.selectByPrimaryKey(templateDetailId);
        return Results.success(freightTemplateDetail);
    }

    @ApiOperation(value = "批量删除运费模板明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<OperateResponse> remove(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody final List<FreightTemplateDetail> freightTemplateDetailList) {
        freightTemplateDetailService.batchDelete(freightTemplateDetailList);
        return Results.success(OperateResponse.success());
    }

}
