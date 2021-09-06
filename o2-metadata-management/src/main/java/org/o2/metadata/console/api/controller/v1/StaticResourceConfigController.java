package org.o2.metadata.console.api.controller.v1;

import io.choerodon.mybatis.pagehelper.PageHelper;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import org.o2.metadata.console.api.dto.StaticResourceConfigDTO;
import org.o2.metadata.console.infra.entity.StaticResourceConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.console.app.service.StaticResourceConfigService;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 静态资源配置 管理 API
 *
 * @author wenjie.liu@hand-china.com 2021-09-06 10:47:44
 */
@RestController("staticResourceConfigController.v1")
@RequestMapping("/v1/{organizationId}/static-resource-configs")
public class StaticResourceConfigController extends BaseController {

    private final StaticResourceConfigService staticResourceConfigService;

    public StaticResourceConfigController(final StaticResourceConfigService staticResourceConfigService){
        this.staticResourceConfigService = staticResourceConfigService;
    }

    @ApiOperation(value = "静态资源配置维护-分页查询静态资源配置列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<StaticResourceConfig>> page(@PathVariable(value = "organizationId") Long organizationId,
                                                            StaticResourceConfigDTO staticResourceConfig,
                                                            @ApiIgnore @SortDefault(value = StaticResourceConfig.FIELD_RESOURCE_CONFIG_ID,
                                                                     direction = Sort.Direction.DESC) PageRequest pageRequest) {
        staticResourceConfig.setTenantId(staticResourceConfig.getTenantId() == null ? organizationId : staticResourceConfig.getTenantId());
        Page<StaticResourceConfig> list = PageHelper.doPageAndSort(pageRequest, () -> staticResourceConfigService.listStaticResourceConfig(staticResourceConfig));
        return Results.success(list);
    }

    @ApiOperation(value = "静态资源配置维护-创建静态资源配置")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<StaticResourceConfig> create(@PathVariable(value = "organizationId") Long organizationId,
                                                       @RequestBody StaticResourceConfig staticResourceConfig) {
        staticResourceConfig.setTenantId(staticResourceConfig.getTenantId() == null ? organizationId : staticResourceConfig.getTenantId());
        validObject(staticResourceConfig);
        staticResourceConfigService.save(staticResourceConfig);
        return Results.success(staticResourceConfig);
    }

    @ApiOperation(value = "静态资源配置维护-修改静态资源配置")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<StaticResourceConfig> update(@PathVariable(value = "organizationId") Long organizationId,
                                                       @RequestBody StaticResourceConfig staticResourceConfig) {
        staticResourceConfig.setTenantId(staticResourceConfig.getTenantId() == null ? organizationId : staticResourceConfig.getTenantId());
        SecurityTokenHelper.validToken(staticResourceConfig);
        staticResourceConfigService.save(staticResourceConfig);
        return Results.success(staticResourceConfig);
    }

}
