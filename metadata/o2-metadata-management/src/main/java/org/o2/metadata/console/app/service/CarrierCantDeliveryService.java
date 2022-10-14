package org.o2.metadata.console.app.service;

import org.o2.metadata.console.infra.entity.CarrierCantDelivery;

import java.util.List;


/**
 * 承运商不可送达范围应用服务
 *
 * @author zhilin.ren@hand-china.com 2022-10-10 13:44:09
 */
public interface CarrierCantDeliveryService {


    /**
     * 保存承运商不可送达范围
     *
     * @param carrierCantDelivery 承运商不可送达范围对象
     * @return 承运商不可送达范围对象
     */
    CarrierCantDelivery save(CarrierCantDelivery carrierCantDelivery);


    /**
     * 查询承运商不可送达范围
     *
     * @param query 条件
     * @return 结果
     */
    List<CarrierCantDelivery> list(CarrierCantDelivery query);
}
