package org.o2.metadata.infra.constants;

/**
 *
 * 承运商
 *
 * @author yipeng.zhu@hand-china.com 2021-08-05
 **/
public interface CarrierConstants {

    interface Redis {
        /**
         * redis key(hash): o2md:carrier:{tenantId}
         */
        String CARRIER_KEY = "o2md:carrier:{%s}:all";

        /**
         * GetFreightDefaultKey
         *
         * @param tenantId 租户ID
         * @return key
         */
        static String getCarrierKey(Long tenantId) {
            return String.format(CARRIER_KEY, tenantId);
        }
    }
}
