package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.o2.metadata.console.api.dto.CatalogVersionDTO;
import org.o2.metadata.console.app.service.CatalogVersionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 目录版本 管理 API
 *
 * @author jiu.yang@hand-china.com 2019-12-19 16:32:58
 */
@RestController("catalogVersionController.v1")
@RequestMapping("/v1/{organizationId}/catalogVersion-internal")
public class CatalogVersionInternalController extends BaseController {

    private final CatalogVersionService catalogVersionService;

    public CatalogVersionInternalController(CatalogVersionService catalogVersionService) {
        this.catalogVersionService = catalogVersionService;
    }

    @ApiOperation(value = "目录版本")
    @Permission(permissionWithin = true, level = ResourceLevel.ORGANIZATION)
    @GetMapping("select-name")
    public ResponseEntity<Map<String,String>> batchSelectNameByCode(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestParam List<CatalogVersionDTO> list) {
        return Results.success(catalogVersionService.batchSelectNameByCode(list,organizationId));
    }

}
