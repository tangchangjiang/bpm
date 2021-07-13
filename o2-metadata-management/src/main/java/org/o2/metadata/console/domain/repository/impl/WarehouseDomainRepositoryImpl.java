package org.o2.metadata.console.domain.repository.impl;

import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.domain.warehouse.domain.WarehouseDO;
import org.o2.metadata.domain.warehouse.repository.WarehouseDomainRepository;
import org.springframework.stereotype.Component;

/**
 *
 * 仓库
 *
 * @author yipeng.zhu@hand-china.com 2021-07-13
 **/
@Component
public class WarehouseDomainRepositoryImpl implements WarehouseDomainRepository {
    private final RedisCacheClient redisCacheClient;

    public WarehouseDomainRepositoryImpl(RedisCacheClient redisCacheClient) {
        this.redisCacheClient = redisCacheClient;
    }

    @Override
    public WarehouseDO getWarehouse(String warehouseCode, Long tenantId) {
        return null;
    }
}
