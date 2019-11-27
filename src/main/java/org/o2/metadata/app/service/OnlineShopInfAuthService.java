package org.o2.metadata.app.service;

import org.o2.ext.metadata.domain.entity.OnlineShopInfAuth;

/**
 * 网店接口表应用服务
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface OnlineShopInfAuthService {

    /**
     * 新增或修改网店接口
     *
     * @param onlineShopInfAuth 网店信息
     * @return OnlineShopInfAuth
     */
    OnlineShopInfAuth updateOrInsert(OnlineShopInfAuth onlineShopInfAuth);
}
