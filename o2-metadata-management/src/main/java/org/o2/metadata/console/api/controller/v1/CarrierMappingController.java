package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.console.api.dto.CarrierMappingQueryDTO;
import org.o2.metadata.console.app.service.CarrierMappingService;
import org.o2.metadata.console.infra.config.MetadataManagementAutoConfiguration;
import org.o2.metadata.console.infra.entity.CarrierMapping;
import org.o2.metadata.console.infra.repository.CarrierMappingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 承运商匹配表 管理 API
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@RestController("carrierMappingController.v1")
@RequestMapping("/v1/{organizationId}/carrier-mappings")
@Api(tags = MetadataManagementAutoConfiguration.CARRIER_MAPPING)
public class CarrierMappingController extends BaseController {
    private final CarrierMappingRepository carrierMappingRepository;
    private final CarrierMappingService carrierMappingService;

    public CarrierMappingController(final CarrierMappingRepository carrierMappingRepository,
                                    final CarrierMappingService carrierMappingService) {
        this.carrierMappingRepository = carrierMappingRepository;
        this.carrierMappingService = carrierMappingService;
    }

    @ApiOperation(value = "承运商匹配表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping
    public ResponseEntity<Page<CarrierMapping>> list(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, final CarrierMappingQueryDTO carrierMappingQueryDTO, @ApiIgnore final PageRequest pageRequest) {
        carrierMappingQueryDTO.setTenantId(organizationId);
        final Page<CarrierMapping> list = PageHelper.doPageAndSort(pageRequest,
                () -> carrierMappingRepository.listCarrierMappingByCondition(carrierMappingQueryDTO));
        return Results.success(list);
    }

    @ApiOperation(value = "承运商匹配表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{carrierMappingId}")
    public ResponseEntity<?> detail(@PathVariable final Long carrierMappingId) {
        final CarrierMapping carrierMapping = carrierMappingRepository.selectByPrimaryKey(carrierMappingId);
        return Results.success(carrierMapping);
    }

    @ApiOperation(value = "批量新增或修改承运商匹配表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<CarrierMapping> create(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody CarrierMapping carrierMapping) {
        carrierMappingService.insertCarrierMapping(organizationId, carrierMapping);
        return Results.success(carrierMapping);

    }

    @ApiOperation(value = "修改承运商匹配表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<?> update(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody final CarrierMapping carrierMapping) {
        SecurityTokenHelper.validToken(carrierMapping);
        carrierMapping.setTenantId(organizationId);
        carrierMappingRepository.updateByPrimaryKeySelective(carrierMapping);
        return Results.success(carrierMapping);
    }

    @ApiOperation(value = "删除承运商匹配表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody final List<CarrierMapping> carrierMappings) {
        SecurityTokenHelper.validToken(carrierMappings);
        carrierMappingRepository.batchDeleteByPrimaryKey(carrierMappings);
        return Results.success();
    }

}
