package org.o2.metadata.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.core.util.Results;
import org.o2.metadata.api.co.SystemParameterCO;
import org.o2.metadata.app.service.SysParameterService;
import org.o2.metadata.config.MetadataAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 元数据
 *
 * @author chao.yang05@hand-china.com 2023-04-18
 */
@Api(tags = {MetadataAutoConfiguration.SYS_PARAMETER_INTERNAL})
@RestController("sysParamInternalController.v1")
@RequestMapping("v1/sysParam-internal")
public class SysParamInternalController {

    private final SysParameterService sysParameterService;

    public SysParamInternalController(SysParameterService sysParameterService) {
        this.sysParameterService = sysParameterService;
    }

    @ApiOperation(value = "从redis查询系统参数（多租户）")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @PostMapping("/batch-tenant")
    public ResponseEntity<Map<Long, SystemParameterCO>> getSystemParameterBatchTenant(@RequestBody Map<Long, String> paramCodeMap) {
        Map<Long, SystemParameterCO> resultMap = new HashMap<>();
        paramCodeMap.forEach((tenant, paramCode) -> resultMap.put(tenant, sysParameterService.getSystemParameter(paramCode, tenant)));
        return Results.success(resultMap);
    }

    @ApiOperation(value = "批量从redis查询系统参数（多租户）")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @PostMapping("/list-param-batch-tenant")
    ResponseEntity<Map<Long, Map<String, SystemParameterCO>>> listSysParamBatchTenant(@RequestBody Map<Long, List<String>> paramCodesMap) {
        Map<Long, Map<String, SystemParameterCO>> resultMap = new HashMap<>();
        paramCodesMap.forEach((tenant, paramCodes) -> {
            List<SystemParameterCO> sysParamList = sysParameterService.listSystemParameters(paramCodes, tenant);
            Map<String, SystemParameterCO> map = new HashMap<>();
            if (CollectionUtils.isNotEmpty(sysParamList)) {
                map = sysParamList.stream().collect(Collectors.toMap(SystemParameterCO::getParamCode, Function.identity(), (s1, s2) -> s2));
            }
            resultMap.put(tenant, map);
        });
        return Results.success(resultMap);
    }
}
