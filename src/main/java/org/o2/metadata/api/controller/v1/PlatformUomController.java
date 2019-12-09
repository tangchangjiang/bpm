package org.o2.metadata.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.o2.metadata.app.service.PlatformUomService;
import org.o2.metadata.config.MetadataSwagger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 平台值集API
 *
 * @author peng.xu@hand-china.com 2019-07-09
 */
@RestController("platformUomController.v1")
@RequestMapping("/v1/{organizationId}/platform-uom")
@Api(tags = MetadataSwagger.PLATFORM_UOM)
public class PlatformUomController extends BaseController {

    private PlatformUomService platformUomService;

    public PlatformUomController(PlatformUomService platformUomService) {
        this.platformUomService = platformUomService;
    }

    @ApiOperation(value = "根据平台父值集获取子值集")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/parent-value")
    public ResponseEntity<?> getChildren(@RequestParam(required = true) final String parentValue) {
        return Results.success(platformUomService.getChildrenValues(parentValue));
    }
}
