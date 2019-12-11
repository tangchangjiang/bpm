package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections4.MapUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.console.app.service.CarrierMappingService;
import org.o2.metadata.console.config.MetadataSwagger;
import org.o2.metadata.core.domain.entity.CarrierMapping;
import org.o2.metadata.core.domain.repository.CarrierMappingRepository;
import org.o2.metadata.core.domain.vo.CarrierMappingVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 承运商匹配表 管理 API
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@RestController("carrierMappingController.v1")
@RequestMapping("/v1/{organizationId}/carrier-mappings")
@Api(tags = MetadataSwagger.CARRIER_MAPPING)
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
    public ResponseEntity<?> list(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, final CarrierMappingVO carrierMappingVO, @ApiIgnore final PageRequest pageRequest) {
        carrierMappingVO.setTenantId(organizationId);
        final Page<CarrierMappingVO> list = PageHelper.doPageAndSort(pageRequest,
                () -> carrierMappingRepository.listCarrierMappingByCondition(carrierMappingVO));
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
    public ResponseEntity<?> create(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,@RequestBody List<CarrierMapping> carrierMappings) {
        final Map<String, Object> resultMap = carrierMappingService.insertAll(organizationId,carrierMappings);
        if (MapUtils.isEmpty(resultMap)) {
            return Results.success(Collections.EMPTY_LIST);
        } else {
            final String resultString = "平台和承运商编码不可重复" + "，成功保存"
                    + resultMap.get("平台和承运商编码不可重复") + "条数据";
            return Results.success(Collections.singletonList(resultString));
        }
    }

    @ApiOperation(value = "修改承运商匹配表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<?> update(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,@RequestBody final CarrierMapping carrierMapping) {
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
