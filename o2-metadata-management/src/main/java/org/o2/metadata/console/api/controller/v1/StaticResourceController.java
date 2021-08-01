package org.o2.metadata.console.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import org.o2.metadata.console.infra.entity.StaticResource;
import org.o2.metadata.console.infra.repository.StaticResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.console.app.service.StaticResourceService;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;
import io.swagger.annotations.ApiParam;
import java.util.List;

/**
 * 静态资源文件表 管理 API
 *
 * @author zhanpeng.jiang@hand-china.com 2021-07-30 11:11:38
 */
@RestController("staticResourceController.v1")
@RequestMapping("/v1/{organizationId}/static-resources")
public class StaticResourceController extends BaseController {

    @Autowired
    private StaticResourceRepository staticResourceRepository;
    @Autowired
    private StaticResourceService staticResourceService;

    @ApiOperation(value = "静态资源文件表维护-分页查询静态资源文件表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<StaticResource>> page(@PathVariable(value = "organizationId") Long organizationId,
                                                     StaticResource staticResource,
                                                     @ApiIgnore @SortDefault(value = StaticResource.FIELD_RESOURCE_ID,
                                                                     direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<StaticResource> list = staticResourceRepository.pageAndSort(pageRequest, staticResource);
        return Results.success(list);
    }

    @ApiOperation(value = "静态资源文件表维护-查询静态资源文件表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{resourceId}")
    public ResponseEntity<StaticResource> detail(@PathVariable(value = "organizationId") Long organizationId,
                                                        @ApiParam(value = "静态资源文件表ID", required = true) @PathVariable Long resourceId) {
        StaticResource staticResource = staticResourceRepository.selectByPrimaryKey(resourceId);
        return Results.success(staticResource);
    }

    @ApiOperation(value = "静态资源文件表维护-创建静态资源文件表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<StaticResource> create(@PathVariable(value = "organizationId") Long organizationId,
                                                       @RequestBody StaticResource staticResource) {
        validObject(staticResource);
        staticResourceService.save(staticResource);
        return Results.success(staticResource);
    }

    @ApiOperation(value = "静态资源文件表维护-修改静态资源文件表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<StaticResource> update(@PathVariable(value = "organizationId") Long organizationId,
                                                       @RequestBody StaticResource staticResource) {
        SecurityTokenHelper.validToken(staticResource);
        staticResourceService.save(staticResource);
        return Results.success(staticResource);
    }

        @ApiOperation(value = "静态资源文件表维护-批量保存静态资源文件表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/batch-saving")
    public ResponseEntity<List<StaticResource>> batchSave(@PathVariable(value = "organizationId") Long organizationId,
                                                       @RequestBody List<StaticResource> staticResourceList) {
        SecurityTokenHelper.validToken(staticResourceList);
        staticResourceService.batchSave(staticResourceList);
        return Results.success(staticResourceList);
    }

    @ApiOperation(value = "静态资源文件表维护-删除静态资源文件表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<Void> remove(@PathVariable(value = "organizationId") Long organizationId,
                                       @RequestBody StaticResource staticResource) {
        SecurityTokenHelper.validToken(staticResource);
        staticResourceRepository.deleteByPrimaryKey(staticResource);
        return Results.success();
    }

}