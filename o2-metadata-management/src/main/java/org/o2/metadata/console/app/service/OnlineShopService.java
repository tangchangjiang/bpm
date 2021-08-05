package org.o2.metadata.console.app.service;

import org.o2.metadata.console.api.vo.OnlineShopVO;
import org.o2.metadata.console.infra.entity.OnlineShop;

import java.util.List;

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

    /**
     * 查询网店code
     * @param platformCode 平台
     * @param shopName 店铺名称
     * @return List<OnlineShopVO> 查询结果
     */
    List<OnlineShopVO> getOnlineShopCode(String platformCode, String shopName);
}
