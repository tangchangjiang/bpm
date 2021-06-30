package org.o2.metadata.console.api.controller.v1;

import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.console.app.service.SysParamService;
import org.o2.metadata.console.app.service.SystemParamValueService;
import org.o2.metadata.console.config.EnableMetadataConsole;
import org.o2.metadata.console.domain.entity.SystemParamValue;
import org.o2.metadata.console.domain.repository.SystemParamValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 系统参数值 管理 API
 *
 * @author hongyun.wang01@hand-china.com 2020-02-13
 */
@RestController("systemParamValueController.v1")
@RequestMapping("/v1/{organizationId}/system-param-values")
@Api(tags = EnableMetadataConsole.SYSTEM_PARAMETER_VALUE)
public class SystemParamValueController extends BaseController {

    @Autowired
    private SystemParamValueRepository systemParamValueRepository;
    @Autowired
    private SystemParamValueService systemParamValueService;
    @Autowired
    private SysParamService sysParamService;

    @ApiOperation(value = "系统参数值列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<?> list(SystemParamValue systemParamValue, @ApiIgnore @SortDefault(value = SystemParamValue.FIELD_VALUE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest, @PathVariable("organizationId") Long organizationId) {
        systemParamValue.setTenantId(organizationId);
        Page<SystemParamValue> page = PageHelper.doPage(pageRequest, () -> systemParamValueRepository.selectByCondition(Condition.builder(SystemParamValue.class)
                .andWhere(Sqls.custom().andEqualTo(SystemParamValue.FIELD_TENANT_ID, systemParamValue.getTenantId())
                        .andLike(SystemParamValue.FIELD_PARAM_VALUE, systemParamValue.getParamValue(), true)
                        .andEqualTo(SystemParamValue.FIELD_PARAM_ID, systemParamValue.getParamId(), true)).build()));
        return Results.success(page);
    }

    @ApiOperation(value = "系统参数值明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{valueId}")
    public ResponseEntity<?> detail(@PathVariable Long valueId, @PathVariable("organizationId") Long organizationId) {
        SystemParamValue condition = new SystemParamValue();
        condition.setTenantId(organizationId);
        condition.setValueId(valueId);
        SystemParamValue systemParamValue = systemParamValueRepository.selectOne(condition);
        return Results.success(systemParamValue);
    }

    @ApiOperation(value = "创建系统参数值")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<?> create(@RequestBody SystemParamValue systemParamValue, @PathVariable("organizationId") Long organizationId) {
        systemParamValue.setTenantId(organizationId);
        systemParamValueRepository.insertSelective(systemParamValue);
        sysParamService.updateToRedis(systemParamValue.getParamId(), organizationId);
        return Results.success(systemParamValue);
    }

    @ApiOperation(value = "修改系统参数值")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<?> update(@RequestBody SystemParamValue systemParamValue, @PathVariable("organizationId") Long organizationId) {
        systemParamValue.setTenantId(organizationId);
        SecurityTokenHelper.validToken(systemParamValue);
        systemParamValueRepository.updateByPrimaryKey(systemParamValue);
        sysParamService.updateToRedis(systemParamValue.getParamId(), organizationId);
        return Results.success(systemParamValue);
    }

    @ApiOperation(value = "删除系统参数值")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody SystemParamValue systemParamValue, @PathVariable("organizationId") Long organizationId) {
        systemParamValue.setTenantId(organizationId);
        SecurityTokenHelper.validToken(systemParamValue);
        systemParamValueRepository.deleteByPrimaryKey(systemParamValue);
        sysParamService.updateToRedis(systemParamValue.getParamId(), organizationId);
        return Results.success();
    }

    @ApiOperation(value = "获取KV系统参数值")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{paramCode}/KV")
    public ResponseEntity<?> getSysValueByParam(@PathVariable("paramCode") String paramCode, @PathVariable("organizationId") Long organizationId) {
        String sysValueByParam = systemParamValueService.getSysValueByParam(paramCode, organizationId);
        return Results.success(sysValueByParam);
    }

    @ApiOperation(value = "获取List系统参数值")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{paramCode}/LIST")
    public ResponseEntity<?> getSysListByParam(@PathVariable("paramCode") String paramCode, @PathVariable("organizationId") Long organizationId) {
        List<String> sysListByParam = systemParamValueService.getSysListByParam(paramCode, organizationId);
        return Results.success(sysListByParam);
    }

    @ApiOperation(value = "获取SET系统参数值")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{paramCode}/SET")
    public ResponseEntity<?> getSysSetByParam(@PathVariable("paramCode") String paramCode, @PathVariable("organizationId") Long organizationId) {
        Set<String> sysSetByParam = systemParamValueService.getSysSetByParam(paramCode, organizationId);
        return Results.success(sysSetByParam);
    }

}
