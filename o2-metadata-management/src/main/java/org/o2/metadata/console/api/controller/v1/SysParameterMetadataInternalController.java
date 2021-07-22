package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.core.util.Results;

import org.o2.metadata.console.api.vo.SystemParameterVO;
import org.o2.metadata.console.app.service.SysParamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
@RestController("sysParameterInternalController.v1")
@RequestMapping("v1/{organizationId}/sysParameter-internal")
public class SysParameterMetadataInternalController {

    private SysParamService sysParamService;

    public SysParameterMetadataInternalController(SysParamService sysParamService) {
        this.sysParamService = sysParamService;
    }

    @ApiOperation(value = "从redis查询系统参数")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{paramCode}")
    public ResponseEntity<SystemParameterVO> getSystemParameter(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                @PathVariable(value = "paramCode") @ApiParam(value = "参数code", required = true) String paramCode) {
        return Results.success(sysParamService.getSystemParameter(paramCode, organizationId));
    }

    @ApiOperation(value = "批量从redis查询系统参数")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @GetMapping("/paramCodes")
    public ResponseEntity<Map<String, SystemParameterVO>> listSystemParameters(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                               @RequestParam List<String> paramCodes) {
        List<SystemParameterVO> systemParameterVOList = sysParamService.listSystemParameters(paramCodes, organizationId);
        Map<String, SystemParameterVO> map = new HashMap<>(4);
        if (CollectionUtils.isEmpty(systemParameterVOList)) {
            Results.success(map);
        }
        for (SystemParameterVO vo : systemParameterVOList) {
            map.put(vo.getParamCode(), vo);
        }
        return Results.success(map);
    }
}
