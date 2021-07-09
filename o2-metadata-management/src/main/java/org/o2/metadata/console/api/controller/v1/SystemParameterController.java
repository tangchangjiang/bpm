package org.o2.metadata.console.api.controller.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.console.app.service.SysParamService;
import org.o2.metadata.console.config.EnableMetadataConsole;
import org.o2.metadata.console.infra.entity.SystemParameter;
import org.o2.metadata.console.domain.repository.SystemParameterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;


import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;

/**
 * 系统参数 管理 API
 *
 * @author hongyun.wang01@hand-china.com 2020-02-13
 */
@Slf4j
@RestController("systemParameterController.v1")
@RequestMapping("/v1/{organizationId}/system-parameters")
@Api(tags = EnableMetadataConsole.SYSTEM_PARAMETER)
public class SystemParameterController extends BaseController {

    @Autowired
    private SystemParameterRepository systemParameterRepository;

    @Autowired
    private SysParamService sysParamService;

    @ApiOperation(value = "系统参数列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = {BaseConstants.FIELD_BODY})
    @GetMapping
    public ResponseEntity<?> list(SystemParameter systemParameter, @ApiIgnore @SortDefault(value = SystemParameter.FIELD_PARAM_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest, @PathVariable("organizationId") Long organizationId) {
        systemParameter.setTenantId(organizationId);
        Page<SystemParameter> list = PageHelper.doPageAndSort(pageRequest,
                () -> systemParameterRepository.fuzzyQuery(systemParameter, organizationId));
        return Results.success(list);
    }

    @ApiOperation(value = "系统参数明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = {BaseConstants.FIELD_BODY})
    @GetMapping("/{paramId}")
    public ResponseEntity<?> detail(@PathVariable Long paramId, @PathVariable("organizationId") Long organizationId) {
        SystemParameter condition = new SystemParameter();
        condition.setTenantId(organizationId);
        condition.setParamId(paramId);
        SystemParameter systemParameter = systemParameterRepository.selectOne(condition);
        return Results.success(systemParameter);
    }

    @ApiOperation(value = "创建系统参数")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<?> create(@RequestBody SystemParameter systemParameter, @PathVariable("organizationId") Long organizationId) {
        systemParameter.setTenantId(organizationId);
        this.validObject(systemParameter);
        systemParameterRepository.insertSelective(systemParameter);
        sysParamService.updateToRedis(systemParameter, organizationId);
        return Results.success(systemParameter);
    }

    @ApiOperation(value = "修改系统参数")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<?> update(@RequestBody SystemParameter systemParameter, @PathVariable("organizationId") Long organizationId) {
        systemParameter.setTenantId(organizationId);
        this.validObject(systemParameter);
        SecurityTokenHelper.validToken(systemParameter);
        systemParameterRepository.updateByPrimaryKey(systemParameter);
        sysParamService.updateToRedis(systemParameter, organizationId);
        sysParamService.extraOperate(systemParameter.getParamCode(), organizationId);
        return Results.success(systemParameter);
    }

    @ApiOperation(value = "删除系统参数")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody SystemParameter systemParameter, @PathVariable("organizationId") Long organizationId) {
        systemParameter.setTenantId(organizationId);
        SecurityTokenHelper.validToken(systemParameter);
        systemParameterRepository.deleteByPrimaryKey(systemParameter);
        return Results.success();
    }

}
