package org.o2.metadata.console.api.controller.v1;

import io.choerodon.mybatis.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import org.o2.metadata.console.config.MetadataManagementAutoConfiguration;
import org.o2.metadata.console.infra.entity.Platform;
import org.o2.metadata.console.infra.repository.PlatformRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.console.app.service.PlatformService;

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
 * 平台定义表 管理 API
 *
 * @author zhilin.ren@hand-china.com 2021-08-02 11:11:28
 */
@Api(tags = MetadataManagementAutoConfiguration.PLATFORM)
@RestController("platformController.v1")
@RequestMapping("/v1/{organizationId}/platforms")
@RequiredArgsConstructor
public class PlatformController extends BaseController {

    private final PlatformRepository platformRepository;
    private final PlatformService platformService;

    @ApiOperation(value = "平台定义表维护-分页查询平台定义表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    public ResponseEntity<Page<Platform>> page(@PathVariable(value = "organizationId") Long organizationId,
                                                            Platform platform,
                                                            @ApiIgnore @SortDefault(value = Platform.FIELD_PLATFORM_ID,
                                                                     direction = Sort.Direction.DESC) PageRequest pageRequest) {
        platform.setTenantId(organizationId);
        Page<Platform> list = PageHelper.doPageAndSort(pageRequest,
                ()-> platformRepository.listPlatform(platform));
        return Results.success(list);
    }

    @ApiOperation(value = "平台定义表维护-查询平台定义表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{platformId}")
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    public ResponseEntity<Platform> detail(@PathVariable(value = "organizationId") Long organizationId,
                                                        @ApiParam(value = "平台定义表ID", required = true) @PathVariable Long platformId) {
        Platform platform = platformRepository.selectByPrimaryKey(platformId);
        return Results.success(platform);
    }

    @ApiOperation(value = "平台定义表维护-创建平台定义表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    public ResponseEntity<Platform> create(@PathVariable(value = "organizationId") Long organizationId,
                                                       @RequestBody Platform platform) {
        platform.setTenantId(organizationId);
        validObject(platform);
        platformService.save(platform);
        return Results.success(platform);
    }

    @ApiOperation(value = "平台定义表维护-修改平台定义表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    public ResponseEntity<Platform> update(@PathVariable(value = "organizationId") Long organizationId,
                                                       @RequestBody Platform platform) {
        platform.setTenantId(organizationId);
        SecurityTokenHelper.validToken(platform);
        platformService.save(platform);
        return Results.success(platform);
    }

    @ApiOperation(value = "平台定义表维护-批量保存平台定义表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/batch-saving")
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    public ResponseEntity<List<Platform>> batchSave(@PathVariable(value = "organizationId") Long organizationId,
                                                       @RequestBody List<Platform> platformList) {
        SecurityTokenHelper.validToken(platformList);
        platformService.batchSave(platformList);
        return Results.success(platformList);
    }

    @ApiOperation(value = "平台定义表维护-删除平台定义表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<Void> remove(@PathVariable(value = "organizationId") Long organizationId,
                                       @RequestBody Platform platform) {
        SecurityTokenHelper.validToken(platform);
        platformRepository.deleteByPrimaryKey(platform);
        return Results.success();
    }

}
