package org.o2.metadata.console.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.o2.metadata.console.domain.entity.OnlineShop;
import org.o2.metadata.console.domain.entity.OnlineShopInfAuth;

import java.util.List;


/**
 * 网店接口表Mapper
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface OnlineShopInfAuthMapper extends BaseMapper<OnlineShopInfAuth> {

    /**
     * 根据 onlineShopInfAuthId 查询网店接口
     *
     * @param onlineShopId
     * @return OnlineShopInfAuth
     */
    OnlineShopInfAuth listOnlineShopInfAuthByOption(@Param("onlineShopId") Long onlineShopId);

    /**
     * 根据 onlineShop 查询网店接口
     *
     * @param onlineShop
     * @return OnlineShopInfAuth
     */
    List<OnlineShopInfAuth> listInfAuthByOnlineShop(OnlineShop onlineShop);

}
