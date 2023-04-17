package org.o2.metadata.console.app.service;

import org.o2.metadata.console.api.co.CarrierCO;
import org.o2.metadata.console.api.co.CarrierDeliveryRangeCO;
import org.o2.metadata.console.api.co.CarrierMappingCO;
import org.o2.metadata.console.api.dto.CarrierDeliveryRangeDTO;
import org.o2.metadata.console.api.dto.CarrierMappingQueryInnerDTO;
import org.o2.metadata.console.api.dto.CarrierQueryInnerDTO;
import org.o2.metadata.console.infra.entity.Carrier;

import java.util.List;
import java.util.Map;

/**
 * 承运商应用服务
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface CarrierService {

    /**
     * 更新承运商
     *
     * @param carrierList    承运商
     * @param organizationId    租户ID
     * @return 承运商集合
     */
    List<Carrier> batchMerge(Long organizationId, List<Carrier> carrierList);

    /**
     * 删除承运商
     * @param carrierList    承运商
     * @param organizationId    租户ID
     */
    void batchDelete(Long organizationId, List<Carrier> carrierList);

    /**
     * 批量查询承运商
     * @param carrierQueryInnerDTO 承运商
     * @param organizationId 租户ID
     * @return map
     */
    Map<String, CarrierCO> listCarriers(CarrierQueryInnerDTO carrierQueryInnerDTO, Long organizationId);

    /**
     * 批量查询承运商匹配规则
     * @param queryInnerDTO 承运商
     * @param organizationId 租户ID
     * @return map
     */
    Map<String, CarrierMappingCO> listCarrierMappings(CarrierMappingQueryInnerDTO queryInnerDTO, Long organizationId);


    /**
     * 批量查询承运商
     *
     * @param organizationId 租户ID
     * @return map
     */
    Map<String, CarrierCO> importListCarriers(Long organizationId);


    /**
     * 查询收货地址是否在承运商的送达范围内
     *
     * @param carrierDeliveryRangeDTO 参数
     * @return 结果
     */
    List<CarrierDeliveryRangeCO> checkDeliveryRange(CarrierDeliveryRangeDTO carrierDeliveryRangeDTO);

    /**
     * 多租户批量查询承运商
     *
     * @param carrierQueryInnerDTOMap 查询条件：tenantId：queryDTO
     * @return map tenantId:carrierCode:CarrierCO
     */
    Map<Long, Map<String, CarrierCO>> listCarriersBatchTenant(Map<Long, CarrierQueryInnerDTO> carrierQueryInnerDTOMap);
}
