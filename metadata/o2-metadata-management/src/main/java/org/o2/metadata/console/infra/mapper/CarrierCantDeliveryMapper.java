package org.o2.metadata.console.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.o2.metadata.console.infra.entity.CarrierCantDelivery;

import java.util.List;

/**
 * 承运商不可送达范围Mapper
 *
 * @author zhilin.ren@hand-china.com 2022-10-10 13:44:09
 */
public interface CarrierCantDeliveryMapper extends BaseMapper<CarrierCantDelivery> {

    /**
     * 查询承运商不可送达范围
     *
     * @param query 条件
     * @return 结果
     */
    List<CarrierCantDelivery> list(CarrierCantDelivery query);
}
