package org.o2.metadata.console.api.controller.v1.feign;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.o2.metadata.console.api.dto.StaticResourceQueryDTO;
import org.o2.metadata.console.api.dto.StaticResourceSaveDTO;
import org.o2.metadata.console.app.service.StaticResourceInternalService;
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

    public StaticResourceInternalController(StaticResourceInternalService staticResourceInternalService) {
        this.staticResourceInternalService = staticResourceInternalService;
    }

    @ApiOperation(value = "查询静态资源文件code&url映射")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @PostMapping("/query-resource-url")
    public ResponseEntity<Map<String, String>> queryResourceCodeUrlMap(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                       @RequestBody StaticResourceQueryDTO staticResourceQueryDTO) {
        return Results.success(staticResourceInternalService.queryResourceCodeUrlMap(staticResourceQueryDTO));
    }


    @ApiOperation(value = "保存静态资源文件code&url映射")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @PostMapping("/save")
    public ResponseEntity<Boolean> saveResource(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                @RequestBody List<StaticResourceSaveDTO> staticResourceSaveDTOList) {
        for (StaticResourceSaveDTO saveDTO : staticResourceSaveDTOList) {
            staticResourceInternalService.saveResource(saveDTO);
        }
        return Results.success(true);
    }

}
