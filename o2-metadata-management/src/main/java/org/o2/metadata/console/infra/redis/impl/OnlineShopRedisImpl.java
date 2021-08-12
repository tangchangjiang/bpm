package org.o2.metadata.console.infra.redis.impl;

import org.hzero.core.base.BaseConstants;
import org.o2.core.helper.FastJsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.console.app.bo.OnlineShopRedisBO;
import org.o2.metadata.console.infra.constant.OnlineShopConstants;
import org.o2.metadata.console.infra.entity.OnlineShop;
import org.o2.metadata.console.infra.redis.OnlineShopRedis;
import org.o2.metadata.console.infra.repository.OnlineShopRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 *
 * 网店
 *
 * @author yipeng.zhu@hand-china.com 2021-08-06
 **/
@Component
public class OnlineShopRedisImpl implements OnlineShopRedis {
    private OnlineShopRepository onlineShopRepository;
    private RedisCacheClient redisCacheClient;
    public OnlineShopRedisImpl(OnlineShopRepository onlineShopRepository,
                               RedisCacheClient redisCacheClient) {
        this.onlineShopRepository = onlineShopRepository;
        this.redisCacheClient = redisCacheClient;
    }

    @Override
    public void batchUpdateRedis(String onlineShopCode, Long tenantId) {
        OnlineShop query = new OnlineShop();
        query.setOnlineShopCode(onlineShopCode);
        query.setTenantId(tenantId);

        List<OnlineShop> list = onlineShopRepository.select(query);
        if (list.isEmpty()) {
            return;
        }
        OnlineShop onlineShop = list.get(0);
        String key = OnlineShopConstants.Redis.getOnlineShopKey(onlineShopCode);
        if (onlineShop.getActiveFlag().equals(BaseConstants.Flag.NO)) {
            redisCacheClient.delete(key);
            return;
        }
        OnlineShopRedisBO bo = new OnlineShopRedisBO();
        bo.setCatalogCode(onlineShop.getCatalogCode());
        bo.setCatalogVersionCode(onlineShop.getCatalogVersionCode());
        bo.setOnlineShopCode(onlineShopCode);
        bo.setOnlineShopName(onlineShop.getOnlineShopName());
        bo.setPlatformCode(onlineShop.getPlatformCode());
        bo.setPlatformShopCode(onlineShop.getPlatformShopCode());
        bo.setTenantId(String.valueOf(onlineShop.getTenantId()));
        Map<String, String> map = FastJsonHelper.stringToMap(FastJsonHelper.objectToString(bo));
        redisCacheClient.opsForHash().putAll(key,map);
    }
}
