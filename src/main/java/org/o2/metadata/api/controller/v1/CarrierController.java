package org.o2.metadata.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.app.service.CarrierDeliveryRangeService;
import org.o2.metadata.app.service.CarrierService;
import org.o2.metadata.config.EnableMetadata;
import org.o2.metadata.domain.entity.Carrier;
import org.o2.metadata.domain.entity.CarrierDeliveryRange;
import org.o2.metadata.domain.repository.CarrierDeliveryRangeRepository;
import org.o2.metadata.domain.repository.CarrierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 承运商 管理 API
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@RestController("carrierController.v1")
@RequestMapping("/v1/carriers")
@Api(tags = EnableMetadata.CARRIER)
public class CarrierController extends BaseController {
    @Autowired
    private CarrierRepository carrierRepository;
    @Autowired
    private CarrierService carrierService;
    @Autowired
    private CarrierDeliveryRangeRepository carrierDeliveryRangeRepository;
    @Autowired
    private CarrierDeliveryRangeService carrierDeliveryRangeService;


    @ApiOperation(value = "承运商列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping("/page-list")
    public ResponseEntity<?> list(final Carrier carrier, @ApiIgnore @SortDefault(
            value = Carrier.FIELD_CARRIER_NAME) final PageRequest pageRequest) {
        final Page<Carrier> list = PageHelper.doPage(pageRequest.getPage(), pageRequest.getSize(),
                () -> carrierRepository.listCarrier(carrier));
        return Results.success(list);
    }

    @ApiOperation(value = "承运商明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping("/detail")
    public ResponseEntity<?> detail(@RequestParam final Long carrierId) {
        final Carrier carrier = carrierRepository.selectByPrimaryKey(carrierId);
        return Results.success(carrier);
    }

    @ApiOperation(value = "批量新增或修改承运商")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<?> batchMerge(@RequestBody final List<Carrier> carrierList) {
        final List<Carrier> insertResult = carrierService.batchMerge(carrierList);
        return Results.success(insertResult);
    }

    @ApiOperation(value = "批量修改承运商")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<?> batchUpdate(@RequestBody final List<Carrier> carrierList) {
        SecurityTokenHelper.validToken(carrierList);
        final List<Carrier> insertResult = carrierService.batchUpdate(carrierList);
        return Results.success(insertResult);
    }

    @ApiOperation(value = "批量删除承运商")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody final List<Carrier> carrierList) {
        SecurityTokenHelper.validToken(carrierList);
        for (final Carrier carrier : carrierList) {
            if (carrier.getCarrierId() != null) {
                final List<CarrierDeliveryRange> list = carrierDeliveryRangeRepository.select(
                        CarrierDeliveryRange.FIELD_CARRIER_ID, carrier.getCarrierId());
                carrierDeliveryRangeRepository.batchDeleteByPrimaryKey(list);
            }
        }
        carrierRepository.batchDeleteByPrimaryKey(carrierList);
        return Results.success();
    }
}
