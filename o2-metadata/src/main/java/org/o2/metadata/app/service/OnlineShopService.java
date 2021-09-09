package org.o2.metadata.app.service;

import org.o2.metadata.api.vo.OnlineShopCO;

/**
 *
 * 承运商
 *
 * @author yipeng.zhu@hand-china.com 2021-08-05
 **/
public interface OnlineShopService {
    /**
     * 查询网店
     * @param onlineShopCode 网店编码
     * @return  网店
     */
   OnlineShopCO getOnlineShop(String onlineShopCode);

}
