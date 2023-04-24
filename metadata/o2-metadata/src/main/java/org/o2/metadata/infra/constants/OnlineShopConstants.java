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
         *  set o2md:onlineShop:[tenantId]:all
         *  该结构暂需保留，因为C端有查询单租户下所有网店的需求
         */
        String ONLINE_SHOP_KEY = "o2md:onlineShop:{%d}:all";

        /**
         * 网店详情 hash o2md:onlineShop:detail
         */
        String ONLINE_SHOP_DETAIL_KEY = "o2md:onlineShop:detail";

        /**
         *  获取key
         * @param tenantId 网店编码
         * @return key
         */
        static String getOnlineShopKey(Long tenantId) {
            return String.format(ONLINE_SHOP_KEY, tenantId);
        }

        /**
         * 获取网店详情key
         *
         * @return 全部网店详情key
         */
        static String getOnlineShopDetailKey() {
            return ONLINE_SHOP_DETAIL_KEY;
        }
    }
}
