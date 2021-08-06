package org.o2.metadata.console.api.controller.v1;


import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.hzero.core.util.Results;
import org.o2.metadata.console.api.dto.AddressMappingQueryIntDTO;
import org.o2.metadata.console.api.vo.AddressMappingVO;
import org.o2.metadata.console.app.service.AddressMappingService;
import org.o2.metadata.console.config.MetadataManagementAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    @Permission(permissionWithin = true,level = ResourceLevel.ORGANIZATION)
    @PostMapping("/list")
    public ResponseEntity<Map<String, AddressMappingVO>> listAddressMappings(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                                                             @RequestBody List<AddressMappingQueryIntDTO> addressMappingQueryIntDTOList){
        List<AddressMappingVO> list =  addressMappingService.listAddressMappings(addressMappingQueryIntDTOList,organizationId);
        if (list.isEmpty()) {
            return Results.success(new HashMap<>(16));
        }
        Map<String, AddressMappingVO> maps = list.stream().collect(Collectors.toMap(AddressMappingVO::getAddressTypeCode, Function.identity(), (v1, v2)->v1));
        return  Results.success(maps);
    }

}