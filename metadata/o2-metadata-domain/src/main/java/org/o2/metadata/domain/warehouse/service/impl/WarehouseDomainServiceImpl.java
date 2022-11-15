package org.o2.metadata.domain.warehouse.service.impl;

import org.o2.metadata.domain.warehouse.domain.WarehouseDO;
import org.o2.metadata.domain.warehouse.repository.WarehouseDomainRepository;
import org.o2.metadata.domain.warehouse.service.WarehouseDomainService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * 仓库
 *
 * @author yipeng.zhu@hand-china.com 2021-07-13
 **/
@Service
public class WarehouseDomainServiceImpl implements WarehouseDomainService {
    private final WarehouseDomainRepository warehouseDomainRepository;

    public WarehouseDomainServiceImpl(WarehouseDomainRepository warehouseDomainRepository) {
        this.warehouseDomainRepository = warehouseDomainRepository;
    }

    @Override
    public List<WarehouseDO> listWarehouses(List<String> warehouseCodes, Long tenantId) {
        return warehouseDomainRepository.listWarehouses(warehouseCodes, tenantId);
    }
}
