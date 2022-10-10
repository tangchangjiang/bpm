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
import org.o2.core.response.OperateResponse;
import org.o2.metadata.console.app.service.CarrierService;
import org.o2.metadata.console.infra.config.MetadataManagementAutoConfiguration;
import org.o2.metadata.console.infra.entity.Carrier;
import org.o2.metadata.console.infra.repository.CarrierRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 承运商 管理 API
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@RestController("carrierController.v1")
@RequestMapping("/v1/{organizationId}/carriers")
@Api(tags = MetadataManagementAutoConfiguration.CARRIER)
public class CarrierController extends BaseController {
    private final CarrierRepository carrierRepository;
    private final CarrierService carrierService;

    public CarrierController(CarrierRepository carrierRepository, CarrierService carrierService) {
        this.carrierRepository = carrierRepository;
        this.carrierService = carrierService;
    }


    @ApiOperation(value = "承运商列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping("/page-list")
    public ResponseEntity<Page<Carrier>> list(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, final Carrier carrier, final PageRequest pageRequest) {
        carrier.setTenantId(organizationId);
        final Page<Carrier> list = PageHelper.doPage(pageRequest.getPage(), pageRequest.getSize(),
                () -> carrierRepository.listCarrier(carrier));
        return Results.success(list);
    }

    @ApiOperation(value = "承运商明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping("/detail")
    public ResponseEntity<Carrier> detail(@RequestParam final Long carrierId) {
        final Carrier carrier = carrierRepository.selectByPrimaryKey(carrierId);
        return Results.success(carrier);
    }

    @ApiOperation(value = "批量新增")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<List<Carrier>> batchMerge(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody final List<Carrier> carrierList) {
         List<Carrier> insertResult = carrierService.batchMerge(organizationId, carrierList);
        return Results.success(insertResult);
    }


    @ApiOperation(value = "批量删除承运商")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<OperateResponse> remove(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody final List<Carrier> carrierList) {
        SecurityTokenHelper.validToken(carrierList);
        carrierService.batchDelete(organizationId,carrierList);
        return Results.success(OperateResponse.success());
    }
}