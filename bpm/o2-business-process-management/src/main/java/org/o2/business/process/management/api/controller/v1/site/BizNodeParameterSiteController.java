package org.o2.business.process.management.api.controller.v1.site;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.o2.business.process.management.api.dto.BatchBusinessNodeQueryDTO;
import org.o2.business.process.management.app.service.BizNodeParameterService;
import org.o2.business.process.management.config.BusinessProcessManagerAutoConfiguration;
import org.o2.business.process.management.domain.entity.BizNodeParameter;
import org.o2.business.process.management.domain.repository.BizNodeParameterRepository;
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
 * 业务节点参数表 管理 API（站点层）
 *
 * @author chao.yang05@hand-china.com 2023-05-22
 */
@RestController("bizNodeParameterSiteController.v1")
@Api(tags = BusinessProcessManagerAutoConfiguration.BUSINESS_PROCESS_NODE_PARAMETER_SITE)
@RequestMapping("/v1/biz-node-parameters")
public class BizNodeParameterSiteController extends BaseController {

    private final BizNodeParameterRepository bizNodeParameterRepository;
    private final BizNodeParameterService bizNodeParameterService;

    public BizNodeParameterSiteController(BizNodeParameterRepository bizNodeParameterRepository, BizNodeParameterService bizNodeParameterService) {
        this.bizNodeParameterRepository = bizNodeParameterRepository;
        this.bizNodeParameterService = bizNodeParameterService;
    }

    @ApiOperation(value = "业务节点参数表维护-分页查询业务节点参数表列表（站点层）")
    @Permission(level = ResourceLevel.SITE)
    @ProcessLovValue(targetField = {BaseConstants.FIELD_BODY})
    @GetMapping
    public ResponseEntity<Page<BizNodeParameter>> page(BizNodeParameter bizNodeParameter,
                                                       @ApiIgnore @SortDefault(value = BizNodeParameter.FIELD_BIZ_NODE_PARAMETER_ID,
                                                               direction = Sort.Direction.DESC) PageRequest pageRequest) {
        bizNodeParameter.setTenantId(BaseConstants.DEFAULT_TENANT_ID);
        Page<BizNodeParameter> list = bizNodeParameterRepository.pageAndSort(pageRequest, bizNodeParameter);
        return Results.success(list);
    }

    @ApiOperation(value = "业务节点参数表维护-查询业务节点参数表明细（站点层）")
    @Permission(level = ResourceLevel.SITE)
    @ProcessLovValue(targetField = {BaseConstants.FIELD_BODY})
    @GetMapping("/{bizNodeParameterId}")
    public ResponseEntity<BizNodeParameter> detail(@ApiParam(value = "业务节点参数表ID", required = true) @PathVariable Long bizNodeParameterId) {
        BizNodeParameter bizNodeParameter = bizNodeParameterRepository.selectByPrimaryKey(bizNodeParameterId);
        return Results.success(bizNodeParameter);
    }

    /**
     * @deprecated 前端没有调用
     */
    @Deprecated
    @ApiOperation(value = "业务节点参数表维护-根据beanId查询节点参数信息（站点层）")
    @Permission(level = ResourceLevel.SITE)
    @ProcessLovValue(targetField = {BaseConstants.FIELD_BODY})
    @PostMapping("list-by-bean-Id")
    public ResponseEntity<List<BizNodeParameter>> listByBeanId(@RequestBody BatchBusinessNodeQueryDTO batchBusinessNodeQueryDTO) {
        validObject(batchBusinessNodeQueryDTO);
        return Results.success(bizNodeParameterService.getBizNodeParameterList(batchBusinessNodeQueryDTO.getBeanIdList(), BaseConstants.DEFAULT_TENANT_ID));
    }

    @ApiOperation(value = "业务节点参数表维护-创建业务节点参数表（站点层）")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping
    public ResponseEntity<List<BizNodeParameter>> batchCreate(@RequestBody List<BizNodeParameter> bizNodeParameterList) {
        bizNodeParameterList.forEach(v -> v.setTenantId(BaseConstants.DEFAULT_TENANT_ID));
        validList(bizNodeParameterList);
        bizNodeParameterService.batchSave(bizNodeParameterList, true);
        return Results.success(bizNodeParameterList);
    }

    @ApiOperation(value = "业务节点参数表维护-修改业务节点参数表（站点层）")
    @Permission(level = ResourceLevel.SITE)
    @PutMapping
    public ResponseEntity<List<BizNodeParameter>> batchUpdate(@RequestBody List<BizNodeParameter> bizNodeParameterList) {
        validList(bizNodeParameterList);
        bizNodeParameterService.batchSave(bizNodeParameterList, false);
        return Results.success(bizNodeParameterList);
    }
}
