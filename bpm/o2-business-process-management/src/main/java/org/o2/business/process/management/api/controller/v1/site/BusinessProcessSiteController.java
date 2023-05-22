package org.o2.business.process.management.api.controller.v1.site;

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
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.business.process.management.api.dto.BusinessExportDTO;
import org.o2.business.process.management.api.dto.BusinessProcessQueryDTO;
import org.o2.business.process.management.api.vo.BusinessExportVO;
import org.o2.business.process.management.app.service.BusinessProcessService;
import org.o2.business.process.management.domain.entity.BusinessProcess;
import org.o2.business.process.management.domain.repository.BusinessProcessRedisRepository;
import org.o2.business.process.management.domain.repository.BusinessProcessRepository;
import org.o2.core.helper.QueryFallbackHelper;
import org.o2.user.helper.IamUserHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 业务流程定义表 管理 API（站点层）
 *
 * @author chao.yang05@hand-china.com 2023-05-22
 */
@RestController("businessProcessSiteController.v1")
@RequestMapping("/v1/business-process")
public class BusinessProcessSiteController extends BaseController {

    private final BusinessProcessRepository businessProcessRepository;
    private final BusinessProcessService businessProcessService;
    private final BusinessProcessRedisRepository businessProcessRedisRepository;

    public BusinessProcessSiteController(BusinessProcessRepository businessProcessRepository,
                                         BusinessProcessService businessProcessService,
                                         BusinessProcessRedisRepository businessProcessRedisRepository) {
        this.businessProcessRepository = businessProcessRepository;
        this.businessProcessService = businessProcessService;
        this.businessProcessRedisRepository = businessProcessRedisRepository;
    }

    @ApiOperation(value = "业务流程定义表维护-分页查询业务流程定义表列表（站点层）")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping
    public ResponseEntity<Page<BusinessProcess>> page(BusinessProcessQueryDTO businessProcess,
                                                      @ApiIgnore @SortDefault(value = BusinessProcess.FIELD_BIZ_PROCESS_ID,
                                                              direction = Sort.Direction.DESC) PageRequest pageRequest) {
        businessProcess.setTenantId(BaseConstants.DEFAULT_TENANT_ID);
        Page<BusinessProcess> list = PageHelper.doPageAndSort(pageRequest, () -> businessProcessService.listBusinessProcess(businessProcess));
        return Results.success(list);
    }

    @ApiOperation(value = "业务流程定义表维护-查询业务流程定义表明细（站点层）")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping("/{bizProcessId}")
    public ResponseEntity<BusinessProcess> detail(@ApiParam(value = "业务流程定义表ID", required = true) @PathVariable Long bizProcessId) {
        BusinessProcess businessProcess = businessProcessRepository.selectByPrimaryKey(bizProcessId);
        businessProcess.setCreatedOperator(IamUserHelper.getRealName(businessProcess.getCreatedBy().toString()));
        businessProcess.setUpdatedOperator(IamUserHelper.getRealName(businessProcess.getLastUpdatedBy().toString()));
        return Results.success(businessProcess);
    }

    @ApiOperation(value = "业务流程定义表维护-创建业务流程定义表（站点层）")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping
    public ResponseEntity<BusinessProcess> create(@RequestBody BusinessProcess businessProcess) {
        businessProcess.setTenantId(BaseConstants.DEFAULT_TENANT_ID);
        validObject(businessProcess);
        businessProcessService.save(businessProcess);
        return Results.success(businessProcess);
    }

    @ApiOperation(value = "业务流程定义表维护-修改业务流程定义表（站点层）")
    @Permission(level = ResourceLevel.SITE)
    @PutMapping
    public ResponseEntity<BusinessProcess> update(@RequestBody BusinessProcess businessProcess) {
        businessProcess.setTenantId(BaseConstants.DEFAULT_TENANT_ID);
        SecurityTokenHelper.validToken(businessProcess);
        businessProcessService.save(businessProcess);
        return Results.success(businessProcess);
    }

    @ApiOperation(value = "业务流程定义缓存详情（站点层）")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping("/process-config/{processCode}")
    public ResponseEntity<String> getBusinessProcessConfig(@PathVariable String processCode) {
        return Results.success(QueryFallbackHelper.siteFallback(BaseConstants.DEFAULT_TENANT_ID, tenantId -> businessProcessRedisRepository.getBusinessProcessConfig(processCode, tenantId)));
    }

    @ApiOperation(value = "业务流程定义表维护-批量保存业务流程定义表（站点层）")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping("/batch-saving")
    public ResponseEntity<List<BusinessProcess>> batchSave(@RequestBody List<BusinessProcess> businessProcessList) {
        SecurityTokenHelper.validToken(businessProcessList);
        businessProcessService.batchSave(businessProcessList);
        return Results.success(businessProcessList);
    }

    /**
     * 规则导出
     *
     * @param businessExportDTO 查询条件
     * @param pageRequest       分页参数
     * @param exportParam       导入参数
     * @param response          响应
     * @return 结果
     */
    @ApiOperation(value = "规则导出（站点层）")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping("/export")
    @ExcelExport(value = BusinessExportVO.class, fillType = "multi-sheet")
    public ResponseEntity<Page<BusinessExportVO>> export(@ApiParam(value = "租户ID", required = true)
                                                         BusinessExportDTO businessExportDTO, @ApiIgnore PageRequest pageRequest,
                                                         ExportParam exportParam, HttpServletResponse response) {
        businessExportDTO.setTenantId(BaseConstants.DEFAULT_TENANT_ID);
        Page<BusinessExportVO> page = PageHelper.doPage(pageRequest, () -> businessProcessService.businessExport(businessExportDTO));
        return Results.success(page);
    }
}
