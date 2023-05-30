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
import org.hzero.core.base.BaseConstants;
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

/**
 * 系统参数值 管理 API（站点级）
 *
 * @author chao.yang05@hand-china.com 2023-04-17
 */
@RestController("systemParamValueSiteController.v1")
@RequestMapping("/v1/system-param-values")
@Api(tags = MetadataManagementAutoConfiguration.SYSTEM_PARAMETER_VALUE_SITE)
public class SystemParamValueSiteController {

    private final SystemParamValueService systemParamValueService;
    private final SystemParamValueRepository systemParamValueRepository;

    public SystemParamValueSiteController(SystemParamValueService systemParamValueService,
                                          SystemParamValueRepository systemParamValueRepository) {
        this.systemParamValueService = systemParamValueService;
        this.systemParamValueRepository = systemParamValueRepository;
    }

    @ApiOperation(value = "系统参数值列表（平台层）")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping
    public ResponseEntity<Page<SystemParamValue>> listSystemParamValues(SystemParamValue systemParamValue, @ApiIgnore @SortDefault(value = SystemParamValue.FIELD_VALUE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<SystemParamValue> page = PageHelper.doPage(pageRequest, () -> systemParamValueRepository.selectByCondition(Condition.builder(SystemParamValue.class)
                .andWhere(Sqls.custom().andLike(SystemParamValue.FIELD_PARAM_VALUE, systemParamValue.getParamValue(), true)
                        .andLike(SystemParamValue.FIELD_PARAM_KEY, systemParamValue.getParamKey(), true)
                        .andEqualTo(SystemParamValue.FIELD_PARAM_ID, systemParamValue.getParamId())).build()));
        return Results.success(page);
    }

    @ApiOperation(value = "系统参数值明细（平台层）")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping("/{valueId}")
    public ResponseEntity<SystemParamValue> getSystemParamValue(@PathVariable Long valueId) {
        SystemParamValue condition = new SystemParamValue();
        condition.setValueId(valueId);
        SystemParamValue systemParamValue = systemParamValueRepository.selectOne(condition);
        return Results.success(systemParamValue);
    }

    @ApiOperation(value = "创建系统参数值（平台层）")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping
    public ResponseEntity<SystemParamValue> saveSystemParamValue(@RequestBody SystemParamValue systemParamValue) {
        systemParamValue.setTenantId(BaseConstants.DEFAULT_TENANT_ID);
        systemParamValueService.systemParamValueValidate(systemParamValue);
        systemParamValueService.saveSystemParamValue(systemParamValue);
        return Results.success(systemParamValue);
    }

    @ApiOperation(value = "修改系统参数值（平台层）")
    @Permission(level = ResourceLevel.SITE)
    @PutMapping
    public ResponseEntity<SystemParamValue> updateSystemParamValue(@RequestBody SystemParamValue systemParamValue) {
        systemParamValue.setTenantId(BaseConstants.DEFAULT_TENANT_ID);
        SecurityTokenHelper.validToken(systemParamValue);
        systemParamValueService.systemParamValueValidate(systemParamValue);
        systemParamValueService.updateSystemParamValue(systemParamValue);
        return Results.success(systemParamValue);
    }

    @ApiOperation(value = "删除系统参数值（平台层）")
    @Permission(level = ResourceLevel.SITE)
    @DeleteMapping
    public ResponseEntity<OperateResponse> removeSystemParamValue(@RequestBody SystemParamValue systemParamValue) {
        systemParamValue.setTenantId(BaseConstants.DEFAULT_TENANT_ID);
        SecurityTokenHelper.validToken(systemParamValue);
        systemParamValueService.removeSystemParamValue(systemParamValue);
        return Results.success(OperateResponse.success());
    }

    @ApiOperation(value = "获取KV系统参数值（平台层）")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping("/{paramCode}/KV")
    public ResponseEntity<String> getSysValueByParam(@PathVariable("paramCode") String paramCode, Long tenantId) {
        if (null == tenantId) {
            tenantId = BaseConstants.DEFAULT_TENANT_ID;
        }
        String sysValueByParam = systemParamValueService.getSysValueByParam(paramCode, tenantId);
        return Results.success(sysValueByParam);
    }
}
