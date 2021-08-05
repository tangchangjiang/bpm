package org.o2.metadata.infra.repository.impl;

import org.o2.metadata.domain.carrier.domain.CarrierDO;
import org.o2.metadata.domain.carrier.repository.CarrierDomainRepository;

import java.util.List;

/**
 *
 * 承运商
 *
 * @author yipeng.zhu@hand-china.com 2021-08-05
 **/

public class CarrierDomainRepositoryImpl implements CarrierDomainRepository {
    @Override
    public List<CarrierDO> listCarriers(Long tenantId) {
        return null;
    }
}
