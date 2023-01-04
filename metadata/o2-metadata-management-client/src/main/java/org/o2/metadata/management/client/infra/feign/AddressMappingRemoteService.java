package org.o2.metadata.management.client.infra.feign;

import io.swagger.annotations.ApiParam;
import org.o2.core.common.O2Service;
import org.o2.metadata.management.client.domain.dto.AddressMappingQueryInnerDTO;
import org.o2.metadata.management.client.domain.dto.InsideAddressMappingDTO;
import org.o2.metadata.management.client.domain.dto.OutAddressMappingInnerDTO;
import org.o2.metadata.management.client.infra.feign.fallback.AddressMappingRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 地址匹配
 *
 * @author yipeng.zhu@hand-china.com 2021-08-06
 **/
@FeignClient(
        value = O2Service.MetadataManagement.NAME,
        path = "/v1",
        fallback = AddressMappingRemoteServiceImpl.class
)
public interface AddressMappingRemoteService {
    /**
     * 通过外部地区名称查询地址匹配
     *
     * @param queryInnerDTO  查地址匹配
     * @param organizationId 租户ID
     * @return String
     */
    @PostMapping("/{organizationId}/address-mappings-internal/list")
    ResponseEntity<String> listAllAddressMappings(@RequestBody AddressMappingQueryInnerDTO queryInnerDTO,
                                                  @PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId);

    /**
     * 查询临近省
     *
     * @param organizationId 租户ID
     * @return String
     */
    @GetMapping("/{organizationId}/neighboring-regions-internal")
    ResponseEntity<String> listNeighboringRegions(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId);

    /**
     * 通过外部编码查询地址匹配
     * @param queryInnerDTO  查地址匹配
     * @param organizationId 租户ID
     * @return String
     */
    @GetMapping("/{organizationId}/address-mappings-internal/list-address-by-code")
    ResponseEntity<String> listAddressMappingByCode(@RequestBody InsideAddressMappingDTO queryInnerDTO,
                                                  @PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId);



    @PostMapping("/{organizationId}/address-mappings-internal/list-out-address")
    ResponseEntity<String> listOutAddress(@RequestBody List<OutAddressMappingInnerDTO> queryInnerDTO,
                                                          @PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId);
}
