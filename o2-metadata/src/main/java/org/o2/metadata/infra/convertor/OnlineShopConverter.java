package org.o2.metadata.infra.convertor;

import org.o2.metadata.api.co.OnlineShopCO;
import org.o2.metadata.infra.entity.OnlineShop;

/**
 *
 * 承运商
 *
 * @author yipeng.zhu@hand-china.com 2021-08-05
 **/
public class OnlineShopConverter {
    private OnlineShopConverter(){}
    /**
     * po->Vo
     * @date 2021-08-09
     * @param onlineShop 网店
     * @return  Vo
     */
    public static OnlineShopCO poToVoObject(OnlineShop onlineShop) {

        if (onlineShop == null) {
            return null;
        }
        OnlineShopCO co = new OnlineShopCO();
        co.setOnlineShopCode(onlineShop.getOnlineShopCode());
        co.setOnlineShopName(onlineShop.getOnlineShopName());
        co.setTenantId(onlineShop.getTenantId());
        co.setPlatformCode(onlineShop.getPlatformCode());
        co.setPickedUpFlag(onlineShop.getPickedUpFlag());
        return co;
    }


}
