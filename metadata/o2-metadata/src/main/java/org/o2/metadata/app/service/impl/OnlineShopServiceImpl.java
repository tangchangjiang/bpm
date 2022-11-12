package org.o2.metadata.app.service.impl;

import org.o2.core.convert.ListConverter;
import org.o2.metadata.api.co.OnlineShopCO;
import org.o2.metadata.app.service.OnlineShopService;
import org.o2.metadata.infra.convertor.OnlineShopConverter;
import org.o2.metadata.infra.redis.OnlineShopRedis;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 网店服务
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
    public OnlineShopCO getOnlineShop(String onlineShopCode, Long tenantId) {
        return OnlineShopConverter.poToCoObject(onlineShopRedis.getOnlineShop(onlineShopCode, tenantId));
    }

    @Override
    public List<OnlineShopCO> queryShopList(List<String> onlineShopCodes) {
        // TODO 添加缓存
        return ListConverter.toList(onlineShopRedis.selectShopList(onlineShopCodes), OnlineShopConverter::poToCoObject);
    }

    @Override
    public List<OnlineShopCO> queryShopListByType(Long tenantId, String onlineShopType) {
        return ListConverter.toList(onlineShopRedis.selectShopListByType(tenantId, onlineShopType), OnlineShopConverter::poToCoObject);
    }

    @Override
    public List<OnlineShopCO> batchQueryOnlineShop(Long tenantId, List<String> onlineShopCodes) {
        return ListConverter.toList(onlineShopRedis.batchQueryShopList(tenantId, onlineShopCodes), OnlineShopConverter::poToCoObject);
    }

}
