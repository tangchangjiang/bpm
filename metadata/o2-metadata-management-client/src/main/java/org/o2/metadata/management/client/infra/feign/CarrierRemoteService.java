package org.o2.metadata.management.client.infra.feign;

import io.swagger.annotations.ApiParam;
import org.o2.core.common.O2Service;
import org.o2.metadata.management.client.domain.dto.CarrierDeliveryRangeDTO;
import org.o2.metadata.management.client.domain.dto.CarrierMappingQueryInnerDTO;
import org.o2.metadata.management.client.domain.dto.CarrierQueryInnerDTO;
import org.o2.metadata.management.client.infra.feign.fallback.CarrierRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * 承运商
 *
 * @author yipeng.zhu@hand-china.com 2021-08-01
 **/
@FeignClient(
        value = O2Service.MetadataManagement.NAME,
        path = "/v1",
        fallback = CarrierRemoteServiceImpl.class
)
public interface CarrierRemoteService {
    /**
     * 批量查询承运商
     *
     * @param carrierQueryInnerDTO 承运商
     * @param organizationId       租户ID
     * @return map
     */
    @PostMapping("/{organizationId}/carrier-internal/list")
    ResponseEntity<String> listCarriers(@RequestBody CarrierQueryInnerDTO carrierQueryInnerDTO,
                                        @PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId);

    /**
     * 多租户批量查询承运商
     *
     * @param carrierQueryInnerDTOMap 查询条件
     * @return  承运商信息
     */
    @PostMapping("/carrier-internal/list-batch-tenant")
    ResponseEntity<String> listCarriersBatchTenant(@RequestBody Map<String, CarrierQueryInnerDTO> carrierQueryInnerDTOMap);

    /**
     * 批量查询承运商匹配规则
     *
     * @param carrierMappingQueryInnerDTO 承运商
     * @param organizationId              租户ID
     * @return map key:carrierCode
     */
    @PostMapping("/{organizationId}/carrier-internal/mapping")
    ResponseEntity<String> listCarrierMappings(@RequestBody CarrierMappingQueryInnerDTO carrierMappingQueryInnerDTO,
                                               @PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId);

    /**
     * 查询对应租户所有承运商数据
     *
     * @param organizationId 租户ID
     * @return map
     */
    @PostMapping("/{organizationId}/carrier-internal/import-list")
    ResponseEntity<String> importList(@PathVariable(value = "organizationId") @ApiParam(value = "租户ID", required = true) Long organizationId);


    @PostMapping("/{organizationId}/carrier-internal/check-delivery-range")
    ResponseEntity<String> checkDeliveryRange(@PathVariable @ApiParam(value = "租户ID", required = true) Long organizationId,
                                              @RequestBody CarrierDeliveryRangeDTO carrierDeliveryRangeDTO);
}
