package org.o2.metadata.console.app.service;

import org.o2.metadata.console.infra.entity.OnlineShop;

/**
 * 网店应用服务
 * @author: yipeng.zhu@hand-china.com 2020-06-03 09:50
 **/
public interface OnlineShopService {
    /**
     * 创建网店
     * @date 2020-06-03
     * @param onlineShop 网店信息
     */
    void createOnlineShop(final OnlineShop onlineShop);
    /**
     * 修改网店
     * @date 2020-06-03
     * @param  onlineShop 网店信息
     */
    void updateOnlineShop(final  OnlineShop onlineShop);
}
