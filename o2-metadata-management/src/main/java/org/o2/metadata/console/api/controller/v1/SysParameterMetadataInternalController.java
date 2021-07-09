package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.util.Results;

import org.o2.metadata.console.api.vo.SystemParameterVO;
import org.o2.metadata.console.app.service.SysParamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
@RestController("sysParameterInternalController.v1")
@RequestMapping("v1/{organizationId}")
public class SysParameterMetadataInternalController {

    private SysParamService sysParamService;

    public SysParameterMetadataInternalController(SysParamService sysParamService) {
        this.sysParamService = sysParamService;
    }

    @ApiOperation(value = "从redis查询系统参数")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{paramCode}")
    public ResponseEntity<SystemParameterVO> listSystemParameter(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                 @PathVariable(value = "paramCode") @ApiParam(value = "参数code", required = true) String paramCode) {
        return Results.success(sysParamService.getSystemParameter(paramCode, organizationId));
    }
}
