package org.o2.metadata.app.service;

import org.o2.metadata.domain.entity.CarrierDeliveryRange;

import java.util.List;

/**
 * 承运商送达范围应用服务
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface CarrierDeliveryRangeService {
    /**
     * 批量新增或更新承运商送达范围
     *
     * @param carrierDeliveryRanges 承运商送达范围
     * @return 结果集
     */
    List<CarrierDeliveryRange> batchMerge(List<CarrierDeliveryRange> carrierDeliveryRanges);
}
