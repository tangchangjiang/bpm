package org.o2.metadata.console.api.controller.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.console.api.dto.CarrierDeliveryRangeSaveDTO;
import org.o2.metadata.console.app.service.CarrierDeliveryRangeService;
import org.o2.metadata.console.infra.config.MetadataManagementAutoConfiguration;
import org.o2.metadata.console.infra.entity.CarrierDeliveryRange;
import org.o2.metadata.console.infra.repository.CarrierDeliveryRangeRepository;
import org.o2.metadata.console.infra.repository.CountryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;


import java.util.List;
import java.util.stream.Collectors;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;


/**
 * 承运商送达范围 管理 API
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */

@RestController("carrierDeliveryRangeController.v1")
@RequestMapping("/v1/{organizationId}/carrier-delivery-ranges")
@Api(tags = MetadataManagementAutoConfiguration.CARRIER_DELIVERY_RANGE)
public class CarrierDeliveryRangeController extends BaseController {
    private final CarrierDeliveryRangeRepository carrierDeliveryRangeRepository;
    private final CarrierDeliveryRangeService carrierDeliveryRangeService;
    private final CountryRepository countryRepository;

    public CarrierDeliveryRangeController(final CarrierDeliveryRangeRepository carrierDeliveryRangeRepository,
                                          final CarrierDeliveryRangeService carrierDeliveryRangeService, CountryRepository countryRepository) {
        this.carrierDeliveryRangeRepository = carrierDeliveryRangeRepository;
        this.carrierDeliveryRangeService = carrierDeliveryRangeService;
        this.countryRepository = countryRepository;
    }

    @ApiOperation(value = "承运商送达范围列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/page-list")
    public ResponseEntity<Page<CarrierDeliveryRange>> list(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, final CarrierDeliveryRange carrierDeliveryRange,
                                  @ApiIgnore @SortDefault(value = CarrierDeliveryRange.FIELD_DELIVERY_RANGE_ID,
                                          direction = Sort.Direction.DESC) final PageRequest pageRequest) {
        carrierDeliveryRange.setTenantId(organizationId);
        final Page<CarrierDeliveryRange> list = PageHelper.doPage(pageRequest.getPage(), pageRequest.getSize(),
                () -> carrierDeliveryRangeService.listCarrierDeliveryRanges(carrierDeliveryRange));
        return Results.success(list);
    }

    @ApiOperation(value = "承运商送达范围明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/detail")
    public ResponseEntity<CarrierDeliveryRange> detail(@RequestParam final Long deliveryRangeId,
                                    @PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId) {
        return Results.success(carrierDeliveryRangeService.carrierDeliveryRangeDetail(deliveryRangeId,organizationId));
    }

    @ApiOperation(value = "批量创建或新增承运商送达范围")
    @Permission(permissionPublic =true,level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<List<CarrierDeliveryRange>> batchMerge(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody final List<CarrierDeliveryRange> carrierDeliveryRanges) {
        final List<CarrierDeliveryRange> resultList = carrierDeliveryRangeService.batchMerge(organizationId, carrierDeliveryRanges);
        return Results.success(resultList);
    }

    @ApiOperation(value = "批量删除承运商送达范围")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<String> remove(@RequestBody final List<CarrierDeliveryRange> carrierDeliveryRanges) {
        SecurityTokenHelper.validToken(carrierDeliveryRanges);
        carrierDeliveryRangeRepository.batchDeleteByPrimaryKey(carrierDeliveryRanges);
        return Results.success(BaseConstants.FIELD_SUCCESS);
    }
}
