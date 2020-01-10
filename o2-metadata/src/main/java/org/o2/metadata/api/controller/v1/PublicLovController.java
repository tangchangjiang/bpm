package org.o2.metadata.api.controller.v1;

import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.*;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.o2.metadata.core.infra.service.CustomLovService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * description:前端-集值
 * </p>
 *
 * @author wei.cai@hand-china.com 2019/12/25 12:14
 */
@Api(
        tags = {"Public Lov"}
)
@RestController("customLovController.v1")
@RequestMapping({"/v1/{organizationId}/lov"})
public class PublicLovController {

    private CustomLovService customLovService;

    public PublicLovController(CustomLovService customLovService){
        this.customLovService = customLovService;
    }

    @ApiOperation("集值 - 查询")
    @Permission(permissionPublic = true)
    @GetMapping({"/search-by-code"})
    public ResponseEntity<List<LovValueDTO>> searchProduct(@RequestParam @ApiParam(value = "值集编码", required = true) String lovCode,
                                                           @PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId) {
        return customLovService.queryLovInfo(lovCode, organizationId);
    }

    @ApiOperation("集值 - 批量查询")
    @Permission(permissionPublic = true)
    @GetMapping({"/batch/search-by-code"})
    @ApiImplicitParams({@ApiImplicitParam(
            name = "queryMap",
            value = "批量查询条件,形式:code=返回key",
            paramType = "query",
            example = "CODE1=codeOne&CODE2=codeTwo",
            required = true
    ), @ApiImplicitParam(
            name = "tenantId",
            value = "租户ID",
            paramType = "query"
    )})
    public ResponseEntity<Map<String, List<LovValueDTO>>> batchSearchProduct(@RequestParam Map<String, String> queryMap,
                                                                       @PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId) {
        return customLovService.batchQueryLovInfo(queryMap, organizationId);
    }
}