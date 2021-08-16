package org.o2.metadata.console.app.service;

import org.o2.metadata.console.infra.entity.CarrierMapping;


/**
 * 承运商匹配表应用服务
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface CarrierMappingService {

    /**
     * 批量插入数据
     * @param organizationId 租户ID
     * @param carrierMappings 原数据
     * @return
     */
    void insertCarrierMapping(Long organizationId, CarrierMapping carrierMappings);
}
