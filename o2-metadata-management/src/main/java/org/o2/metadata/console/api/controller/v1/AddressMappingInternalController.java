package org.o2.metadata.console.api.controller.v1;


import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.util.Results;
import org.o2.metadata.console.api.co.AddressMappingCO;
import org.o2.metadata.console.api.dto.AddressMappingQueryInnerDTO;
import org.o2.metadata.console.app.service.AddressMappingService;
import org.o2.metadata.console.infra.config.MetadataManagementAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 地址匹配 管理 API
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@RestController("addressMappingInternalController.v1")
@RequestMapping("/v1/{organizationId}/address-mappings-internal")
@Api(tags = MetadataManagementAutoConfiguration.ADDRESS_MAPPING)
public class AddressMappingInternalController {
    private final AddressMappingService addressMappingService;

    public AddressMappingInternalController(AddressMappingService addressMappingService) {
        this.addressMappingService = addressMappingService;
    }


    @ApiOperation(value = "批量查询地址批量表")
    @Permission(permissionPublic = true,level = ResourceLevel.ORGANIZATION)
    @PostMapping("/list")
    public ResponseEntity<Map<String, AddressMappingCO>> listAddressMappings(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                             @RequestBody List<AddressMappingQueryInnerDTO> addressMappingQueryInnerDTOList){
        return  Results.success(addressMappingService.listAddressMappings(addressMappingQueryInnerDTOList,organizationId));
    }


    @ApiOperation(value = "全量查询地址批量表")
    @Permission(permissionPublic = true,level = ResourceLevel.ORGANIZATION)
    @PostMapping("/list-all")
    public ResponseEntity<Map<String, AddressMappingCO>> listAllAddressMapping(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                             @RequestParam("platformCode") String platformCode){
        return  Results.success(addressMappingService.listAddressMappings(platformCode,organizationId));
    }

}
