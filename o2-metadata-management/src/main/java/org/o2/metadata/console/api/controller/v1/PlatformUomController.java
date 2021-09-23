package org.o2.metadata.console.api.controller.v1;

import io.swagger.annotations.Api;
import org.hzero.core.base.BaseController;
import org.o2.metadata.console.app.service.PlatformUomService;
import org.o2.metadata.console.infra.config.MetadataManagementAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 平台值集API
 *
 * @author peng.xu@hand-china.com 2019-07-09
 */
@RestController("platformUomController.v1")
@RequestMapping("/v1/{organizationId}/platform-uom")
@Api(tags = MetadataManagementAutoConfiguration.PLATFORM_UOM)
public class PlatformUomController extends BaseController {

}
