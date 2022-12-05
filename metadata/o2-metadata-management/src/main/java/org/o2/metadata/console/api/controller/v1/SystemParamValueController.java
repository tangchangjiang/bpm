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
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.o2.core.response.OperateResponse;
import org.o2.metadata.console.app.service.SystemParamValueService;
import org.o2.metadata.console.infra.config.MetadataManagementAutoConfiguration;
import org.o2.metadata.console.infra.entity.SystemParamValue;
import org.o2.metadata.console.infra.repository.SystemParamValueRepository;
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

import java.util.List;

/**
 * 系统参数值 管理 API
 *
 * @author hongyun.wang01@hand-china.com 2020-02-13
 */
@RestController("systemParamValueController.v1")
@RequestMapping("/v1/{organizationId}/system-param-values")
@Api(tags = MetadataManagementAutoConfiguration.SYSTEM_PARAMETER_VALUE)
public class SystemParamValueController extends BaseController {

    private final SystemParamValueRepository systemParamValueRepository;
    private final SystemParamValueService systemParamValueService;

    public SystemParamValueController(SystemParamValueRepository systemParamValueRepository, SystemParamValueService systemParamValueService) {
        this.systemParamValueRepository = systemParamValueRepository;
        this.systemParamValueService = systemParamValueService;
    }

    @ApiOperation(value = "系统参数值列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<SystemParamValue>> listSystemParamValues(SystemParamValue systemParamValue, @ApiIgnore @SortDefault(value = SystemParamValue.FIELD_VALUE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest, @PathVariable("organizationId") Long organizationId) {
        systemParamValue.setTenantId(organizationId);
        Page<SystemParamValue> page = PageHelper.doPage(pageRequest, () -> systemParamValueRepository.selectByCondition(Condition.builder(SystemParamValue.class)
                .andWhere(Sqls.custom().andEqualTo(SystemParamValue.FIELD_TENANT_ID, systemParamValue.getTenantId())
                        .andLike(SystemParamValue.FIELD_PARAM_VALUE, systemParamValue.getParamValue(), true)
                        .andLike(SystemParamValue.FIELD_PARAM_KEY, systemParamValue.getParamKey(), true)
                        .andEqualTo(SystemParamValue.FIELD_PARAM_ID, systemParamValue.getParamId(), true)).build()));
        return Results.success(page);
    }

    @ApiOperation(value = "系统参数值明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{valueId}")
    public ResponseEntity<SystemParamValue> getSystemParamValue(@PathVariable Long valueId, @PathVariable("organizationId") Long organizationId) {
        SystemParamValue condition = new SystemParamValue();
        condition.setTenantId(organizationId);
        condition.setValueId(valueId);
        SystemParamValue systemParamValue = systemParamValueRepository.selectOne(condition);
        return Results.success(systemParamValue);
    }

    @ApiOperation(value = "创建系统参数值")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<SystemParamValue> saveSystemParamValue(@RequestBody SystemParamValue systemParamValue, @PathVariable("organizationId") Long organizationId) {
        systemParamValue.setTenantId(organizationId);
        systemParamValueService.systemParamValueValidate(systemParamValue);
        systemParamValueService.saveSystemParamValue(systemParamValue);
        return Results.success(systemParamValue);
    }

    @ApiOperation(value = "修改系统参数值")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<SystemParamValue> updateSystemParamValue(@RequestBody SystemParamValue systemParamValue, @PathVariable("organizationId") Long organizationId) {
        systemParamValue.setTenantId(organizationId);
        SecurityTokenHelper.validToken(systemParamValue);
        systemParamValueService.systemParamValueValidate(systemParamValue);
        systemParamValueService.updateSystemParamValue(systemParamValue);
        return Results.success(systemParamValue);
    }

    @ApiOperation(value = "删除系统参数值")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<OperateResponse> removeSystemParamValue(@RequestBody SystemParamValue systemParamValue, @PathVariable("organizationId") Long organizationId) {
        systemParamValue.setTenantId(organizationId);
        SecurityTokenHelper.validToken(systemParamValue);
        systemParamValueService.removeSystemParamValue(systemParamValue);
        return Results.success(OperateResponse.success());
    }

    @ApiOperation(value = "获取KV系统参数值")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{paramCode}/KV")
    public ResponseEntity<String> getSysValueByParam(@PathVariable("paramCode") String paramCode, @PathVariable("organizationId") Long organizationId) {
        String sysValueByParam = systemParamValueService.getSysValueByParam(paramCode, organizationId);
        return Results.success(sysValueByParam);
    }

    @ApiOperation(value = "获取List系统参数值")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{paramCode}/LIST")
    public ResponseEntity<List<String>> getSysListByParam(@PathVariable("paramCode") String paramCode, @PathVariable("organizationId") Long organizationId) {
        List<String> sysListByParam = systemParamValueService.getSysListByParam(paramCode, organizationId);
        return Results.success(sysListByParam);
    }

}
