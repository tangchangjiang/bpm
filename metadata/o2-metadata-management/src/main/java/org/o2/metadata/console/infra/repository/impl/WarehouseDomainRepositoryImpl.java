package org.o2.metadata.console.infra.repository.impl;

import org.o2.metadata.console.infra.convertor.WarehouseConverter;
import org.o2.metadata.console.infra.redis.WarehouseRedis;
import org.o2.metadata.domain.warehouse.domain.WarehouseDO;
import org.o2.metadata.domain.warehouse.repository.WarehouseDomainRepository;
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
        return WarehouseConverter.poToDoListObjects(warehouseRedis.listWarehouses(warehouseCodes, tenantId));
    }

    @Override
    public void updateExpressValue(String warehouseCode, String increment, Long tenantId) {
    }
}
