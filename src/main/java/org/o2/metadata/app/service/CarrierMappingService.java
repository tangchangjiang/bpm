package org.o2.metadata.app.service;

import org.o2.metadata.domain.entity.CarrierMapping;

import java.util.List;
import java.util.Map;

/**
 * 承运商匹配表应用服务
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface CarrierMappingService {

    /**
     * 批量插入数据
     *
     * @param carrierMappings 原数据
     * @return
     */
    Map<String, Object> insertAll(List<CarrierMapping> carrierMappings);
}
