package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.util.Results;
import org.o2.metadata.console.api.dto.CatalogVersionDTO;
import org.o2.metadata.console.app.service.CatalogVersionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 目录版本 管理 API
 *
 * @author jiu.yang@hand-china.com 2019-12-19 16:32:58
 */
@RestController("catalogVersionInternalController.v1")
@RequestMapping("/v1/{organizationId}/catalogVersion-internal")
public class CatalogVersionInternalController{

    private final CatalogVersionService catalogVersionService;

    public CatalogVersionInternalController(CatalogVersionService catalogVersionService) {
        this.catalogVersionService = catalogVersionService;
    }

    @ApiOperation(value = "目录版本")
    @Permission(permissionPublic = true, level = ResourceLevel.ORGANIZATION)
    @PostMapping("/select-name")
    public ResponseEntity<Map<String,String>> listCatalogVersions(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody CatalogVersionDTO catalogVersionDTO) {
        return Results.success(catalogVersionService.listCatalogVersions(catalogVersionDTO,organizationId));
    }

}
