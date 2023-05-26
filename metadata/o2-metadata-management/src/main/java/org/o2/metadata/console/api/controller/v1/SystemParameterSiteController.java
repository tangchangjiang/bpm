package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.annotation.annotation.ProcessAnnotationValue;
import org.o2.core.response.OperateResponse;
import org.o2.metadata.console.app.service.SysParamService;
import org.o2.metadata.console.infra.config.MetadataManagementAutoConfiguration;
import org.o2.metadata.console.infra.entity.SystemParameter;
import org.o2.metadata.console.infra.repository.SystemParameterRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 系统参数 管理 API 站点级
 *
 * @author chao.yang05@hand-china.com 2023-04-12
 */
@Slf4j
@RestController("systemParameterSiteController.v1")
@RequestMapping("/v1/system-parameters")
@Api(tags = MetadataManagementAutoConfiguration.SYSTEM_PARAMETER_SITE)
public class SystemParameterSiteController extends BaseController {

    private final SystemParameterRepository systemParameterRepository;
    private final SysParamService sysParamService;

    public SystemParameterSiteController(SystemParameterRepository systemParameterRepository,
                                         SysParamService sysParamService) {
        this.systemParameterRepository = systemParameterRepository;
        this.sysParamService = sysParamService;
    }

    @ApiOperation(value = "系统参数（平台层）")
    @Permission(level = ResourceLevel.SITE)
    @ProcessLovValue(targetField = {BaseConstants.FIELD_BODY})
    @GetMapping("/find")
    public ResponseEntity<SystemParameter> findOne(SystemParameter systemParameter) {
        return Results.success(systemParameterRepository.findOne(systemParameter));
    }

    @ApiOperation(value = "系统参数列表（平台层）")
    @Permission(level = ResourceLevel.SITE)
    @ProcessLovValue(targetField = {BaseConstants.FIELD_BODY})
    @ProcessAnnotationValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping
    public ResponseEntity<Page<SystemParameter>> list(SystemParameter systemParameter, @ApiIgnore @SortDefault(value = SystemParameter.FIELD_PARAM_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<SystemParameter> list = PageHelper.doPageAndSort(pageRequest,
                () -> systemParameterRepository.fuzzyQuery(systemParameter, systemParameter.getTenantId(), BaseConstants.Flag.YES));
        return Results.success(list);
    }

    @ApiOperation(value = "系统参数明细（平台层）")
    @Permission(level = ResourceLevel.SITE)
    @ProcessLovValue(targetField = {BaseConstants.FIELD_BODY})
    @GetMapping("/{paramId}")
    public ResponseEntity<SystemParameter> detail(@PathVariable Long paramId) {
        SystemParameter condition = new SystemParameter();
        condition.setTenantId(BaseConstants.DEFAULT_TENANT_ID);
        condition.setParamId(paramId);
        SystemParameter systemParameter = systemParameterRepository.findOne(condition);
        return Results.success(systemParameter);
    }

    @ApiOperation(value = "创建系统参数（平台层）")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping
    public ResponseEntity<SystemParameter> saveSystemParameter(@RequestBody SystemParameter systemParameter) {
        systemParameter.setTenantId(BaseConstants.DEFAULT_TENANT_ID);
        this.validObject(systemParameter);
        sysParamService.saveSystemParameter(systemParameter, BaseConstants.DEFAULT_TENANT_ID);
        return Results.success(systemParameter);
    }

    @ApiOperation(value = "修改系统参数（平台层）")
    @Permission(level = ResourceLevel.SITE)
    @PutMapping
    public ResponseEntity<SystemParameter> updateSystemParameter(@RequestBody SystemParameter systemParameter) {
        systemParameter.setTenantId(BaseConstants.DEFAULT_TENANT_ID);
        this.validObject(systemParameter);
        SecurityTokenHelper.validToken(systemParameter);
        sysParamService.updateSystemParameter(systemParameter, BaseConstants.DEFAULT_TENANT_ID);
        return Results.success(systemParameter);
    }

    @ApiOperation(value = "删除系统参数（平台层）")
    @Permission(level = ResourceLevel.SITE)
    @DeleteMapping
    public ResponseEntity<OperateResponse> removeSystemParameter(@RequestBody SystemParameter systemParameter) {
        systemParameter.setTenantId(BaseConstants.DEFAULT_TENANT_ID);
        SecurityTokenHelper.validToken(systemParameter);
        systemParameterRepository.deleteByPrimaryKey(systemParameter);
        return Results.success(OperateResponse.success());
    }
}
