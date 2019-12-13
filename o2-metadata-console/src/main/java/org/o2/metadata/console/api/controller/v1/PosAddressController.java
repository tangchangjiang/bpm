package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.console.config.EnableMetadataConsole;
import org.o2.metadata.core.domain.entity.PosAddress;
import org.o2.metadata.core.domain.repository.PosAddressRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 详细地址 管理 API
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@RestController("detailedAddressController.v1")
@RequestMapping("/v1/{organizationId}/pos-address")
@Api(tags = EnableMetadataConsole.POS_ADDRESS)
public class PosAddressController extends BaseController {
    private final PosAddressRepository posAddressRepository;

    public PosAddressController(final PosAddressRepository posAddressRepository) {
        this.posAddressRepository = posAddressRepository;
    }

    @ApiOperation(value = "详细地址列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<?> list(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                  final PosAddress posAddress,
                                  @ApiIgnore @SortDefault(value = PosAddress.FIELD_POS_ADDRESS_ID, direction = Sort.Direction.DESC) final PageRequest pageRequest) {
        posAddress.setTenantId(organizationId);
        return Results.success(posAddressRepository.pageAndSort(pageRequest, posAddress));
    }

    @ApiOperation(value = "详细地址明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{addressId}")
    public ResponseEntity<?> detail(@PathVariable final Long addressId) {
        return Results.success(posAddressRepository.findDetailedAddressById(addressId));
    }

    @ApiOperation(value = "创建详细地址")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<?> create(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody final PosAddress posAddress) {
        posAddress.setTenantId(organizationId);
        posAddressRepository.insertSelective(posAddress);
        return Results.success(posAddress);
    }

    @ApiOperation(value = "修改详细地址")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<?> update(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody final PosAddress posAddress) {
        SecurityTokenHelper.validToken(posAddress);
        posAddress.setTenantId(organizationId);
        posAddressRepository.updateByPrimaryKeySelective(posAddress);
        return Results.success(posAddress);
    }

    @ApiOperation(value = "删除详细地址")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId, @RequestBody final PosAddress posAddress) {
        SecurityTokenHelper.validToken(posAddress);
        posAddress.setTenantId(organizationId);
        posAddressRepository.deleteByPrimaryKey(posAddress);
        return Results.success();
    }


}