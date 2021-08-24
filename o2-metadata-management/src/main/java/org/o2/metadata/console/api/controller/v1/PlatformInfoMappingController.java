package org.o2.metadata.console.api.controller.v1;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import org.o2.metadata.console.api.dto.InfMappingDTO;
import org.o2.metadata.console.infra.config.MetadataManagementAutoConfiguration;
import org.o2.metadata.console.infra.entity.PlatformInfoMapping;
import org.o2.metadata.console.infra.repository.PlatformInfoMappingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.console.app.service.PlatformInfoMappingService;

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
@Api(tags=MetadataManagementAutoConfiguration.PLATFORM_INF_MAPPING)
@RestController("platformInfMappingController.v1")
@RequestMapping("/v1/{organizationId}/platform-inf-mappings")
@RequiredArgsConstructor
public class PlatformInfoMappingController extends BaseController {


    private final PlatformInfoMappingRepository platformInfoMappingRepository;
    private final PlatformInfoMappingService platformInfoMappingService;

    @ApiOperation(value = "平台信息匹配表维护-分页查询平台信息匹配表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    public ResponseEntity<Page<PlatformInfoMapping>> page(@PathVariable(value = "organizationId") Long organizationId,
                                                          InfMappingDTO platformInfMapping,
                                                          @ApiIgnore @SortDefault(value = PlatformInfoMapping.FIELD_PLATFORM_INF_MAPPING_ID,
                                                                     direction = Sort.Direction.DESC) PageRequest pageRequest) {
        platformInfMapping.setTenantId(organizationId);
        Page<PlatformInfoMapping> list = platformInfoMappingService.page(platformInfMapping, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "平台信息匹配表维护-查询平台信息匹配表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{platformInfMappingId}")
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    public ResponseEntity<PlatformInfoMapping> detail(@PathVariable(value = "organizationId") Long organizationId,
                                                      @ApiParam(value = "平台信息匹配表ID", required = true) @PathVariable Long platformInfMappingId) {
        PlatformInfoMapping platformInfoMapping = platformInfoMappingRepository.selectById(platformInfMappingId);
        return Results.success(platformInfoMapping);
    }

    @ApiOperation(value = "平台信息匹配表维护-创建平台信息匹配表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    public ResponseEntity<PlatformInfoMapping> create(@PathVariable(value = "organizationId") Long organizationId,
                                                      @RequestBody PlatformInfoMapping platformInfoMapping) {
        platformInfoMapping.setTenantId(organizationId);
        validObject(platformInfoMapping);
        platformInfoMappingService.save(platformInfoMapping);
        return Results.success(platformInfoMapping);
    }

    @ApiOperation(value = "平台信息匹配表维护-修改平台信息匹配表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    public ResponseEntity<PlatformInfoMapping> update(@PathVariable(value = "organizationId") Long organizationId,
                                                      @RequestBody PlatformInfoMapping platformInfoMapping) {
        platformInfoMapping.setTenantId(organizationId);
        SecurityTokenHelper.validToken(platformInfoMapping);
        platformInfoMappingService.save(platformInfoMapping);
        return Results.success(platformInfoMapping);
    }

    @ApiOperation(value = "平台信息匹配表维护-批量保存平台信息匹配表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/batch-saving")
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    public ResponseEntity<List<PlatformInfoMapping>> batchSave(@PathVariable(value = "organizationId") Long organizationId,
                                                               @RequestBody List<PlatformInfoMapping> platformInfoMappingList) {
        SecurityTokenHelper.validToken(platformInfoMappingList);
        platformInfoMappingService.batchSave(platformInfoMappingList);
        return Results.success(platformInfoMappingList);
    }

    @ApiOperation(value = "平台信息匹配表维护-删除平台信息匹配表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<Void> remove(@PathVariable(value = "organizationId") Long organizationId,
                                       @RequestBody List<PlatformInfoMapping> platformInfoMapping) {
        for (PlatformInfoMapping infMapping : platformInfoMapping) {
            infMapping.setTenantId(organizationId);
        }
        SecurityTokenHelper.validToken(platformInfoMapping);

        platformInfoMappingRepository.batchDeleteByPrimaryKey(platformInfoMapping);
        return Results.success();
    }

}
