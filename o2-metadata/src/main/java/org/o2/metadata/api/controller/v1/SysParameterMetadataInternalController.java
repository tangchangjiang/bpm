package org.o2.metadata.api.controller.v1;

import org.hzero.core.util.Results;
import org.o2.metadata.app.service.SysParameterService;
import org.o2.metadata.config.EnableMetadata;
import org.o2.metadata.core.domain.vo.SystemParamDetailVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
@Api(tags = {EnableMetadata.SYS_PARAMETER_INTERNAL})
@RestController("sysParameterInternalController.v1")
@RequestMapping("v1/{organizationId}")
public class SysParameterMetadataInternalController {

    private SysParameterService sysParameterService;

    public SysParameterMetadataInternalController(SysParameterService sysParameterService) {
        this.sysParameterService = sysParameterService;
    }

    @ApiOperation(value = "从redis查询系统参数")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{paramCode}")
    public ResponseEntity<SystemParamDetailVO> listSystemParameter(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                   @PathVariable(value = "paramCode") @ApiParam(value = "参数code", required = true) String paramCode) {
        SystemParamDetailVO systemParamDetailVO = sysParameterService.listSystemParameter(paramCode, organizationId);
        return Results.success(systemParamDetailVO);
    }
}
