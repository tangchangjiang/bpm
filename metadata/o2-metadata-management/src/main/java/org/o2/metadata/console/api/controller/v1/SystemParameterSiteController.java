package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.o2.metadata.console.infra.config.MetadataManagementAutoConfiguration;
import org.o2.metadata.console.infra.entity.SystemParameter;
import org.o2.metadata.console.infra.repository.SystemParameterRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    public SystemParameterSiteController(SystemParameterRepository systemParameterRepository) {
        this.systemParameterRepository = systemParameterRepository;
    }

    @ApiOperation(value = "系统参数列表（站点级）")
    @Permission(level = ResourceLevel.SITE)
    @ProcessLovValue(targetField = {BaseConstants.FIELD_BODY})
    @GetMapping("/find")
    public ResponseEntity<SystemParameter> findOne(SystemParameter systemParameter) {
        return Results.success(systemParameterRepository.findOne(systemParameter));
    }
}
