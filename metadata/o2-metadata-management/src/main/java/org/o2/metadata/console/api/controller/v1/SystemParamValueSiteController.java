package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.o2.metadata.console.app.service.SystemParamValueService;
import org.o2.metadata.console.infra.config.MetadataManagementAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    public SystemParamValueSiteController(SystemParamValueService systemParamValueService) {
        this.systemParamValueService = systemParamValueService;
    }

    @ApiOperation(value = "获取KV系统参数值（站点级）")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping("/{paramCode}/KV")
    public ResponseEntity<String> getSysValueByParam(@PathVariable("paramCode") String paramCode, Long tenantId) {
        String sysValueByParam = systemParamValueService.getSysValueByParam(paramCode, tenantId);
        return Results.success(sysValueByParam);
    }
}
