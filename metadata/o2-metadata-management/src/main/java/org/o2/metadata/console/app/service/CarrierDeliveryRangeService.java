package org.o2.metadata.console.app.service;

import org.o2.metadata.console.infra.entity.CarrierDeliveryRange;

import java.util.List;

/**
 * 承运商送达范围应用服务
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface CarrierDeliveryRangeService {
    /**
     * 查询承运商范围
     *
     * @param carrierDeliveryRange 承运商送达范围资源库
     * @return 承运商范围结果集
     */
    List<CarrierDeliveryRange> listCarrierDeliveryRanges(CarrierDeliveryRange carrierDeliveryRange);

    /**
     * 批量新增或更新承运商送达范围
     *
     * @param carrierDeliveryRanges 承运商送达范围
     * @param organizationId        租户ID
     * @return 结果集
     */
    List<CarrierDeliveryRange> batchMerge(Long organizationId, List<CarrierDeliveryRange> carrierDeliveryRanges);

    /**
     * 承运商送达范围明细获取
     *
     * @param deliveryRangeId meaning
     * @param tenantId        租户ID
     * @return the return
     */
    CarrierDeliveryRange carrierDeliveryRangeDetail(Long deliveryRangeId, Long tenantId);
}
