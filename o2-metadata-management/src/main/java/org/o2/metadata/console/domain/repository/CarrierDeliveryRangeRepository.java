package org.o2.metadata.console.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.console.infra.entity.CarrierDeliveryRange;

import java.util.List;

/**
 * 承运商送达范围资源库
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface CarrierDeliveryRangeRepository extends BaseRepository<CarrierDeliveryRange> {
    /**
     * 查询承运商范围
     *
     * @param carrierDeliveryRange 承运商送达范围资源库
     * @return 承运商范围结果集
     */
    List<CarrierDeliveryRange> list(CarrierDeliveryRange carrierDeliveryRange);

    /**
     * 承运商送达范围明细获取
     * @param deliveryRangeId meaning
     * @return the return
     * @throws RuntimeException exception description
     */
    CarrierDeliveryRange detail(Long deliveryRangeId);
}
