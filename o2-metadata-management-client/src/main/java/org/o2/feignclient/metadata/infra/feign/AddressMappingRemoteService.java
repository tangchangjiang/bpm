package org.o2.feignclient.metadata.infra.feign;

import io.swagger.annotations.ApiParam;
import org.o2.feignclient.metadata.domain.dto.AddressMappingQueryIntDTO;
import org.o2.feignclient.metadata.infra.constants.O2Service;
import org.o2.feignclient.metadata.infra.feign.fallback.AddressMappingRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 *
 * 地址匹配
 *
 * @author yipeng.zhu@hand-china.com 2021-08-06
 **/
@FeignClient(
        value = O2Service.Metadata.NAME,
        path = "/v1",
        fallback = AddressMappingRemoteServiceImpl.class
)
public interface AddressMappingRemoteService {
    /**
     * 查询地址匹配
     * @param addressMappingQueryIntDTOList 查地址匹配
     * @param organizationId 租户ID
     * @return String
     */
    @PostMapping("/{organizationId}/address-mappings-internal/list")
    ResponseEntity<String> listAddressMappings(@RequestBody List<AddressMappingQueryIntDTO> addressMappingQueryIntDTOList,
                                               @PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId);
}
