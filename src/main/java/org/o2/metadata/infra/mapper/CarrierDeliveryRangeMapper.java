package org.o2.metadata.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.o2.ext.metadata.domain.entity.CarrierDeliveryRange;

import java.util.List;

/**
 * 承运商送达范围Mapper
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface CarrierDeliveryRangeMapper extends BaseMapper<CarrierDeliveryRange> {
    /**
     * 查询承运商范围
     *
     * @param carrierDeliveryRange 承运商送达范围资源库
     * @return 承运商范围列表
     */
    List<CarrierDeliveryRange> list(CarrierDeliveryRange carrierDeliveryRange);
}
