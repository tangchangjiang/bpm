package org.o2.metadata.infra.convertor;

import org.o2.metadata.api.vo.OnlineShopVO;
import org.o2.metadata.infra.entity.OnlineShop;

/**
 *
 * 承运商
 *
 * @author yipeng.zhu@hand-china.com 2021-08-05
 **/
public class OnlineShopConverter {
    /**
     * po->Vo
     * @date 2021-08-09
     * @param onlineShop 网店
     * @return  Vo
     */
    public static OnlineShopVO poToVoObject(OnlineShop onlineShop) {

        if (onlineShop == null) {
            return null;
        }
        OnlineShopVO onlineShopVO = new OnlineShopVO();
        onlineShopVO.setOnlineShopCode(onlineShop.getOnlineShopCode());
        onlineShopVO.setOnlineShopName(onlineShop.getOnlineShopName());
        onlineShopVO.setTenantId(onlineShop.getTenantId());
        return onlineShopVO;
    }


}
