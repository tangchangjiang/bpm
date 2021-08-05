package org.o2.metadata.domain.carrier.repository;

import org.o2.metadata.domain.carrier.domain.CarrierDO;

import java.util.List;

/**
 *
 * 承运商
 *
 * @author yipeng.zhu@hand-china.com 2021-08-5
 **/
public interface CarrierDomainRepository {
    /**
     * 查询承运商
     * @param tenantId 租户ID
     * @return  list
     */
    List<CarrierDO> listCarriers(Long tenantId);
}
