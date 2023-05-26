package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.core.response.OperateResponse;
import org.o2.metadata.console.app.service.CarrierCantDeliveryService;
import org.o2.metadata.console.infra.entity.CarrierCantDelivery;
import org.o2.metadata.console.infra.repository.CarrierCantDeliveryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 承运商不可送达范围 管理 API 站点级
 *
 * @author chao.yang05@hand-china.com 2023-04-17
 */
@RestController("carrierCantDeliverySiteController.v1")
@RequestMapping("/v1/carrier-cant-deliverys")
@RequiredArgsConstructor
public class CarrierCantDeliverySiteController extends BaseController {

    private final CarrierCantDeliveryService carrierCantDeliveryService;
    private final CarrierCantDeliveryRepository carrierCantDeliveryRepository;

    @ApiOperation(value = "承运商不可送达范围维护-分页查询承运商不可送达范围列表（站点级）")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping
    public ResponseEntity<Page<CarrierCantDelivery>> page(CarrierCantDelivery carrierCantDelivery,
                                                          @ApiIgnore @SortDefault(value = CarrierCantDelivery.FIELD_CARRIER_CANT_DELIVERY_ID,
                                                                  direction = Sort.Direction.DESC) PageRequest pageRequest) {
        if (null == carrierCantDelivery.getTenantId()) {
            carrierCantDelivery.setTenantId(BaseConstants.DEFAULT_TENANT_ID);
        }
        Page<CarrierCantDelivery> list = PageHelper.doPageAndSort(pageRequest, () -> carrierCantDeliveryService.list(carrierCantDelivery));
        return Results.success(list);
    }

    @ApiOperation(value = "承运商不可送达范围维护-查询承运商不可送达范围明细")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping("/{carrierCantDeliveryId}")
    public ResponseEntity<CarrierCantDelivery> detail(@ApiParam(value = "承运商不可送达范围ID", required = true) @PathVariable Long carrierCantDeliveryId) {
        CarrierCantDelivery carrierCantDelivery = carrierCantDeliveryRepository.selectByPrimaryKey(carrierCantDeliveryId);
        return Results.success(carrierCantDelivery);
    }

    @ApiOperation(value = "承运商不可送达范围维护-创建承运商不可送达范围")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping
    public ResponseEntity<CarrierCantDelivery> create(@RequestBody CarrierCantDelivery carrierCantDelivery) {
        carrierCantDelivery.setTenantId(BaseConstants.DEFAULT_TENANT_ID);
        validObject(carrierCantDelivery);
        carrierCantDeliveryService.save(carrierCantDelivery);
        return Results.success(carrierCantDelivery);
    }

    @ApiOperation(value = "承运商不可送达范围维护-修改承运商不可送达范围")
    @Permission(level = ResourceLevel.SITE)
    @PutMapping
    public ResponseEntity<CarrierCantDelivery> update(@RequestBody CarrierCantDelivery carrierCantDelivery) {
        carrierCantDelivery.setTenantId(BaseConstants.DEFAULT_TENANT_ID);
        SecurityTokenHelper.validToken(carrierCantDelivery);
        carrierCantDeliveryService.save(carrierCantDelivery);
        return Results.success(carrierCantDelivery);
    }

    @ApiOperation(value = "承运商不可送达范围维护-删除承运商不可送达范围")
    @Permission(level = ResourceLevel.SITE)
    @DeleteMapping
    public ResponseEntity<OperateResponse> remove(@RequestBody List<CarrierCantDelivery> carrierCantDeliveryList) {
        SecurityTokenHelper.validToken(carrierCantDeliveryList);
        carrierCantDeliveryRepository.batchDeleteByPrimaryKey(carrierCantDeliveryList);
        return Results.success(OperateResponse.success());
    }
}
