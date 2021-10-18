package org.o2.metadata.console.api.controller.v1;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.o2.metadata.console.api.co.StaticResourceAndConfigCO;
import org.o2.metadata.console.api.dto.StaticResourceListDTO;
import org.o2.metadata.console.api.dto.StaticResourceQueryDTO;
import org.o2.metadata.console.api.dto.StaticResourceSaveDTO;
import org.o2.metadata.console.app.service.StaticResourceInternalService;
import org.o2.metadata.console.infra.repository.StaticResourceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;

/**
 * 静态资源文件表 内部接口API
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/07/30 11:45
 */
@RestController("staticResourceInternalController.v1")
@RequestMapping("/v1/{organizationId}/static-resources-internal")
public class StaticResourceInternalController extends BaseController {

    private final StaticResourceInternalService staticResourceInternalService;
    private final StaticResourceRepository staticResourceRepository;

    public StaticResourceInternalController(StaticResourceInternalService staticResourceInternalService, StaticResourceRepository staticResourceRepository) {
        this.staticResourceInternalService = staticResourceInternalService;
        this.staticResourceRepository = staticResourceRepository;
    }

    @ApiOperation(value = "查询静态资源文件code&url映射")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @PostMapping("/query-resource-url")
    public ResponseEntity<Map<String, String>> queryResourceCodeUrlMap(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                       @RequestBody StaticResourceQueryDTO staticResourceQueryDTO) {
        staticResourceQueryDTO.setTenantId(organizationId);
        return Results.success(staticResourceInternalService.queryResourceCodeUrlMap(staticResourceQueryDTO));
    }


    @ApiOperation(value = "保存静态资源文件code&url映射")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @PostMapping("/save")
    public ResponseEntity<Boolean> saveResource(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                @RequestBody List<StaticResourceSaveDTO> staticResourceSaveDTOList) {
        validList(staticResourceSaveDTOList);
        staticResourceInternalService.batchSaveResource(staticResourceSaveDTOList);
        return Results.success(true);
    }

    @ApiOperation(value = "获取json_key&resource_url")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @PostMapping("/get_resource_and_config")
    public ResponseEntity<List<StaticResourceAndConfigCO>> getStaticResourceAndConfig(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                                @RequestBody StaticResourceListDTO staticResourceListDTO){
        staticResourceListDTO.setTenantId(organizationId);
        validObject(staticResourceListDTO);
        return Results.success(staticResourceRepository.getStaticResourceAndConfig(staticResourceListDTO));
    }

}
