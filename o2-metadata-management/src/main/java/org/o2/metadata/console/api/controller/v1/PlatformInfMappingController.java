package org.o2.metadata.console.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import org.o2.metadata.console.infra.entity.PlatformInfMapping;
import org.o2.metadata.console.infra.repository.PlatformInfMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.console.app.service.PlatformInfMappingService;

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
 * 平台信息匹配表 管理 API
 *
 * @author zhilin.ren@hand-china.com 2021-08-02 11:11:28
 */
@RestController("platformInfMappingController.v1")
@RequestMapping("/v1/{organizationId}/platform-inf-mappings")
public class PlatformInfMappingController extends BaseController {

    @Autowired
    private PlatformInfMappingRepository platformInfMappingRepository;
    @Autowired
    private PlatformInfMappingService platformInfMappingService;

    @ApiOperation(value = "平台信息匹配表维护-分页查询平台信息匹配表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<PlatformInfMapping>> page(@PathVariable(value = "organizationId") Long organizationId,
                                                            PlatformInfMapping platformInfMapping,
                                                            @ApiIgnore @SortDefault(value = PlatformInfMapping.FIELD_PLATFORM_INF_MAPPING_ID,
                                                                     direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<PlatformInfMapping> list = platformInfMappingRepository.pageAndSort(pageRequest, platformInfMapping);
        return Results.success(list);
    }

    @ApiOperation(value = "平台信息匹配表维护-查询平台信息匹配表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{platformInfMappingId}")
    public ResponseEntity<PlatformInfMapping> detail(@PathVariable(value = "organizationId") Long organizationId,
                                                        @ApiParam(value = "平台信息匹配表ID", required = true) @PathVariable Long platformInfMappingId) {
        PlatformInfMapping platformInfMapping = platformInfMappingRepository.selectByPrimaryKey(platformInfMappingId);
        return Results.success(platformInfMapping);
    }

    @ApiOperation(value = "平台信息匹配表维护-创建平台信息匹配表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<PlatformInfMapping> create(@PathVariable(value = "organizationId") Long organizationId,
                                                       @RequestBody PlatformInfMapping platformInfMapping) {
        validObject(platformInfMapping);
        platformInfMappingService.save(platformInfMapping);
        return Results.success(platformInfMapping);
    }

    @ApiOperation(value = "平台信息匹配表维护-修改平台信息匹配表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<PlatformInfMapping> update(@PathVariable(value = "organizationId") Long organizationId,
                                                       @RequestBody PlatformInfMapping platformInfMapping) {
        SecurityTokenHelper.validToken(platformInfMapping);
        platformInfMappingService.save(platformInfMapping);
        return Results.success(platformInfMapping);
    }

        @ApiOperation(value = "平台信息匹配表维护-批量保存平台信息匹配表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/batch-saving")
    public ResponseEntity<List<PlatformInfMapping>> batchSave(@PathVariable(value = "organizationId") Long organizationId,
                                                       @RequestBody List<PlatformInfMapping> platformInfMappingList) {
        SecurityTokenHelper.validToken(platformInfMappingList);
        platformInfMappingService.batchSave(platformInfMappingList);
        return Results.success(platformInfMappingList);
    }

    @ApiOperation(value = "平台信息匹配表维护-删除平台信息匹配表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<Void> remove(@PathVariable(value = "organizationId") Long organizationId,
                                       @RequestBody PlatformInfMapping platformInfMapping) {
        SecurityTokenHelper.validToken(platformInfMapping);
        platformInfMappingRepository.deleteByPrimaryKey(platformInfMapping);
        return Results.success();
    }

}
