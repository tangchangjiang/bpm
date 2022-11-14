package org.o2.metadata.infra.constants;

/**
 *
 * 网店
 *
 * @author yipeng.zhu@hand-china.com 2021-08-05
 **/
public interface OnlineShopConstants {

    interface Redis {
        /**
         *  o2md:onlineShop:[tenantId]:[shopCode]
         */
        String ONLINE_SHOP_KEY = "o2md:onlineShop:{%d}:all";

        /**
         *  获取key
         * @param tenantId 网店编码
         * @return key
         */
        static String getOnlineShopKey(Long tenantId) {
            return String.format(ONLINE_SHOP_KEY, tenantId);
        }
    }
}
