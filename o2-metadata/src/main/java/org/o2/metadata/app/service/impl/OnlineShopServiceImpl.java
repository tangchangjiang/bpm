package org.o2.metadata.app.service.impl;

import org.o2.metadata.api.co.OnlineShopCO;
import org.o2.metadata.app.service.OnlineShopService;
import org.o2.metadata.infra.convertor.OnlineShopConverter;
import org.o2.metadata.infra.redis.OnlineShopRedis;
import org.springframework.stereotype.Service;

/**
 *
 * 承运商
 *
 * @author yipeng.zhu@hand-china.com 2021-08-05
 **/
@Service
public class OnlineShopServiceImpl implements OnlineShopService {
    private final OnlineShopRedis onlineShopRedis;

    public OnlineShopServiceImpl(OnlineShopRedis onlineShopRedis) {
        this.onlineShopRedis = onlineShopRedis;
    }

    @Override
    public OnlineShopCO getOnlineShop(String onlineShopCode,Long tenantId) {
        return OnlineShopConverter.poToCoObject(onlineShopRedis.getOnlineShop(onlineShopCode,tenantId));
    }
}
