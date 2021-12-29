package org.o2.metadata.infra.redis;


import org.o2.metadata.infra.entity.OnlineShop;

/**
 *
 * 承运商
 *
 * @author yipeng.zhu@hand-china.com 2021-08-05
 **/
public interface OnlineShopRedis {
    /**
     * 查询网店
     * @param onlineShopCode 网店编码
     * @return  网店
     */
    OnlineShop getOnlineShop(String onlineShopCode,Long tenantId);
}
