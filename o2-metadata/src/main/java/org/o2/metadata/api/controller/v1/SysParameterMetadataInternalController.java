package org.o2.metadata.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.util.Results;
import org.o2.metadata.domain.vo.SysParameterVO;
import org.o2.metadata.app.service.SysParameterService;
import org.o2.metadata.config.EnableMetadata;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("保存系统参数缓存(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @PostMapping({"/internal/saveSysParameter"})
    public ResponseEntity<?> saveSysParameter(@RequestBody final SysParameterVO sysParameterVO,
                                              @PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId) {
        sysParameterService.saveSysParameter(sysParameterVO);
        return Results.success();
    }

    @ApiOperation("删除系统参数缓存(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @DeleteMapping({"/internal/deleteSysParameter"})
    public ResponseEntity<?> deleteSysParameter(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                @RequestParam(value = "code", required = true) String code) {
        sysParameterService.deleteSysParameter(code, organizationId);
        return Results.success();
    }

    @ApiOperation("根据系统参数编码获取实体(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @GetMapping({"/internal/getSysParameter"})
    public ResponseEntity<?> getSysParameter(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                             @RequestParam(value = "code", required = true) String code) {
        sysParameterService.getSysParameter(code, organizationId);
        return Results.success();
    }

    @ApiOperation("根据系统参数编码获取参数值(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @GetMapping({"/internal/getSysParameterValue"})
    public ResponseEntity<?> getSysParameterValue(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                  @RequestParam(value = "code", required = true) String code) {
        sysParameterService.getSysParameterValue(code, organizationId);
        return Results.success();
    }

    @ApiOperation("根据系统参数编码判断参数是否有效(内部调用)")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @GetMapping({"/internal/isSysParameterActive"})
    public ResponseEntity<?> isSysParameterActive(@PathVariable @ApiParam(value = "租户ID", required = true) final Long organizationId,
                                                  @RequestParam(value = "code", required = true) String code) {
        Boolean result = sysParameterService.isSysParameterActive(code, organizationId);
        return Results.success(result);
    }
}
