package org.o2.metadata.infra.constants;

/**
 * 承运商
 *
 * @author yipeng.zhu@hand-china.com 2021-08-05
 **/
public interface CarrierConstants {

    /**
     * Redis key
     */
    interface Redis {
        /**
         * redis key(hash): o2md:carrier:{tenantId}
         */
        String CARRIER_KEY = "o2md:carrier:{%s}:all";
        /**
         * redis key(hash): o2md:carrier:{tenantId}:{carrierCode}
         */
        String CARRIER_MULTI_KEY = "o2md:carrier:{%s}:%s";

        /**
         * GetFreightDefaultKey
         *
         * @param tenantId 租户ID
         * @return key
         */
        static String getCarrierKey(Long tenantId) {
            return String.format(CARRIER_KEY, tenantId);
        }

        /**
         * 获取多语言key
         *
         * @param tenantId    租户id
         * @param carrierCode code
         * @return key
         */
        static String getCarrierMultiKey(Long tenantId, String carrierCode) {
            return String.format(CARRIER_MULTI_KEY, tenantId, carrierCode);
        }
    }
}
