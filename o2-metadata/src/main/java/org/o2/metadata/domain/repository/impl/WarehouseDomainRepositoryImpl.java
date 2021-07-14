package org.o2.metadata.domain.repository.impl;

import org.o2.metadata.domain.warehouse.domain.WarehouseDO;
import org.o2.metadata.domain.warehouse.repository.WarehouseDomainRepository;
import org.o2.metadata.infra.convertor.WarehouseConvertor;
import org.o2.metadata.infra.redis.WarehouseRedis;
import org.springframework.stereotype.Component;


/**
 *
 * 仓库
 *
 * @author yipeng.zhu@hand-china.com 2021-07-13
 **/
@Component
public class WarehouseDomainRepositoryImpl implements WarehouseDomainRepository {
    private final WarehouseRedis warehouseRedis;

    public WarehouseDomainRepositoryImpl(WarehouseRedis warehouseRedis) {
        this.warehouseRedis = warehouseRedis;
    }

    @Override
    public WarehouseDO getWarehouse(String warehouseCode, Long tenantId) {
        return WarehouseConvertor.poToDoObject(warehouseRedis.getWarehouse(warehouseCode,tenantId));
    }
}
