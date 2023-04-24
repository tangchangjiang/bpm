package org.o2.metadata.api.controller.v1;

import org.apache.commons.collections4.CollectionUtils;
import org.hzero.core.util.Results;
import org.o2.metadata.api.co.SystemParameterCO;
import org.o2.metadata.app.service.SysParameterService;
import org.o2.metadata.config.MetadataAutoConfiguration;
import org.springframework.http.ResponseEntity;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
@Api(tags = {MetadataAutoConfiguration.SYS_PARAMETER_INTERNAL})
@RestController("sysParameterInternalController.v1")
@RequestMapping("v1/{organizationId}/sysParameter-internal")
public class SysParameterMetadataInternalController {

    private SysParameterService sysParameterService;

    public SysParameterMetadataInternalController(SysParameterService sysParameterService) {
        this.sysParameterService = sysParameterService;
    }

    @ApiOperation(value = "从redis查询系统参数")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{paramCode}")
    public ResponseEntity<SystemParameterCO> getSystemParameter(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                @PathVariable(value = "paramCode") @ApiParam(value = "参数code", required = true) String paramCode) {
        return Results.success(sysParameterService.getSystemParameter(paramCode, organizationId));
    }

    @ApiOperation(value = "批量从redis查询系统参数")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @GetMapping("/paramCodes")
    public ResponseEntity<Map<String, SystemParameterCO>> listSystemParameters(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                               @RequestParam List<String> paramCodes) {
        List<SystemParameterCO> systemParameterVOList = sysParameterService.listSystemParameters(paramCodes, organizationId);
        Map<String, SystemParameterCO> map = new HashMap<>(4);
        if (CollectionUtils.isEmpty(systemParameterVOList)) {
            Results.success(map);
        }
        for (SystemParameterCO vo : systemParameterVOList) {
            map.put(vo.getParamCode(), vo);
        }
        return Results.success(map);
    }
}
