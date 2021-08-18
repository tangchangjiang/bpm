package org.o2.feignclient.metadata.infra.feign;

import io.swagger.annotations.ApiParam;
import org.o2.feignclient.metadata.domain.dto.CarrierMappingQueryInnerDTO;
import org.o2.feignclient.metadata.domain.dto.CarrierQueryInnerDTO;
import org.o2.feignclient.metadata.infra.constants.O2Service;
import org.o2.feignclient.metadata.infra.feign.fallback.CarrierRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * 承运商
 * @author yipeng.zhu@hand-china.com 2021-08-01
 **/
@FeignClient(
        value = O2Service.Metadata.NAME,
        path = "/v1",
        fallback = CarrierRemoteServiceImpl.class
)
public interface CarrierRemoteService {
    /**
     * 批量查询承运商
     * @param carrierQueryInnerDTO 承运商
     * @param organizationId 租户ID
     * @return map
     */
    @PostMapping("/{organizationId}/carrier-internal/list")
    ResponseEntity<String> listCarriers(@RequestBody CarrierQueryInnerDTO carrierQueryInnerDTO,
                                        @PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId);

    /**
     * 批量查询承运商匹配规则
     *
     * @param carrierMappingQueryInnerDTO 承运商
     * @param organizationId   租户ID
     * @return map key:carrierCode
     */
    @PostMapping("/{organizationId}/carrier-internal/mapping")
    ResponseEntity<String> listCarrierMappings(@RequestBody CarrierMappingQueryInnerDTO carrierMappingQueryInnerDTO,
                                              @PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId);
}
