package org.o2.metadata.domain.onlineshop.service.impl;

import org.o2.metadata.domain.onlineshop.domain.OnlineShopRelWarehouseDO;
import org.o2.metadata.domain.onlineshop.repository.OnlineShopRelWarehouseDomainRepository;
import org.o2.metadata.domain.onlineshop.service.OnlineShopRelWarehouseDomainService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * 网店关联仓库
 *
 * @author yipeng.zhu@hand-china.com 2021-07-13
 **/
@Service
public class OnlineShopRelWarehouseDomainServiceImpl implements OnlineShopRelWarehouseDomainService {
    private final OnlineShopRelWarehouseDomainRepository onlineShopRelWarehouseDomainRepository;

    public OnlineShopRelWarehouseDomainServiceImpl(OnlineShopRelWarehouseDomainRepository onlineShopRelWarehouseDomainRepository) {
        this.onlineShopRelWarehouseDomainRepository = onlineShopRelWarehouseDomainRepository;
    }

    @Override
    public List<OnlineShopRelWarehouseDO> listOnlineShopRelWarehouses(String onlineShopCode, Long tenantId) {
        return onlineShopRelWarehouseDomainRepository.listOnlineShopRelWarehouses(onlineShopCode,tenantId);
    }
}
