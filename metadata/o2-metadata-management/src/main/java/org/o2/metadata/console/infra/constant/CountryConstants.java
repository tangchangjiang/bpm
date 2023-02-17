package org.o2.metadata.console.infra.constant;

public interface CountryConstants {

    interface Redis {
        /**
         * o2md:country:region:static-resource:[tenantId]:countryCode
         */
        String REGION_RESOURCE_URL_KEY = "o2md:country:region:static-resource:{%s}:%s";

        static String getRegionResourceUrlKey(Long tenantId, String countryCode) {
            return String.format(REGION_RESOURCE_URL_KEY, tenantId, countryCode);
        }
    }
}
