package org.o2.metadata.console.infra.convertor;

import org.o2.metadata.console.api.vo.OnlineShopVO;
import org.o2.metadata.console.infra.entity.OnlineShop;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 网店
 *
 * @author yipeng.zhu@hand-china.com 2021-08-02
 **/
public class OnlineShopConverter {

    private OnlineShopConverter() {
    }

    public static OnlineShopVO poToVoObject(OnlineShop onlineShop) {

        if (onlineShop == null) {
            return null;
        }
        OnlineShopVO onlineShopVO = new OnlineShopVO();
        onlineShopVO.setOnlineShopId(onlineShop.getOnlineShopId());
        onlineShopVO.setOnlineShopCode(onlineShop.getOnlineShopCode());
        onlineShopVO.setOnlineShopName(onlineShop.getOnlineShopName());
        onlineShopVO.setCatalogCode(onlineShop.getCatalogCode());
        onlineShopVO.setCatalogVersionCode(onlineShop.getCatalogVersionCode());
        return onlineShopVO;
    }

    /**
     * PO 转 VO
     * @param onlineShopList 网店
     * @return  list
     */
    public static List<OnlineShopVO> poToVoListObjects(List<OnlineShop> onlineShopList) {
        List<OnlineShopVO> onlineShopVOList = new ArrayList<>();
        if (onlineShopList == null) {
            return onlineShopVOList;
        }
        for (OnlineShop onlineShop : onlineShopList) {
            onlineShopVOList.add(poToVoObject(onlineShop));
        }
        return onlineShopVOList;
    }
}
