package org.o2.metadata.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.app.service.CarrierDeliveryRangeService;
import org.o2.metadata.config.MetadataSwagger;
import org.o2.metadata.domain.entity.CarrierDeliveryRange;
import org.o2.metadata.domain.repository.CarrierDeliveryRangeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;


/**
 * 承运商送达范围 管理 API
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */

@RestController("carrierDeliveryRangeController.v1")
@RequestMapping("/v1/{tenantId}/carrier-delivery-ranges")
@Api(tags = MetadataSwagger.CARRIER_DELIVERY_RANGE)
public class CarrierDeliveryRangeController extends BaseController {
    private final CarrierDeliveryRangeRepository carrierDeliveryRangeRepository;
    private final CarrierDeliveryRangeService carrierDeliveryRangeService;

    public CarrierDeliveryRangeController(final CarrierDeliveryRangeRepository carrierDeliveryRangeRepository,
                                          final CarrierDeliveryRangeService carrierDeliveryRangeService) {
        this.carrierDeliveryRangeRepository = carrierDeliveryRangeRepository;
        this.carrierDeliveryRangeService = carrierDeliveryRangeService;
    }

    @ApiOperation(value = "承运商送达范围列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/page-list")
    public ResponseEntity<?> list(final CarrierDeliveryRange carrierDeliveryRange,
                                  @ApiIgnore @SortDefault(value = CarrierDeliveryRange.FIELD_DELIVERY_RANGE_ID,
                                          direction = Sort.Direction.DESC) final PageRequest pageRequest) {
        final Page<CarrierDeliveryRange> list = PageHelper.doPage(pageRequest.getPage(), pageRequest.getSize(),
                () -> carrierDeliveryRangeRepository.list(carrierDeliveryRange));
        return Results.success(list);
    }

    @ApiOperation(value = "承运商送达范围明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/detail")
    public ResponseEntity<?> detail(@RequestParam final Long deliveryRangeId) {
        return Results.success(carrierDeliveryRangeRepository.detail(deliveryRangeId));
    }

    @ApiOperation(value = "批量创建或新增承运商送达范围")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<?> batchMerge(@RequestBody final List<CarrierDeliveryRange> carrierDeliveryRanges) {
        final List<CarrierDeliveryRange> resultList = carrierDeliveryRangeService.batchMerge(carrierDeliveryRanges);
        return Results.success(resultList);
    }

    @ApiOperation(value = "批量删除承运商送达范围")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody final List<CarrierDeliveryRange> carrierDeliveryRanges) {
        SecurityTokenHelper.validToken(carrierDeliveryRanges);
        carrierDeliveryRangeRepository.batchDeleteByPrimaryKey(carrierDeliveryRanges);
        return Results.success();
    }
}
