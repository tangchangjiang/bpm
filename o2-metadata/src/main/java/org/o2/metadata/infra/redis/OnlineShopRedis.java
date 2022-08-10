package org.o2.metadata.infra.redis;


import org.o2.metadata.infra.entity.OnlineShop;

import java.util.List;

/**
 * 承运商
 *
 * @author yipeng.zhu@hand-china.com 2021-08-05
 **/
public interface OnlineShopRedis {
    /**
     * 查询网店
     *
     * @param onlineShopCode 网店编码
     * @param tenantId       租户id
     * @return 网店
     */
    OnlineShop getOnlineShop(String onlineShopCode, Long tenantId);

    /**
     * 查询网店code
     *
     * @param onlineShopCodes 网店code
     * @return List<OnlineShop>
     */
    List<OnlineShop> selectShopList(List<String> onlineShopCodes);
}
