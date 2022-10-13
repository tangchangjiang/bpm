package org.o2.metadata.management.client;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.metadata.management.client.domain.co.CarrierCO;
import org.o2.metadata.management.client.domain.co.CarrierDeliveryRangeCO;
import org.o2.metadata.management.client.domain.co.CarrierLogisticsCostCO;
import org.o2.metadata.management.client.domain.co.CarrierMappingCO;
import org.o2.metadata.management.client.domain.dto.CarrierDeliveryRangeDTO;
import org.o2.metadata.management.client.domain.dto.CarrierLogisticsCostDTO;
import org.o2.metadata.management.client.domain.dto.CarrierMappingQueryInnerDTO;
import org.o2.metadata.management.client.domain.dto.CarrierQueryInnerDTO;
import org.o2.metadata.management.client.infra.feign.CarrierRemoteService;

import java.util.List;
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


    /**
     * 租户id查询所有承运商数据
     *
     * @param tenantId 租户ID
     * @return map key:carrierName
     */
    public Map<String, CarrierCO> importList(Long tenantId) {
        return ResponseUtils.getResponse(carrierRemoteService.importList(tenantId), new TypeReference<Map<String, CarrierCO>>() {
        });
    }


    /**
     * 承运商物流成本计算
     *
     * @param carrierLogisticsCostDTO 参数
     * @return 计算结果
     */
    public List<CarrierLogisticsCostCO> calculateLogisticsCost(CarrierLogisticsCostDTO carrierLogisticsCostDTO, Long tenantId) {
        return ResponseUtils.getResponse(carrierRemoteService.calculateLogisticsCost(tenantId, carrierLogisticsCostDTO), new TypeReference<List<CarrierLogisticsCostCO>>() {
        });
    }

    /**
     * 查询收货地址是否在承运商的送达范围内
     *
     * @param carrierDeliveryRangeDTO 参数
     * @return 结果
     */
    public List<CarrierDeliveryRangeCO> checkDeliveryRange(CarrierDeliveryRangeDTO carrierDeliveryRangeDTO, Long tenantId) {
        return ResponseUtils.getResponse(carrierRemoteService.checkDeliveryRange(tenantId, carrierDeliveryRangeDTO), new TypeReference<List<CarrierDeliveryRangeCO>>() {
        });

    }

}
