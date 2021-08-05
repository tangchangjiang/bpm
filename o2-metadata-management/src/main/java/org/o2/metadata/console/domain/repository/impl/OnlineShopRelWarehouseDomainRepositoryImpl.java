package org.o2.metadata.console.domain.repository.impl;

import org.o2.metadata.console.infra.convertor.OnlineShopRelWarehouseConverter;
import org.o2.metadata.console.infra.redis.OnlineShopRelWarehouseRedis;
import org.o2.metadata.domain.onlineshop.domain.OnlineShopRelWarehouseDO;
import org.o2.metadata.domain.onlineshop.repository.OnlineShopRelWarehouseDomainRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * 网店关联仓库
 *
 * @author yipeng.zhu@hand-china.com 2021-07-13
 **/
@Component
public class OnlineShopRelWarehouseDomainRepositoryImpl implements OnlineShopRelWarehouseDomainRepository {
    private final OnlineShopRelWarehouseRedis onlineShopRelWarehouseRedis;

    public OnlineShopRelWarehouseDomainRepositoryImpl(OnlineShopRelWarehouseRedis onlineShopRelWarehouseRedis) {
        this.onlineShopRelWarehouseRedis = onlineShopRelWarehouseRedis;
    }

    @Override
    public List<OnlineShopRelWarehouseDO> listOnlineShopRelWarehouses(String onlineShopCode, Long tenantId) {
        return OnlineShopRelWarehouseConverter.poToDoListObjects(onlineShopRelWarehouseRedis.listOnlineShopRelWarehouses(onlineShopCode,tenantId));
    }
}
