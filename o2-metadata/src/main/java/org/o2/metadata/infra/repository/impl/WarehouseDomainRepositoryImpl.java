package org.o2.metadata.infra.repository.impl;

import org.o2.metadata.domain.warehouse.domain.WarehouseDO;
import org.o2.metadata.domain.warehouse.repository.WarehouseDomainRepository;
import org.o2.metadata.infra.convertor.WarehouseConvertor;
import org.o2.metadata.infra.redis.WarehouseRedis;
import org.springframework.stereotype.Component;

import java.util.List;


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
    public List<WarehouseDO> listWarehouses(List<String> warehouseCodes, Long tenantId) {
        return WarehouseConvertor.poToDoListObjects(warehouseRedis.listWarehouses(warehouseCodes,tenantId));
    }
}