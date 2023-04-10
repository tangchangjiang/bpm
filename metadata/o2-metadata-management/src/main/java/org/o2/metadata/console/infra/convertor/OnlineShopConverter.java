package org.o2.metadata.console.infra.convertor;

import org.o2.metadata.console.api.co.OnlineShopCO;
import org.o2.metadata.console.app.bo.OnlineShopCacheBO;
import org.o2.metadata.console.infra.entity.OnlineShop;
import org.o2.metadata.management.client.domain.dto.OnlineShopDTO;

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
     * po->CO
     * @param onlineShop 网店
     * @return  vo
     */
    public static OnlineShopCO poToCoObject(OnlineShop onlineShop) {

        if (onlineShop == null) {
            return null;
        }
        OnlineShopCO co = new OnlineShopCO();
        co.setOnlineShopId(onlineShop.getOnlineShopId());
        co.setOnlineShopCode(onlineShop.getOnlineShopCode());
        co.setCatalogCode(onlineShop.getCatalogCode());
        co.setCatalogName(onlineShop.getCatalogName());
        co.setCatalogVersionCode(onlineShop.getCatalogVersionCode());
        co.setCatalogVersionName(onlineShop.getCatalogVersionName());
        co.setOnlineShopName(onlineShop.getOnlineShopName());
        co.setActiveFlag(onlineShop.getActiveFlag());
        co.setPlatformCode(onlineShop.getPlatformCode());
        co.setPlatformShopCode(onlineShop.getPlatformShopCode());
        co.setPlatformName(onlineShop.getPlatformName());
        co.setSourcedFlag(onlineShop.getSourcedFlag());
        co.setPickedUpFlag(onlineShop.getPickedUpFlag());
        co.setReturnedFlag(onlineShop.getReturnedFlag());
        co.setExchangedFlag(onlineShop.getExchangedFlag());
        co.setEnableSplitFlag(onlineShop.getEnableSplitFlag());
        co.setAccountNumber(onlineShop.getAccountNumber());
        co.setOnlineShopType(onlineShop.getOnlineShopType());
        co.setBusinessTypeCode(onlineShop.getBusinessTypeCode());
        co.setTenantId(onlineShop.getTenantId());
        return co;
    }

    /**
     * PO 转 CO
     * @param onlineShopList 网店
     * @return  list
     */
    public static List<OnlineShopCO> poToCoListObjects(List<OnlineShop> onlineShopList) {
        List<OnlineShopCO> cos = new ArrayList<>();
        if (onlineShopList == null) {
            return cos;
        }
        for (OnlineShop onlineShop : onlineShopList) {
            cos.add(poToCoObject(onlineShop));
        }
        return cos;
    }

    /**
     * po->bo
     * @param  onlineShop 网店
     * @return bo
     */
    public static OnlineShopCacheBO poToBoObject(OnlineShop onlineShop) {

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
        onlineShopCacheBO.setPickedUpFlag(onlineShop.getPickedUpFlag());
        onlineShopCacheBO.setReturnedFlag(onlineShop.getReturnedFlag());
        onlineShopCacheBO.setExchangedFlag(onlineShop.getExchangedFlag());
        onlineShopCacheBO.setEnableSplitFlag(onlineShop.getEnableSplitFlag());
        onlineShopCacheBO.setTenantId(onlineShop.getTenantId());
        onlineShopCacheBO.setIsDefault(onlineShop.getIsDefault());
        onlineShopCacheBO.setSourcedFlag(onlineShop.getSourcedFlag());
        onlineShopCacheBO.setOnlineShopType(onlineShop.getOnlineShopType());
        onlineShopCacheBO.setBusinessTypeCode(onlineShop.getBusinessTypeCode());
        onlineShopCacheBO.setActiveFlag(onlineShop.getActiveFlag());
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

    /**
     * DTO 转 PO
     * @param onlineShopDTO DTO
     * @return PO
     */
    public static OnlineShop dtoToBoOnlineShop(OnlineShopDTO onlineShopDTO) {
        if (null == onlineShopDTO) {
            return null;
        }
        OnlineShop onlineShop = new OnlineShop();
        onlineShop.setTenantId(onlineShopDTO.getTenantId());
        onlineShop.setActiveFlag(onlineShopDTO.getActiveFlag());
        onlineShop.setCatalogCode(onlineShopDTO.getCatalogCode());
        onlineShop.setCatalogVersionCode(onlineShopDTO.getCatalogVersionCode());
        onlineShop.setDefaultCurrency(onlineShopDTO.getDefaultCurrency());
        onlineShop.setEnableSplitFlag(onlineShopDTO.getEnableSplitFlag());
        onlineShop.setExchangedFlag(onlineShopDTO.getExchangedFlag());
        onlineShop.setReturnedFlag(onlineShopDTO.getReturnedFlag());
        onlineShop.setIsDefault(onlineShopDTO.getIsDefault());
        onlineShop.setOnlineShopCode(onlineShopDTO.getOnlineShopCode());
        onlineShop.setOnlineShopName(onlineShopDTO.getOnlineShopName());
        onlineShop.setPickedUpFlag(onlineShopDTO.getPickedUpFlag());
        onlineShop.setSourcedFlag(onlineShopDTO.getSourcedFlag());
        onlineShop.setPlatformCode(onlineShopDTO.getPlatformCode());
        onlineShop.setPlatformShopCode(onlineShopDTO.getPlatformShopCode());
        onlineShop.setOnlineShopType(onlineShopDTO.getOnlineShopType());
        onlineShop.setBusinessTypeCode(onlineShopDTO.getBusinessTypeCode());

        return onlineShop;
    }
}
