package org.o2.metadata.console.infra.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.console.infra.entity.OnlineShop;
import org.o2.metadata.console.infra.entity.OnlineShopInfAuth;

import java.util.List;

/**
 * 网店接口表资源库
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface OnlineShopInfAuthRepository extends BaseRepository<OnlineShopInfAuth> {

    /**
     * 条件查询
     *
     * @param onlineShopId
     * @return OnlineShopInfAuth
     */
    OnlineShopInfAuth listOnlineShopInfAuthByOption(Long onlineShopId);

    /**
     * 条件查询
     *
     * @param onlineShop
     * @return List<OnlineShopInfAuth>
     */
    List<OnlineShopInfAuth> listInfAuthByOnlineShop(OnlineShop onlineShop);
}
