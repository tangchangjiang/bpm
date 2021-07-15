package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
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
import org.o2.metadata.console.app.service.CarrierService;
import org.o2.metadata.console.config.EnableMetadataConsole;
import org.o2.metadata.console.infra.entity.Carrier;
import org.o2.metadata.console.infra.repository.CarrierRepository;
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
@RequestMapping("/v1/{organizationId}/carriers")
@Api(tags = EnableMetadataConsole.CARRIER)
public class CarrierController extends BaseController {
    @Autowired
    private CarrierRepository carrierRepository;
    @Autowired
    private CarrierService carrierService;


    @ApiOperation(value = "承运商列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping("/page-list")
    public ResponseEntity<?> list(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, final Carrier carrier, @ApiIgnore @SortDefault(
            value = Carrier.FIELD_CARRIER_NAME) final PageRequest pageRequest) {
        carrier.setTenantId(organizationId);
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
    public ResponseEntity<?> batchMerge(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody final List<Carrier> carrierList) {
        final List<Carrier> insertResult = carrierService.batchMerge(organizationId, carrierList);
        return Results.success(insertResult);
    }

    @ApiOperation(value = "批量修改承运商")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<?> batchUpdate(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody final List<Carrier> carrierList) {
        SecurityTokenHelper.validToken(carrierList);
        final List<Carrier> insertResult = carrierService.batchUpdate(organizationId, carrierList);
        return Results.success(insertResult);
    }

    @ApiOperation(value = "批量删除承运商")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody final List<Carrier> carrierList) {
        SecurityTokenHelper.validToken(carrierList);
        carrierService.batchDelete(organizationId,carrierList);
        return Results.success();
    }
}
