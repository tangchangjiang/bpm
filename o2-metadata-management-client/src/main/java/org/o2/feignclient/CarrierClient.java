package org.o2.feignclient;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.feignclient.metadata.domain.co.CarrierCO;
import org.o2.feignclient.metadata.domain.co.CarrierMappingCO;
import org.o2.feignclient.metadata.domain.dto.CarrierMappingQueryInnerDTO;
import org.o2.feignclient.metadata.domain.dto.CarrierQueryInnerDTO;
import org.o2.feignclient.metadata.infra.feign.CarrierRemoteService;

import java.util.Map;

/**
 *
 * 承运商=
 *
 * @author yipeng.zhu@hand-china.com 2021-09-23
 **/
public class CarrierClient {
    private final CarrierRemoteService carrierRemoteService;

    public CarrierClient(CarrierRemoteService carrierRemoteService) {
        this.carrierRemoteService = carrierRemoteService;
    }

    /**
     * 批量查询承运商
     *
     * @param carrierQueryInnerDTO 承运商
     * @param tenantId   租户ID
     * @return map key:carrierCode
     */
    public Map<String, CarrierCO> listCarriers(CarrierQueryInnerDTO carrierQueryInnerDTO, Long tenantId) {
        return ResponseUtils.getResponse(carrierRemoteService.listCarriers(carrierQueryInnerDTO, tenantId), new TypeReference<Map<String, CarrierCO>>() {
        });
    }

    /**
     * 批量查询承运商匹配规则
     *
     * @param carrierMappingQueryInnerDTO 承运商
     * @param tenantId   租户ID
     * @return map key:carrierCode
     */
    public Map<String, CarrierMappingCO> listCarrierMappings(CarrierMappingQueryInnerDTO carrierMappingQueryInnerDTO, Long tenantId) {
        return ResponseUtils.getResponse(carrierRemoteService.listCarrierMappings(carrierMappingQueryInnerDTO, tenantId), new TypeReference<Map<String, CarrierMappingCO>>() {
        });
    }
}
