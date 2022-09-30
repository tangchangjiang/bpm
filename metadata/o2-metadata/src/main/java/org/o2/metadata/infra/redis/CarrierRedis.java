package org.o2.metadata.infra.redis;


import org.o2.metadata.infra.entity.Carrier;

import java.util.List;

/**
 *
 * 承运商
 *
 * @author yipeng.zhu@hand-china.com 2021-08-05
 **/
public interface CarrierRedis {
    /**
     * 批量更新redis
     * @param tenantId 租户ID
     */
    List<Carrier> listCarriers(Long tenantId);
}
