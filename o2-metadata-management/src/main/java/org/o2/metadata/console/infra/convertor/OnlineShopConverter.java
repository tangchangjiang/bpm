package org.o2.metadata.console.infra.convertor;

import org.o2.metadata.console.api.vo.OnlineShopVO;
import org.o2.metadata.console.app.bo.OnlineShopCacheBO;
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
    /**
     * po->vo
     * @param onlineShop 网店
     * @return  vo
     */
    public static OnlineShopVO poToVoObject(OnlineShop onlineShop) {

        if (onlineShop == null) {
            return null;
        }
        OnlineShopVO onlineShopVO = new OnlineShopVO();
        onlineShopVO.setOnlineShopId(onlineShop.getOnlineShopId());
        onlineShopVO.setOnlineShopCode(onlineShop.getOnlineShopCode());
        onlineShopVO.setOnlineShopName(onlineShop.getOnlineShopName());
        onlineShopVO.setCatalogCode(onlineShop.getCatalogCode());
        onlineShopVO.setPlatformCode(onlineShop.getPlatformCode());
        onlineShopVO.setPlatformShopCode(onlineShop.getPlatformShopCode());
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


    /**
     * po->bo
     * @param  onlineShop 网店
     * @return bo
     */
    private static OnlineShopCacheBO poToBoObject(OnlineShop onlineShop){

        if (onlineShop == null) {
            return null;
        }
        OnlineShopCacheBO onlineShopCacheBO = new OnlineShopCacheBO();
        onlineShopCacheBO.setPlatformCode(onlineShop.getPlatformCode());
        onlineShopCacheBO.setOnlineShopName(onlineShop.getOnlineShopName());
        onlineShopCacheBO.setOnlineShopCode(onlineShop.getOnlineShopCode());
        onlineShopCacheBO.setPlatformShopCode(onlineShop.getPlatformShopCode());
        onlineShopCacheBO.setCatalogCode(onlineShop.getCatalogCode());
        onlineShopCacheBO.setCatalogVersionCode(onlineShop.getCatalogVersionCode());
        onlineShopCacheBO.setTenantId(onlineShop.getTenantId());
        return onlineShopCacheBO;
    }

    /**
     * PO 转 BO
     * @param onlineShopList 网店
     * @return  list
     */
    public static List<OnlineShopCacheBO> poToBoListObjects(List<OnlineShop> onlineShopList) {
        List<OnlineShopCacheBO> onlineShopBOList = new ArrayList<>();
        if (onlineShopList == null) {
            return onlineShopBOList;
        }
        for (OnlineShop onlineShop : onlineShopList) {
            onlineShopBOList.add(poToBoObject(onlineShop));
        }
        return onlineShopBOList;
    }
}
