package org.o2.metadata.app.service;

import org.o2.metadata.api.co.OnlineShopCO;

import java.util.List;

/**
 * 承运商
 *
 * @author yipeng.zhu@hand-china.com 2021-08-05
 **/
public interface OnlineShopService {
    /**
     * 查询网店
     *
     * @param onlineShopCode 网店编码
     * @param tenantId       租户id
     * @return 网店
     */
    OnlineShopCO getOnlineShop(String onlineShopCode, Long tenantId);

    /**
     * 批量查询网店
     *
     * @param onlineShopCodes 网店
     * @return List<OnlineShopCO>
     */
    List<OnlineShopCO> queryShopList(List<String> onlineShopCodes);

    /**
     * 批量查询网店-根据网店类型
     *
     * @param tenantId 租户ID
     * @param onlineShopType 网店类型
     * @return List<OnlineShopCO>
     */
    List<OnlineShopCO> queryShopListByType(Long tenantId, String onlineShopType);
}
