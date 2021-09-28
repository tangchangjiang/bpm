package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.o2.metadata.console.api.co.PlatformCO;
import org.o2.metadata.console.api.dto.PlatformQueryInnerDTO;
import org.o2.metadata.console.app.service.PlatformService;
import org.o2.metadata.console.infra.config.MetadataManagementAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * description 平台信息匹配内部调用
 *
 * @author zhilin.ren@hand-china.com 2021/08/02 21:19
 */
@Api(tags= MetadataManagementAutoConfiguration.PLATFORM)
@RestController("platformInternalController.v1")
@RequestMapping("/v1/{organizationId}/platform-internal")
public class PlatformInternalController{

    private final PlatformService platformService;

    public PlatformInternalController(PlatformService platformService) {
        this.platformService = platformService;
    }

    @ApiOperation(value = "查询平台信息匹配结果")
    @Permission(level = ResourceLevel.ORGANIZATION,permissionPublic = true)
    @PostMapping("/list")
    public ResponseEntity<Map<String, PlatformCO>> getPlatformInfMappings(@PathVariable(value = "organizationId") Long organizationId,
                                                                          @RequestBody PlatformQueryInnerDTO queryInnerDTO) {
        queryInnerDTO.setTenantId(organizationId);
        return Results.success(platformService.selectCondition(queryInnerDTO));
    }
}
