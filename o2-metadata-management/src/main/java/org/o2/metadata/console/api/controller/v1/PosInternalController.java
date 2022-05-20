package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.o2.metadata.console.api.co.PosAddressCO;
import org.o2.metadata.console.api.dto.PosAddressQueryInnerDTO;
import org.o2.metadata.console.app.service.PosService;
import org.o2.metadata.console.infra.config.MetadataManagementAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 服务点信息 管理 API
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */

@RestController("posInternalController.v1")
@RequestMapping("/v1/{organizationId}/pos-internal")
@Api(tags = MetadataManagementAutoConfiguration.POS)
public class PosInternalController extends BaseController {

    private final PosService posService;

    public PosInternalController( final PosService posService) {
        this.posService = posService;
    }

    @ApiOperation(value = "服务点地址")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @PostMapping("/select-address")
    public ResponseEntity<Map<String, PosAddressCO>> listPosAddress(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                    @RequestBody PosAddressQueryInnerDTO posAddressQueryInnerDTO) {
        Map<String, PosAddressCO> map = new HashMap<>(16);
        if (null == posAddressQueryInnerDTO) {
            return Results.success(map);
        }
        List<PosAddressCO> voList = posService.listPosAddress(posAddressQueryInnerDTO,organizationId);
        for (PosAddressCO co : voList) {
            map.put(co.getPosCode(), co);
        }
        return Results.success(map);
    }

    @ApiOperation(value = "查询服务点名称")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @PostMapping("/select-name")
    public ResponseEntity<Map<String, String>> listPoseName(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                            @RequestBody List<String> posCodes) {
        return Results.success(posService.listPosName(organizationId, posCodes));
    }
}
